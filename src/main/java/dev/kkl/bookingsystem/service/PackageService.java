package dev.kkl.bookingsystem.service;

import dev.kkl.bookingsystem.dto.PurchasePackageRequest;
import dev.kkl.bookingsystem.entity.User;
import dev.kkl.bookingsystem.entity.Package;
import dev.kkl.bookingsystem.entity.UserPackage;
import dev.kkl.bookingsystem.repository.PackageRepository;
import dev.kkl.bookingsystem.repository.UserPackageRepository;
import dev.kkl.bookingsystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PackageService {

    private final PackageRepository packageRepo;
    private final UserPackageRepository userPackageRepo;
    private final UserRepository userRepo;

    public List<Package> listByCountry(String country) {
        return packageRepo.findByCountry(country);
    }

    public void purchasePackage(String userEmail, PurchasePackageRequest request) {
        User user = userRepo.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Package pkg = packageRepo.findById(request.getPackageId())
                .orElseThrow(() -> new RuntimeException("Package not found"));

        // mock payment
        System.out.println("Charging $" + pkg.getPrice() + " to " + user.getEmail());

        UserPackage up = new UserPackage();
        up.setUser(user);
        up.setPkg(pkg);
        up.setRemainingCredits(pkg.getTotalCredits());
        up.setPurchaseDate(LocalDateTime.now());
        up.setExpiryDate(LocalDateTime.now().plusDays(pkg.getValidityDays()));

        userPackageRepo.save(up);
    }

    public List<UserPackage> getUserPackages(String userEmail) {
        User user = userRepo.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return userPackageRepo.findByUserId(user.getId());
    }
}
