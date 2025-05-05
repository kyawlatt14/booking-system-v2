package dev.kkl.bookingsystem.base.common.service;

import dev.kkl.bookingsystem.base.exception.ApplicationErrorException;
import org.springframework.stereotype.Component;

@Component
public class MockService {

    public void verificationForEmail(String email) {
        if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new ApplicationErrorException("Invalid email address");
        }
    }

    public void payment(double price, String email) {
        if (price <= 0) {
            throw new ApplicationErrorException(email+" --> Invalid price. Cannot process payment.");
        }
        // Simulate payment processing
        System.out.printf("Charging $%.2f to %s%n", price, email);
        System.out.println("Payment processed successfully (mock).");
    }
}
