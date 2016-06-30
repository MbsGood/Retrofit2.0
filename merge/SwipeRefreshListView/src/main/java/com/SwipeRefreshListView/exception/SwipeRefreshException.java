package com.SwipeRefreshListView.exception;

/**
 * SwipeRefreshListView
 *          Exception
 */
public class SwipeRefreshException extends RuntimeException {

    private static String msg = "SwipeRefreshException";

    public SwipeRefreshException(String detailMessage) {
        super(detailMessage);
        msg = detailMessage;
    }

    @Override
    public String getMessage() {
        return msg;
    }
}
