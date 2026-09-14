package assembly.general.api.controllers;

import assembly.general.api.dto.*;
import assembly.general.api.service.ReservationService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/reservations")
@Validated
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

    @PostMapping("/{reservationId}/checkout")
    public ResponseEntity<CheckoutResponse> checkoutReservation(Authentication authentication, @PathVariable UUID reservationId, @Valid @RequestBody CheckoutRequest request){
        UUID userId = UUID.fromString(authentication.getName());

        CheckoutResponse response = reservationService.checkoutReservation(userId, reservationId, request);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/{reservationId}/return")
    public ResponseEntity<ReturnResponse> returnBook(Authentication authentication, @PathVariable UUID reservationId, @Valid @RequestBody ReturnRequest request){
        UUID userId = UUID.fromString(authentication.getName());

        ReturnResponse response = reservationService.returnBook(userId, reservationId, request);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/history")
    public ResponseEntity<ReservationHistoryResponse> getHistory(
            Authentication authentication,
            @RequestParam(defaultValue = "0") @Min(0) Integer page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) Integer size
    ){
        UUID userId = UUID.fromString(authentication.getName());

        ReservationHistoryResponse response = reservationService.getHistory(userId, page, size);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
