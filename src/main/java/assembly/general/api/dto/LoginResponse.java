package assembly.general.api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse {
    private String accessToken;
    private String tokenType;
    private Integer expiresIn;
    private UserDto user;

    public LoginResponse(
            String accessToken,
            String tokenType,
            Integer expiresIn,
            UserDto user
    ){
        this.accessToken = accessToken;
        this.tokenType = tokenType;
        this.expiresIn = expiresIn;
        this.user = user;
    }
}
