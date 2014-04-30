package edu.illinois.i3.htrc.tools;

import java.io.IOException;

public class InvalidPairtreePathException extends IOException {

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
