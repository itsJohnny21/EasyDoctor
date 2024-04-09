package edu.asu.easydoctor.exceptions;

public class ExpiredResetPasswordTokenException extends Exception {
    
    public ExpiredResetPasswordTokenException() {
        super("Expired reset password token");
    }
}
