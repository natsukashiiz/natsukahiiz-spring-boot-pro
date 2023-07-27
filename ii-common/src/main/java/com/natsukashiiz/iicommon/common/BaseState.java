package com.natsukashiiz.iicommon.common;

import java.util.Optional;

public interface BaseState<E, T> {

    T value();

    Optional<E> find(T code);
}
