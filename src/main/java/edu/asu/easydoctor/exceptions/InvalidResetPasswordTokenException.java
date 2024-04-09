package edu.asu.easydoctor.exceptions;

public class InvalidResetPasswordTokenException extends Exception {
    
    public InvalidResetPasswordTokenException() {
        super("Invalid reset password token");
    }
}
