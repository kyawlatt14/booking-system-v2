package dev.kkl.bookingsystem.dto;

public class Constant {
    public static final int SUCCESS_CODE = 1;
    public static final int FAILURE_CODE = 0;
    public static final String GET_ALL = "Fetched all data from system.";;

    /*user*/
    public static final String USER_EXISTED = "User existed in system.";
    public static final String USER_REGISTERED = "User registered in system.";
    public static final String AUTH_SUCCESS = "You are authenticated in system.";
    public static final String USER_FETCHED = "User's data fetched in system.";
    public static final String USER_NOT_FOUND = "User not found in system.";
    public static final String USER_NOT_ALLOW = "User not allow to the booking because purchase is expired (Or) Your location not allow to buy the purchase.";


    public static final String PASSWORD_NOT_MATCH = "Your password and oldPassword from system is not match";
    public static final String USER_PASSWORD_UPDATED = "Your password updated in system.";;
    /*user*/
    /*purchase*/
    public static final String PURCHASE_SAVED = "Purchase inserted in system.";
    public static final String PURCHASE_EXISTED = "Purchase existed in system.";
    public static final String PURCHASE_NOT_FOUND = "Purchase not found in system.";
    public static final String PURCHASE_UPDATED = "Purchase updated in system.";
    /*purchase*/
    /*booking*/
    public static final String BOOKING_SAVE = "Booking successfully inserted in system.";
    public static final String BOOKING_WAIT = "Wait for booking.";
    public static final String BOOKING_EXISTED = "Booking existed in system";
    public static final String REFUND_PAID = "Paid credits to customer.";
    public static final String REFUND_NO_PAID = "No paid credits to customer.";
    /*booking*/
}
