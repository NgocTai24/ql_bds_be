package vn.com.bds.usecase;

import lombok.Builder;
import lombok.Value;
import vn.com.bds.domain.model.User;

import java.util.List;
import java.util.UUID;
import java.io.InputStream; // Keep if using InputStream in command

public interface ManageUserUseCase {

    // === QUERIES (Read Operations) ===

    /**
     * Get the profile of the currently authenticated user.
     * @param email The email extracted from the authentication token.
     * @return The User domain model.
     */
    User getMyProfile(String email); // Renamed from execute

    /**
     * Get a user by their unique ID.
     * @param id The UUID of the user.
     * @return The User domain model.
     */
    User getUserById(UUID id); // Renamed from execute

    /**
     * Get a list of all users (consider adding pagination later).
     * @return A list of User domain models.
     */
    List<User> getAllUsers(); // Renamed from getAllUser

    // === COMMANDS (Write Operations) ===

    /**
     * Update the profile of the currently authenticated user.
     * @param command The data to update.
     * @return The updated User domain model.
     */
    User updateMyProfile(UpdateMyProfileCommand command); // Renamed from execute

    /**
     * Delete a user by their unique ID (typically admin only).
     * @param id The UUID of the user to delete.
     */
    void deleteUserById(UUID id); // Renamed from deleteById

    // === Nested Command/Query Classes ===

    @Value
    @Builder
    class UpdateMyProfileCommand {
        String email; // Email of user performing the update (from token)
        String fullname;
        String phone;
        byte[] fileBytes; // Keep the updated field
        String fileName;
    }

    // You could add other specific commands/queries here if needed later
    // e.g., @Value class FindUserByCriteriaQuery { ... }
}