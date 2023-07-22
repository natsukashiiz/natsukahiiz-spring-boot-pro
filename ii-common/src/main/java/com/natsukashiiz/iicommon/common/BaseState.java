package com.natsukashiiz.iicommon.common;

import java.util.Optional;

public interface BaseState<E> {
    Integer value();
    Optional<E> find(Integer code);
}
