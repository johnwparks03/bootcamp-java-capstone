package assembly.general.api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name="users")
@Getter
@Setter
public class User extends AuditableEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_id")
    private UUID id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name="password_hash", nullable = false)
    private String password;

    @Column(name="first_name", nullable = false)
    private String firstName;

    @Column(name="last_name", nullable = false)
    private String lastName;

    @Column(name="phone_number", nullable = false)
    private String phoneNumber;

    @Column(name="role", nullable = false)
    private Role role;

    @Column(name="membership_status", nullable = false)
    private MembershipStatus membershipStatus;

    @Column(name="member_since_date")
    private LocalDateTime memberSinceDate;

    @Column(name="created_at")
    private Instant createdAt;

    protected User(){
    }

    public User(String email, String password, String firstName, String lastName, String phoneNumber, Role role, MembershipStatus membershipStatus){
        this();
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.membershipStatus = membershipStatus;
        this.memberSinceDate = LocalDateTime.now();
        this.createdAt = Instant.now();
    }


}
