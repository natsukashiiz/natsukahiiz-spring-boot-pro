package com.natsukashiiz.iiboot.configuration.jwt.model;

import lombok.Builder;
import lombok.Data;

/**
 * {
 *     access: "xxx",
 *     refresh: "xxx",
 *     refreshExpire: 123456789,
 *     accessExpire: 123456789
 * }
 */
@Data
@Builder
public class TokenResponse {
    private String access;
    private String refresh;
    private Long refreshExpire;
    private Long accessExpire;
}
