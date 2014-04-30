package edu.illinois.i3.htrc.tools;

public class InvalidPairtreePathException extends Exception {

    public InvalidPairtreePathException(String message) {
        super(message);
    }

    public InvalidPairtreePathException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidPairtreePathException(Throwable cause) {
        super(cause);
    }
}
