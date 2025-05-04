package dev.kkl.bookingsystem.service;

import dev.kkl.bookingsystem.dto.BookingRequest;
import dev.kkl.bookingsystem.entity.Booking;
import dev.kkl.bookingsystem.entity.User;
import dev.kkl.bookingsystem.entity.UserPackage;
import dev.kkl.bookingsystem.exception.ApplicationErrorException;
import dev.kkl.bookingsystem.repository.BookingRepository;
import dev.kkl.bookingsystem.repository.UserPackageRepository;
import dev.kkl.bookingsystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final UserRepository userRepo;
    private final BookingRepository bookingRepo;
    private final UserPackageRepository userPackageRepo;

    public void bookClass(String email, BookingRequest request) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new ApplicationErrorException("User not found"));

        // Find a valid package with credits
        UserPackage userPkg = userPackageRepo.findByUserId(user.getId()).stream()
                .filter(pkg -> pkg.getRemainingCredits() > 0 && pkg.getExpiryDate().isAfter(LocalDateTime.now()))
                .findFirst()
                .orElseThrow(() -> new ApplicationErrorException("No active package with credits"));

        // Deduct credit
        userPkg.setRemainingCredits(userPkg.getRemainingCredits() - 1);
        userPackageRepo.save(userPkg);

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setClassName(request.getClassName());
        booking.setClassDate(request.getClassDate());
        booking.setBookedAt(LocalDateTime.now());

        bookingRepo.save(booking);
    }

    public List<Booking> getUserBookings(String email) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new ApplicationErrorException("User not found"));
        return bookingRepo.findByUserIdOrderByClassDateAsc(user.getId());
    }
}

