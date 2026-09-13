package assembly.general.api.dto;

import assembly.general.api.entity.MembershipStatus;
import assembly.general.api.entity.Role;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class RegisterResponse {
    private UUID userId;
    private String email;
    private String firstName;
    private String lastName;
    private Role role;
    private MembershipStatus membershipStatus;
    private Instant createdAt;
    private String message;

    public RegisterResponse() {}

    public  RegisterResponse(
            UUID userId,
            String email,
            String firstName,
            String lastName,
            Role role,
            MembershipStatus membershipStatus,
            Instant createdAt
    ){
        this.userId = userId;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.membershipStatus = membershipStatus;
        this.createdAt = createdAt;
        this.message = "Registration Successful";
    }
}
