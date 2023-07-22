package com.natsukashiiz.iicommon.common;

import java.util.Optional;

public enum DeviceCode implements BaseState<DeviceCode> {
    ANDROID(10),
    IPHONE(20),
    WINDOWS(30),
    MAC(40),
    LINUX(50),
    UNKNOWN(100)
    ;

    private final Integer value;

    DeviceCode(final Integer code) {
        this.value = code;
    }

    @Override
    public Integer value() {
        return this.value;
    }

    @Override
    public Optional<DeviceCode> find(Integer code) {
        for (DeviceCode values : DeviceCode.values()) {
            if (values.value.equals(code))
                return Optional.of(values);
        }
        return Optional.empty();
    }
}
