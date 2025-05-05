package dev.kkl.bookingsystem.booking.service;

import dev.kkl.bookingsystem.auth.entity.User;
import dev.kkl.bookingsystem.auth.entity.UserPackage;
import dev.kkl.bookingsystem.auth.repository.UserPackageRepository;
import dev.kkl.bookingsystem.auth.repository.UserRepository;
import dev.kkl.bookingsystem.base.common.service.RedisService;
import dev.kkl.bookingsystem.booking.entity.Booking;
import dev.kkl.bookingsystem.booking.entity.ClassSchedule;
import dev.kkl.bookingsystem.booking.entity.Waitlist;
import dev.kkl.bookingsystem.booking.repository.BookingRepository;
import dev.kkl.bookingsystem.booking.repository.ClassScheduleRepository;
import dev.kkl.bookingsystem.booking.repository.WaitlistRepository;
import dev.kkl.bookingsystem.base.common.AppResponse;
import dev.kkl.bookingsystem.booking.dto.ClassScheduleCreateRequest;
import dev.kkl.bookingsystem.base.exception.ApplicationErrorException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static dev.kkl.bookingsystem.base.common.Constant.*;

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
                .orElseThrow(() -> new RuntimeException(CLASS_SCHEDULE_NOT_FOUND));

        // Check if the user already booked this class
        if (bookingRepo.findByUserIdAndClassScheduleId(user.getId(), classScheduleId).isPresent()) {
            return AppResponse.fail(BOOKING_EXISTED);
        }

        // Check if class is fully booked
        if (classSchedule.getBookedCount() >= classSchedule.getCapacity()) {
            // Add to waitlist if full
            Waitlist waitlist = new Waitlist();
            waitlist.setUser(user);  // Use managed entity
            waitlist.setClassSchedule(classSchedule);
            waitlist.setAddedToWaitlistTime(LocalDateTime.now());
            waitlistRepository.save(waitlist);
            return AppResponse.fail(CLASS_IS_FULL);
        }

        // Find a valid package with credits
        UserPackage userPkg = userPackageRepo.findByUserId(user.getId()).stream()
                .filter(pkg -> pkg.getRemainingCredits() > 0 && pkg.getExpiryDate().isAfter(LocalDateTime.now()))
                .findFirst()
                .orElseThrow(() -> new ApplicationErrorException(PACKAGE_NO_ACTIVE));

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

        return AppResponse.success(BOOKING_SAVED, HttpStatus.ACCEPTED);
    }


    public AppResponse getUserBookings(String email) {
        User user = getUser(email);
        return AppResponse.success(USER_FETCHED,bookingRepo.findByUserIdOrderByClassDateAsc(user.getId()));
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

        return AppResponse.success( BOOKING_CANCEL,HttpStatus.ACCEPTED);
    }

    private User getUser(String email) {
        return userRepo.findByEmail(email)
                .orElseThrow(() -> new ApplicationErrorException(DATA_NOT_FOUND));
    }

    // Add method to check for overlapping bookings
    public boolean checkOverlap(Long classScheduleId, Long userId) {
        List<Booking> userBookings = bookingRepo.findByUserId(userId);
        ClassSchedule newClass = classScheduleRepository.findById(classScheduleId).orElseThrow(() -> new RuntimeException(DATA_NOT_FOUND));

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
        return AppResponse.success(CLASS_SAVED, HttpStatus.CREATED);

    }
}

