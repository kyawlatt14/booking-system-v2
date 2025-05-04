package dev.kkl.bookingsystem.repository;

import dev.kkl.bookingsystem.entity.ClassSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClassScheduleRepository extends JpaRepository<ClassSchedule, Long> {
    List<ClassSchedule> findByCountry(String country);
    Optional<ClassSchedule> findById(Long id);
}

