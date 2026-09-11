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
public class UserDto {
    private UUID id;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private MembershipStatus membershipStatus;
    private LocalDateTime memberSinceDate;

    public UserDto() {}

    public UserDto(
            UUID id,
            String email,
            String firstName,
            String lastName,
            String phoneNumber,
            MembershipStatus membershipStatus){
        this();
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.membershipStatus = membershipStatus;
        this.memberSinceDate = LocalDateTime.now();
    }
}
