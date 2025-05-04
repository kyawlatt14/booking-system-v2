package dev.kkl.bookingsystem.repository;

import dev.kkl.bookingsystem.entity.UserPackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface UserPackageRepository extends JpaRepository<UserPackage, Long> {
    List<UserPackage> findByUserId(Long userId);
    List<UserPackage> findByExpiryDateBefore(LocalDate now);
}
