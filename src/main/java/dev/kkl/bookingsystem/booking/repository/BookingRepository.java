package dev.kkl.bookingsystem.booking.repository;

import dev.kkl.bookingsystem.booking.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUserIdOrderByClassDateAsc(Long userId);
    List<Booking> findByClassScheduleId(Long classScheduleId);
    List<Booking> findByUserId(Long userId);
    Optional<Booking> findByUserIdAndClassScheduleId(Long userId, Long classScheduleId);
}


