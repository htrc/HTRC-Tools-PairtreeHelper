package org.hathitrust.htrc.tools.pairtreehelper;

public class InvalidHtrcIdException extends Exception {
    public InvalidHtrcIdException() {
    }

    public InvalidHtrcIdException(String message) {
        super(message);
    }

    public InvalidHtrcIdException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidHtrcIdException(Throwable cause) {
        super(cause);
    }
}
