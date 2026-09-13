package assembly.general.api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileResponse {
    private boolean success;
    private String message;
    private ProfileDto profile;

    public ProfileResponse() {}

    public ProfileResponse(
            boolean success,
            String message
    ){
        this.success = success;
        this.message = message;
    }

    public ProfileResponse(
            boolean success,
            String message,
            ProfileDto profile
    ){
        this.success = success;
        this.message = message;
        this.profile = profile;
    }

    public static ProfileResponse success(String message, ProfileDto profile){
        return new ProfileResponse(true, message, profile);
    }

    public static ProfileResponse failure(String message){
        return new ProfileResponse(true, message);
    }


}
