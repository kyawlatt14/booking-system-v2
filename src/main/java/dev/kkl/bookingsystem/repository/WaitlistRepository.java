package dev.kkl.bookingsystem.repository;

import dev.kkl.bookingsystem.entity.Waitlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WaitlistRepository extends JpaRepository<Waitlist, Long> {
    List<Waitlist> findByClassScheduleId(Long classScheduleId);
    List<Waitlist> findByUserId(Long userId);
    Optional<Waitlist> findByUserIdAndClassScheduleId(Long userId, Long classScheduleId);
    boolean existsByUserId(Long id);
}


