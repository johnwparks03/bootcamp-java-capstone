package assembly.general.api.service;

import assembly.general.api.dto.AuthResponse;
import assembly.general.api.dto.LoginRequest;
import assembly.general.api.dto.RegisterRequest;
import assembly.general.api.dto.UserDto;
import assembly.general.api.entity.MembershipStatus;
import assembly.general.api.entity.User;
import assembly.general.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public AuthResponse register(RegisterRequest request){
        try{
            if(userRepository.existsByEmail(request.getEmail())) {
                return AuthResponse.failure("User registration failed. If a user exists for this email, we'll send instructions to sign in or reset your password.");
            }

            String hashedPassword = passwordEncoder.encode(request.getPassword());

            User user = new User(
                    request.getEmail(),
                    hashedPassword,
                    request.getFirstName(),
                    request.getLastName(),
                    request.getPhoneNumber(),
                    request.getRole(),
                    MembershipStatus.ACTIVE
            );

            user = userRepository.save(user);

            String token = jwtService.generateToken(user);

            UserDto userDto = convertUserToUserDto(user);

            return AuthResponse.success("User registered successfully", userDto, token);
        } catch (Exception e){
            return AuthResponse.failure("User registration failed: " + e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request){
        try{
            Optional<User> userOpt = findUserByEmail(request.getEmail());

            if(userOpt.isEmpty()){
                return AuthResponse.failure("Incorrect email or password");
            }

            User user = userOpt.get();

            if (!passwordEncoder.matches(request.getPassword(), user.getPassword())){
                return AuthResponse.failure("Incorrect email or password");
            }

            String token = jwtService.generateToken(user);

            UserDto userDto = convertUserToUserDto(user);

            return AuthResponse.success("Login successful", userDto, token);
        } catch (Exception e) {
            return AuthResponse.failure("Login failed: " + e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public Optional<UserDto> getUserById(UUID userId){
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            return Optional.empty();
        }
        User user = userOpt.get();
        UserDto userDto = convertUserToUserDto(user);
        return Optional.of(userDto);
    }

    private Optional<User> findUserByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user != null){
            return Optional.of(user);
        }

        return Optional.empty();
    }

    private UserDto convertUserToUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getPhoneNumber(),
                user.getMembershipStatus()
        );
    }
}
