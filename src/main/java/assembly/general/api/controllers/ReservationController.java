package assembly.general.api.controllers;

import assembly.general.api.dto.CreateReservationRequest;
import assembly.general.api.dto.CreateReservationResponse;
import assembly.general.api.dto.ReservationsResponse;
import assembly.general.api.service.ReservationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@Controller
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService){
        this.reservationService = reservationService;
    }

    @GetMapping()
    public ResponseEntity<ReservationsResponse> getReservations(Authentication authentication){
        UUID userId = UUID.fromString(authentication.getName());

        ReservationsResponse response = reservationService.getReservations(userId);

        return  ResponseEntity.ok().body(response);
    }

    @PostMapping()
    public ResponseEntity<CreateReservationResponse> createReservation(Authentication authentication, @Valid @RequestBody CreateReservationRequest request){
        UUID userId = UUID.fromString(authentication.getName());

        CreateReservationResponse response = reservationService.createReservation(userId, request.getBookId());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
