package dev.kkl.bookingsystem.service;

import dev.kkl.bookingsystem.dto.PackageDTO;
import dev.kkl.bookingsystem.dto.PurchasePackageRequest;
import dev.kkl.bookingsystem.entity.User;
import dev.kkl.bookingsystem.entity.Package;
import dev.kkl.bookingsystem.entity.UserPackage;
import dev.kkl.bookingsystem.exception.ApplicationErrorException;
import dev.kkl.bookingsystem.repository.PackageRepository;
import dev.kkl.bookingsystem.repository.UserPackageRepository;
import dev.kkl.bookingsystem.repository.UserRepository;
import dev.kkl.bookingsystem.repository.WaitlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PackageService {

    private final PackageRepository packageRepo;
    private final UserPackageRepository userPackageRepo;
    private final UserRepository userRepo;
    private final MockService mockService;
    private final WaitlistRepository waitlistRepository;

    public List<Package> listByCountry(String country) {
        return packageRepo.findByCountry(country);
    }

    public void purchasePackage(String userEmail, PurchasePackageRequest request) {
        User user = userRepo.findByEmail(userEmail)
                .orElseThrow(() -> new ApplicationErrorException("User not found"));
        Package pkg = packageRepo.findById(request.getPackageId())
                .orElseThrow(() -> new ApplicationErrorException("Package not found"));

        if(waitlistRepository.existsByUserId(user.getId()))
            throw new ApplicationErrorException("Your are in waitlist!");

        if(!Objects.equals(user.getCountry(),pkg.getCountry())){
            throw new ApplicationErrorException("User("+user.getCountry()+") can not buy this package("+pkg.getCountry()+")!");
        }

        // mock payment
        mockService.payment(pkg.getPrice(),user.getEmail());

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

    public void createPackage(PackageDTO packageDTO) {
        if(packageRepo.existsByTitle(packageDTO.getTitle().toLowerCase())){
            throw new ApplicationErrorException("Package is already existed!");
        }
        packageRepo.save(Package.builder()
                        .price(packageDTO.getPrice())
                        .title(packageDTO.getTitle().toLowerCase())
                        .country(packageDTO.getCountry().toLowerCase())
                        .description(packageDTO.getDescription())
                        .validityDays(packageDTO.getValidityDays())
                        .totalCredits(packageDTO.getTotalCredits())
                        .price(packageDTO.getPrice())
                .build());
    }

    public List<Package> getAll() {
        return packageRepo.findAll();
    }
}
