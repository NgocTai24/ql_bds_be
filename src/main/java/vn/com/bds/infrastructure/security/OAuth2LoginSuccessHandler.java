package vn.com.bds.infrastructure.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
// import lombok.RequiredArgsConstructor; // <-- XÓA DÒNG NÀY
import org.springframework.beans.factory.annotation.Value; // <-- THÊM IMPORT NÀY
import org.springframework.context.annotation.Lazy; // <-- THÊM IMPORT NÀY
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import vn.com.bds.usecase.LoginWithGoogleUseCase;
import vn.com.bds.usecase.RegisterUserUseCase;

import java.io.IOException;

@Component
// @RequiredArgsConstructor // <-- XÓA DÒNG NÀY
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final LoginWithGoogleUseCase loginWithGoogleUseCase;
    private final String frontendUrl;

    // --- VIẾT CONSTRUCTOR BẰNG TAY ---
    public OAuth2LoginSuccessHandler(
            @Lazy LoginWithGoogleUseCase loginWithGoogleUseCase, // <-- THÊM @Lazy ở đây
            @Value("${application.frontend.url}") String frontendUrl
    ) {
        this.loginWithGoogleUseCase = loginWithGoogleUseCase;
        this.frontendUrl = frontendUrl;
    }
    // -------------------------------

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {

        // 1. Lấy thông tin user từ Google (do Spring cung cấp)
        DefaultOidcUser oidcUser = (DefaultOidcUser) authentication.getPrincipal();
        String email = oidcUser.getAttribute("email");
        String fullname = oidcUser.getAttribute("name");
        String avatarUrl = oidcUser.getAttribute("picture");

        // 2. Tạo Command để gọi UseCase
        LoginWithGoogleUseCase.GoogleLoginCommand command = LoginWithGoogleUseCase.GoogleLoginCommand.builder()
                .email(email)
                .fullname(fullname)
                .avatarUrl(avatarUrl)
                .build();

        // 3. Gọi UseCase để "tìm hoặc tạo" user và nhận về JWT
        RegisterUserUseCase.AuthResponse authResponse = loginWithGoogleUseCase.execute(command);
        String token = authResponse.getToken();

        // 4. Chuyển hướng người dùng về Frontend, đính kèm token
        String redirectUrl = frontendUrl + "/login-success?token=" + token;
        response.sendRedirect(redirectUrl);
    }
}