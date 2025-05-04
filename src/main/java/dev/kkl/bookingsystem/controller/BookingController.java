package dev.kkl.bookingsystem.controller;

import dev.kkl.bookingsystem.dto.BookingRequest;
import dev.kkl.bookingsystem.entity.Booking;
import dev.kkl.bookingsystem.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping("/book")
    public ResponseEntity<?> book(@RequestBody BookingRequest request, Principal principal) {
        bookingService.bookClass(principal.getName(), request);
        return ResponseEntity.ok("Class booked!");
    }

    @GetMapping("/my")
    public List<Booking> myBookings(Principal principal) {
        return bookingService.getUserBookings(principal.getName());
    }
}

