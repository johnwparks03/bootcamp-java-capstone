package assembly.general.api.seed;

import assembly.general.api.entity.MembershipStatus;
import assembly.general.api.entity.Role;
import assembly.general.api.entity.User;
import assembly.general.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class UserDataSeeder implements CommandLineRunner {
    private final UserRepository userRepository;

    @Autowired
    public UserDataSeeder(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception{
        userRepository.deleteAll();

        User user1 = new User("test@test.com", "password", "first", "last", "513-720-0058",Role.LIBRARIAN, MembershipStatus.ACTIVE);

        userRepository.save(user1);
    }
}
