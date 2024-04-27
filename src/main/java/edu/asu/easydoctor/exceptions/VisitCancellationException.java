package edu.asu.easydoctor.exceptions;

public class VisitCancellationException extends Exception {
    
    public VisitCancellationException() {
        super("Visit cannot be cancelled");
    }
}
