package com.natsukashiiz.iicommon.common;

import java.util.Optional;

public enum ResponseCode implements BaseState<ResponseCode>{
    SUCCESS(0),
    INVALID_REQUEST(210),
    INVALID_EMAIL(211),
    INVALID_USERNAME(212),
    INVALID_PASSWORD(213),
    INVALID_NEW_PASSWORD(214),
    INVALID_CODE(215),
    INVALID_UID(216),
    INVALID_USERNAME_PASSWORD(217),
    PASSWORD_NOT_MATCH(218),
    INVALID_TITLE(219),
    INVALID_CONTENT(220),
    EXISTED_EMAIL(310),
    EXISTED_USERNAME(311),
    NOT_FOUND(410),
    NO_DATA(411),
    TOKEN_EXPIRE(510),
    REFRESH_TOKEN_EXPIRE(511),
    UNAUTHORIZED(888),
    UNKNOWN(999);

    private final Integer value;

    ResponseCode(final Integer code) {
        this.value = code;
    }

    @Override
    public Integer value() {
        return this.value;
    }

    @Override
    public Optional<ResponseCode> find(Integer code) {
        for (ResponseCode values : ResponseCode.values()) {
            if (values.value.equals(code))
                return Optional.of(values);
        }
        return Optional.empty();
    }
}

