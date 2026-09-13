package assembly.general.api.service;

import assembly.general.api.dto.ProfileDto;
import assembly.general.api.entity.User;
import assembly.general.api.exception.ProfileNotFoundException;
import assembly.general.api.repository.ReservationRepository;
import assembly.general.api.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class ProfileService {

    private final UserRepository userRepository;
    private final ReservationRepository reservationRepository;

    public ProfileService(UserRepository userRepository, ReservationRepository reservationRepository){
        this.userRepository = userRepository;
        this.reservationRepository = reservationRepository;
    }

    public ProfileDto getProfile(UUID userId){
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new ProfileNotFoundException(userId);
        }
        User user = userOpt.get();

        Integer activeReservations = reservationRepository.getUserActiveReservationsCount(user.getId()).intValue();
        Integer borrowingHistory = reservationRepository.getUserBorrowingHistoryCount(user.getId()).intValue();

        ProfileDto profile = new ProfileDto(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getPhoneNumber(),
                user.getRole(),
                user.getMembershipStatus(),
                activeReservations,
                borrowingHistory
        );

        return profile;
    }
}
