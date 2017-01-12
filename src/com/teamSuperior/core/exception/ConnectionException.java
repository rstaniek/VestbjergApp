package com.teamSuperior.core.exception;

/**
 * Created by rajmu on 17.01.12.
 */
public class ConnectionException extends Exception {
    public ConnectionException(){
        super();
    }

    public ConnectionException(String message){
        super(message);
    }

    public ConnectionException(Throwable cause){
        super(cause);
    }

    public ConnectionException(String message, Throwable cause){
        super(message, cause);
    }

    public ConnectionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace){
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
