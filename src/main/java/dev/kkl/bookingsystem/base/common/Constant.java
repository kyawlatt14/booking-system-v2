package dev.kkl.bookingsystem.base.common;

public class Constant {

    private Constant(){
        throw new UnsupportedOperationException("This class is ulity class!");
    }

    public static final int SUCCESS_CODE = 1;
    public static final int FAILURE_CODE = 0;
    public static final String GET_ALL = "Fetched all data from system.";;

    /*user*/
    public static final String USER_EMAIL_EXISTED = "Email already exists.";
    public static final String USER_REGISTERED = "Registration successful! Please verify email.";
    public static final String AUTH_SUCCESS = "You are authenticated in system.";
    public static final String AUTH_FAIL = "Invalid credentials";
    public static final String USER_PASSWORD_NOT_MATCH = "Password not match.";
    public static final String USER_FETCHED = "User's data fetched in system.";
    public static final String DATA_NOT_FOUND = "Data not found in system.";
    public static final String USER_NOT_ALLOW = "User not allow to the booking because purchase is expired (Or) Your location not allow to buy the purchase.";
    public static final String PASSWORD_NOT_MATCH = "Your password and oldPassword from system is not match";
    public static final String USER_PASSWORD_UPDATED = "Your password updated in system.";;
    /*user*/
    /*purchase*/
    public static final String PURCHASE_SAVED = "Purchase successfully inserted in system.";
    public static final String PURCHASE_EXISTED = "Purchase existed in system.";
    public static final String PURCHASE_NOT_FOUND = "Purchase not found in system.";
    public static final String PURCHASE_UPDATED = "Purchase updated in system.";
    /*purchase*/
    /*booking*/
    public static final String BOOKING_SAVED = "Successfully booked the class.";
    public static final String BOOKING_CANCEL = "Successfully canceled the class. If waitlist users are available, they will be promoted.";
    public static final String BOOKING_WAIT = "Wait for booking.";
    public static final String BOOKING_EXISTED = "You have already booked this class.";
    public static final String REFUND_PAID = "Paid credits to customer.";
    public static final String REFUND_NO_PAID = "No paid credits to customer.";
    /*booking*/
    /*class*/
    public static final String CLASS_SAVED = "Class schedule created successfully.";
    public static final String CLASS_SCHEDULE_NOT_FOUND = "Class not found.";
    public static final String CLASS_IS_FULL="Class is full. You have been added to the waitlist.";
    public static final String PACKAGE_NO_ACTIVE = "No active package with credits";
    /*class*/
    public static final String URW = "Your are in waitlist!";
    public static final String PACKAGE_EXISTED = "Package is already existed!";
    public static final String PACKAGE_PURCHASE = "Package purchased!";
}
