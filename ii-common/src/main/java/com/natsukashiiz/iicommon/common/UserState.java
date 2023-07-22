package com.natsukashiiz.iicommon.common;

import java.util.Optional;

public enum UserState implements BaseState<UserState> {
    NORMAL(10),
    DISABLED(20),
    DESTROYED(30)
    ;
    private final Integer value;

    UserState(final Integer code) {
        this.value = code;
    }

    @Override
    public Integer value() {
        return this.value;
    }

    @Override
    public Optional<UserState> find(Integer code) {
        for (UserState values : UserState.values()) {
            if (values.value.equals(code))
                return Optional.of(values);
        }
        return Optional.empty();
    }
}
