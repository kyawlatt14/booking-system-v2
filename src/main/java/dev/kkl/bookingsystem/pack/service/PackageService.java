package dev.kkl.bookingsystem.pack.service;

import dev.kkl.bookingsystem.base.common.AppResponse;
import dev.kkl.bookingsystem.base.common.service.MockService;
import dev.kkl.bookingsystem.pack.dto.PackageDTO;
import dev.kkl.bookingsystem.pack.dto.PurchasePackageRequest;
import dev.kkl.bookingsystem.auth.entity.User;
import dev.kkl.bookingsystem.pack.entity.Package;
import dev.kkl.bookingsystem.auth.entity.UserPackage;
import dev.kkl.bookingsystem.base.exception.ApplicationErrorException;
import dev.kkl.bookingsystem.pack.repository.PackageRepository;
import dev.kkl.bookingsystem.auth.repository.UserPackageRepository;
import dev.kkl.bookingsystem.auth.repository.UserRepository;
import dev.kkl.bookingsystem.booking.repository.WaitlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

import static dev.kkl.bookingsystem.base.common.Constant.*;

@Service
@RequiredArgsConstructor
public class PackageService {

    private final PackageRepository packageRepo;
    private final UserPackageRepository userPackageRepo;
    private final UserRepository userRepo;
    private final MockService mockService;
    private final WaitlistRepository waitlistRepository;

    public AppResponse listByCountry(String country) {
        return AppResponse.success(GET_ALL,packageRepo.findByCountry(country));
    }

    public AppResponse purchasePackage(String userEmail, PurchasePackageRequest request) {
        User user = userRepo.findByEmail(userEmail)
                .orElseThrow(() -> new ApplicationErrorException(DATA_NOT_FOUND));
        Package pkg = packageRepo.findById(request.getPackageId())
                .orElseThrow(() -> new ApplicationErrorException(DATA_NOT_FOUND));

        if(waitlistRepository.existsByUserId(user.getId()))
            throw new ApplicationErrorException(URW);

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
        return AppResponse.success(PACKAGE_PURCHASE,HttpStatus.CREATED);
    }

    public AppResponse getUserPackages(String userEmail) {
        User user = userRepo.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException(DATA_NOT_FOUND));
        return AppResponse.success(USER_FETCHED,userPackageRepo.findByUserId(user.getId()));
    }

    public AppResponse createPackage(PackageDTO packageDTO) {
        if(packageRepo.existsByTitle(packageDTO.getTitle().toLowerCase())){
            throw new ApplicationErrorException(PACKAGE_EXISTED);
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

        return AppResponse.success(PURCHASE_SAVED, HttpStatus.CREATED);
    }

    public AppResponse getAll() {
        return AppResponse.success(GET_ALL,packageRepo.findAll());
    }
}
