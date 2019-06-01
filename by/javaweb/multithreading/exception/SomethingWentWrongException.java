package by.javaweb.multithreading.exception;

public class SomethingWentWrongException extends Exception {

    public SomethingWentWrongException() {

    }

    public SomethingWentWrongException(String message, Throwable cause) {
        super(message, cause);
    }

    public SomethingWentWrongException(Throwable cause) {
        super(cause);
    }

    public SomethingWentWrongException(String message) {
        super(message);
    }

}
