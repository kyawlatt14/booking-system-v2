package dev.kkl.bookingsystem.service;

import dev.kkl.bookingsystem.dto.AppResponse;
import dev.kkl.bookingsystem.dto.BookingRequest;
import dev.kkl.bookingsystem.dto.ClassScheduleCreateRequest;
import dev.kkl.bookingsystem.entity.*;
import dev.kkl.bookingsystem.exception.ApplicationErrorException;
import dev.kkl.bookingsystem.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final UserRepository userRepo;
    private final BookingRepository bookingRepo;
    private final UserPackageRepository userPackageRepo;
    private final ClassScheduleRepository classScheduleRepository;
    private final WaitlistRepository waitlistRepository;
    private final RedisService redisService;  // Service for Redis caching (Concurrency Control)

    @Transactional
    public AppResponse bookClass(Long classScheduleId, String email) {
        User user = getUser(email);

        ClassSchedule classSchedule = classScheduleRepository.findById(classScheduleId)
                .orElseThrow(() -> new RuntimeException("Class not found"));

        // Check if the user already booked this class
        if (bookingRepo.findByUserIdAndClassScheduleId(user.getId(), classScheduleId).isPresent()) {
            return AppResponse.fail("You have already booked this class.");
        }

        // Check if class is fully booked
        if (classSchedule.getBookedCount() >= classSchedule.getCapacity()) {
            // Add to waitlist if full
            Waitlist waitlist = new Waitlist();
            waitlist.setUser(user);  // Use managed entity
            waitlist.setClassSchedule(classSchedule);
            waitlist.setAddedToWaitlistTime(LocalDateTime.now());
            waitlistRepository.save(waitlist);
            return AppResponse.fail("Class is full. You have been added to the waitlist.");
        }

        // Find a valid package with credits
        UserPackage userPkg = userPackageRepo.findByUserId(user.getId()).stream()
                .filter(pkg -> pkg.getRemainingCredits() > 0 && pkg.getExpiryDate().isAfter(LocalDateTime.now()))
                .findFirst()
                .orElseThrow(() -> new ApplicationErrorException("No active package with credits"));

        // Deduct credit
        userPkg.setRemainingCredits(userPkg.getRemainingCredits() - 1);
        userPackageRepo.save(userPkg);

        // Book the class if not full
        Booking booking = new Booking();
        booking.setUser(user);  // Use managed entity
        booking.setClassSchedule(classSchedule);
        booking.setBookedTime(LocalDateTime.now());
        booking.setStatus("BOOKED");

        classSchedule.setBookedCount(classSchedule.getBookedCount() + 1);  // Increase booked count

        bookingRepo.save(booking);
        classScheduleRepository.save(classSchedule);

        return AppResponse.success("Successfully booked the class.", null);
    }


    public List<Booking> getUserBookings(String email) {
        User user = getUser(email);
        return bookingRepo.findByUserIdOrderByClassDateAsc(user.getId());
    }

    @Transactional
    public AppResponse cancelBooking(Long classScheduleId, String email) {

        User user = getUser(email);

        Booking booking = bookingRepo.findByUserIdAndClassScheduleId(user.getId(), classScheduleId).orElseThrow(() -> new RuntimeException("Booking not found"));

        // Set booking status to CANCELLED
        booking.setStatus("CANCELED");
        bookingRepo.save(booking);

        // Decrease booked count
        ClassSchedule classSchedule = booking.getClassSchedule();
        classSchedule.setBookedCount(classSchedule.getBookedCount() - 1);
        classScheduleRepository.save(classSchedule);

        // If there is a waitlist, promote FIFO
        List<Waitlist> waitlist = waitlistRepository.findByClassScheduleId(classScheduleId);
        if (!waitlist.isEmpty()) {
            Waitlist waitlistUser = waitlist.get(0);  // FIFO promotion
            waitlistRepository.delete(waitlistUser);

            // Book the class for the waitlisted user
            Booking newBooking = new Booking();
            newBooking.setUser(waitlistUser.getUser());
            newBooking.setClassSchedule(classSchedule);
            newBooking.setBookedTime(LocalDateTime.now());
            newBooking.setStatus("BOOKED");
            bookingRepo.save(newBooking);
        }

        return AppResponse.success( "Successfully canceled the class. If waitlist users are available, they will be promoted.",null);
    }

    private User getUser(String email) {
        return userRepo.findByEmail(email)
                .orElseThrow(() -> new ApplicationErrorException("User not found"));
    }

    // Add method to check for overlapping bookings
    public boolean checkOverlap(Long classScheduleId, Long userId) {
        List<Booking> userBookings = bookingRepo.findByUserId(userId);
        ClassSchedule newClass = classScheduleRepository.findById(classScheduleId).orElseThrow(() -> new RuntimeException("Class not found"));

        for (Booking booking : userBookings) {
            ClassSchedule bookedClass = booking.getClassSchedule();
            if (newClass.getStartTime().isBefore(bookedClass.getEndTime()) && newClass.getEndTime().isAfter(bookedClass.getStartTime())) {
                return true;  // Overlap detected
            }
        }
        return false;  // No overlap
    }

    public AppResponse createClassSchedule(ClassScheduleCreateRequest request) {

        ClassSchedule classSchedule = new ClassSchedule();
        classSchedule.setClassName(request.getClassName());
        classSchedule.setCountry(request.getCountry());
        classSchedule.setStartTime(request.getStartTime());
        classSchedule.setEndTime(request.getEndTime());
        classSchedule.setCapacity(request.getCapacity());
        classSchedule.setBookedCount(0); // default 0

        classScheduleRepository.save(classSchedule);
        return AppResponse.success("Class schedule created successfully.",null);

    }
}

