package assembly.general.api.repository;

import assembly.general.api.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, UUID> {
    @Query("""
    SELECT COUNT(r)
    FROM Reservation r
    WHERE r.user.id = :userId
      AND r.status IN ('RESERVED', 'CHECKED_OUT')
    """)
    Long getUserActiveReservationsCount(@Param("userId") UUID userId);

    @Query("""
    SELECT r
    FROM Reservation r
    WHERE r.user.id = :userId
      AND r.status IN ('RESERVED', 'CHECKED_OUT')
    """)
    List<Reservation> getUserActiveReservations(@Param("userId") UUID userId);

    @Query("""
    SELECT COUNT(r)
    FROM Reservation r
    WHERE r.user.id = :userId
      AND r.status IN ('RETURNED')
    """)
    Long getUserBorrowingHistoryCount(@Param("userId") UUID userId);
}
