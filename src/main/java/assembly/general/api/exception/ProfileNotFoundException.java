package assembly.general.api.exception;

import java.util.UUID;

public class ProfileNotFoundException extends RuntimeException {
    public ProfileNotFoundException(UUID userId) {
        super("Profile not found with ID: "+ userId);
    }
}
