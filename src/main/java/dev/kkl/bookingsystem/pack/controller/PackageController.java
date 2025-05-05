package dev.kkl.bookingsystem.pack.controller;

import dev.kkl.bookingsystem.base.common.AppResponse;
import dev.kkl.bookingsystem.pack.dto.PackageDTO;
import dev.kkl.bookingsystem.pack.dto.PurchasePackageRequest;
import dev.kkl.bookingsystem.pack.service.PackageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.security.Principal;

@RestController
@RequestMapping("/api/packages")
@Tag(name = "Package-Controller")
@RequiredArgsConstructor
public class PackageController {

    private final PackageService packageService;

    @GetMapping
    public ResponseEntity<AppResponse> getAll() {

        return ResponseEntity.ok(packageService.getAll());
    }

    @PostMapping("/create")
    public ResponseEntity<AppResponse> createPackage(@RequestBody PackageDTO packageDTO){
        return ResponseEntity.ok(packageService.createPackage(packageDTO));
    }

    @GetMapping("/country/{country}")
    public ResponseEntity<AppResponse> getByCountry(@PathVariable String country) {
        return ResponseEntity.ok(packageService.listByCountry(country));
    }

    @PostMapping("/purchase")
    public ResponseEntity<AppResponse> purchase(@RequestBody PurchasePackageRequest request,
                                      Principal principal) {
        return ResponseEntity.ok(packageService.purchasePackage(principal.getName(), request));
    }

    @GetMapping("/my")
    public ResponseEntity<AppResponse> myPackages(Principal principal) {
        return ResponseEntity.ok(packageService.getUserPackages(principal.getName()));
    }
}

