package dev.kkl.bookingsystem.pack.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import dev.kkl.bookingsystem.pack.entity.Package;

import java.util.List;

@Repository
public interface PackageRepository extends JpaRepository<Package, Long> {
    List<Package> findByCountry(String country);
    boolean existsByTitle(String lowerCase);
}
