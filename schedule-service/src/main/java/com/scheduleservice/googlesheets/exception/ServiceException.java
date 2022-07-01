package com.scheduleservice.googlesheets.exception;

/**
 * @author :keisho.
 */
public class ServiceException extends RuntimeException {

    private static final long serialVersionUID = -6369682426625266903L;

    public ServiceException() {
        super();
    }

    public ServiceException(String e) {
        super(e);
    }

}
