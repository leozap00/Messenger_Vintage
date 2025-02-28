package com.example.messenger_vintage.business;

public class UserNotFoundException extends BusinessException{

    public UserNotFoundException(){ super(); }

    public UserNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace){
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public UserNotFoundException(String message, Throwable cause){ super(message, cause); }

    public UserNotFoundException(String message){ super(message); }

    public UserNotFoundException(Throwable cause){ super(cause); }
}
