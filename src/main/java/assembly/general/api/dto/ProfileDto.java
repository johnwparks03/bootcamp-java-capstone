package assembly.general.api.dto;

import assembly.general.api.entity.MembershipStatus;
import assembly.general.api.entity.Role;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class ProfileDto {
    private UUID id;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private Role role;
    private MembershipStatus membershipStatus;
    private LocalDateTime memberSinceDate;
    private Integer activeReservations;
    private Integer borrowingHistory;

    public ProfileDto() {}

    public ProfileDto(
            UUID id,
            String email,
            String firstName,
            String lastName,
            String phoneNumber,
            Role role,
            MembershipStatus membershipStatus,
            Integer activeReservations,
            Integer borrowingHistory
    ){
        this();
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.membershipStatus = membershipStatus;
        this.memberSinceDate = LocalDateTime.now();
        this.activeReservations = activeReservations;
        this.borrowingHistory = borrowingHistory;
    }
}
