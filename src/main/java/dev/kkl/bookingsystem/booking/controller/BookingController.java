package dev.kkl.bookingsystem.booking.controller;

import dev.kkl.bookingsystem.base.common.AppResponse;
import dev.kkl.bookingsystem.booking.dto.ClassScheduleCreateRequest;
import dev.kkl.bookingsystem.booking.service.BookingService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/bookings")
@Tag(name = "Booking-Controller")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping("/classSchedule/create")
    public ResponseEntity<AppResponse> createClassSchedule(@RequestBody ClassScheduleCreateRequest request) {
        return ResponseEntity.ok(bookingService.createClassSchedule(request));
    }

    @PostMapping("/book/{classScheduleId}")
    public ResponseEntity<AppResponse> bookClass(@PathVariable Long classScheduleId,
                                                 Principal principal) {
        return ResponseEntity.ok(bookingService.bookClass(classScheduleId, principal.getName()));
    }

    @PostMapping("/cancel/{classScheduleId}")
    public ResponseEntity<AppResponse> cancelBooking(@PathVariable Long classScheduleId,
                                                     Principal principal) {
        return ResponseEntity.ok(bookingService.cancelBooking(classScheduleId, principal.getName()));
    }

    @GetMapping("/my")
    public ResponseEntity<AppResponse> myBookings(Principal principal) {
        return ResponseEntity.ok(bookingService.getUserBookings(principal.getName()));
    }
}

