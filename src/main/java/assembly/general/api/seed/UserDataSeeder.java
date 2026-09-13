package assembly.general.api.seed;

import assembly.general.api.entity.MembershipStatus;
import assembly.general.api.entity.Role;
import assembly.general.api.entity.User;
import assembly.general.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Order(1)
public class UserDataSeeder implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserDataSeeder(UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception{
        userRepository.deleteAll();

        String encodedPassword = passwordEncoder.encode("password");

        User user1 = new User(
                "test@test.com",
                encodedPassword,
                "Test",
                "User",
                "513-720-0058",
                Role.LIBRARIAN,
                MembershipStatus.ACTIVE
        );

        User user2 = new User(
                "john.doe@test.com",
                encodedPassword,
                "John",
                "Doe",
                "513-720-0059",
                Role.PATRON,
                MembershipStatus.ACTIVE
        );

        User user3 = new User(
                "jane.smith@test.com",
                encodedPassword,
                "Jane",
                "Smith",
                "513-720-0060",
                Role.PATRON,
                MembershipStatus.ACTIVE
        );

        User user4 = new User(
                "michael.brown@test.com",
                encodedPassword,
                "Michael",
                "Brown",
                "513-720-0061",
                Role.PATRON,
                MembershipStatus.ACTIVE
        );

        User user5 = new User(
                "sarah.jones@test.com",
                encodedPassword,
                "Sarah",
                "Jones",
                "513-720-0062",
                Role.PATRON,
                MembershipStatus.ACTIVE
        );

        userRepository.saveAll(List.of(user1, user2, user3, user4, user5));
    }
}
