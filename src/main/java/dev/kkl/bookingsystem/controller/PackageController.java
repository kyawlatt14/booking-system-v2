package dev.kkl.bookingsystem.controller;

import dev.kkl.bookingsystem.dto.PurchasePackageRequest;
import dev.kkl.bookingsystem.entity.UserPackage;
import dev.kkl.bookingsystem.service.PackageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import dev.kkl.bookingsystem.entity.Package;


import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/packages")
@RequiredArgsConstructor
public class PackageController {

    private final PackageService packageService;

    @GetMapping("/country/{country}")
    public List<Package> getByCountry(@PathVariable String country) {
        return packageService.listByCountry(country);
    }

    @PostMapping("/purchase")
    public ResponseEntity<?> purchase(@RequestBody PurchasePackageRequest request,
                                      Principal principal) {
        packageService.purchasePackage(principal.getName(), request);
        return ResponseEntity.ok("Package purchased!");
    }

    @GetMapping("/my")
    public List<UserPackage> myPackages(Principal principal) {
        return packageService.getUserPackages(principal.getName());
    }
}

