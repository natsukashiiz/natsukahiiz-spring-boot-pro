package com.natsukashiiz.iiserverapi.exception;

public class UserException extends BaseException {
    private static final String PREFIX = "user.";
    private static final String INVALID = "invalid.";
    public UserException(String code) {
        super(PREFIX.concat(code));
    }

    public String invalid(String code) {
        return INVALID.concat(code);
    }

    public UserException invalidEmail() {
        return new UserException(this.invalid("email"));
    }
}
