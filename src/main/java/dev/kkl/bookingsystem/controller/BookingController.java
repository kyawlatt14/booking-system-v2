package dev.kkl.bookingsystem.controller;

import dev.kkl.bookingsystem.dto.AppResponse;
import dev.kkl.bookingsystem.dto.BookingRequest;
import dev.kkl.bookingsystem.dto.ClassScheduleCreateRequest;
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

    @PostMapping("/classSchedule/create")
    public ResponseEntity<AppResponse> createClassSchedule(@RequestBody ClassScheduleCreateRequest request) {
        AppResponse response = bookingService.createClassSchedule(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/book/{classScheduleId}")
    public ResponseEntity<AppResponse> bookClass(@PathVariable Long classScheduleId,
                                                 Principal principal) {
        AppResponse response = bookingService.bookClass(classScheduleId, principal.getName());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/cancel/{classScheduleId}")
    public ResponseEntity<AppResponse> cancelBooking(@PathVariable Long classScheduleId,
                                                     Principal principal) {
        AppResponse response = bookingService.cancelBooking(classScheduleId, principal.getName());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/my")
    public List<Booking> myBookings(Principal principal) {
        return bookingService.getUserBookings(principal.getName());
    }
}

