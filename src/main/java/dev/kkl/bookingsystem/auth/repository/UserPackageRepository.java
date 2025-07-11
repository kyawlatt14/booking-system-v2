package dev.kkl.bookingsystem.auth.repository;

import dev.kkl.bookingsystem.auth.entity.UserPackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface UserPackageRepository extends JpaRepository<UserPackage, Long> {
    List<UserPackage> findByUserId(Long userId);
    List<UserPackage> findByExpiryDateBefore(LocalDateTime now);
}
