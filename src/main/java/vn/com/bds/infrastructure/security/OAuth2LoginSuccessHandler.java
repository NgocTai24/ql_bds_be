package vn.com.bds.infrastructure.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import vn.com.bds.presentation.dto.AuthResponse;
import vn.com.bds.usecase.ManageAuthUseCase;


import java.io.IOException;

@Component
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    // Inject the consolidated UseCase
    private final ManageAuthUseCase manageAuthUseCase;
    private final String frontendUrl;

    // Constructor using the consolidated UseCase
    public OAuth2LoginSuccessHandler(
            // Use the consolidated interface, keep @Lazy if needed for circular deps
            @Lazy ManageAuthUseCase manageAuthUseCase,
            @Value("${application.frontend.url}") String frontendUrl
    ) {
        this.manageAuthUseCase = manageAuthUseCase;
        this.frontendUrl = frontendUrl;
    }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {

        // 1. Get user info from Google
        DefaultOidcUser oidcUser = (DefaultOidcUser) authentication.getPrincipal();
        String email = oidcUser.getAttribute("email");
        String fullname = oidcUser.getAttribute("name");
        String avatarUrl = oidcUser.getAttribute("picture");

        // 2. Create Command using the nested class from ManageAuthUseCase
        ManageAuthUseCase.GoogleLoginCommand command = ManageAuthUseCase.GoogleLoginCommand.builder()
                .email(email)
                .fullname(fullname)
                .avatarUrl(avatarUrl)
                .build();

        // 3. Call the correct method on the consolidated UseCase
        //    The return type is now the standalone AuthResponse DTO
        AuthResponse authResponse = manageAuthUseCase.loginWithGoogle(command);
        String token = authResponse.getToken();

        // 4. Redirect to Frontend with token
        String redirectUrl = frontendUrl + "/login-success?token=" + token;
        response.sendRedirect(redirectUrl);
    }
}