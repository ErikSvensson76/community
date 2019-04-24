package se.smelly.community.exception;

public class CouldNotBeFoundException extends RuntimeException{

    public CouldNotBeFoundException() {
        super();
    }

    public CouldNotBeFoundException(String message) {
        super(message);
    }
}
