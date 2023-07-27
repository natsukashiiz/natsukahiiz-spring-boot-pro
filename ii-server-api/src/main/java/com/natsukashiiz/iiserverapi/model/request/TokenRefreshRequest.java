package com.natsukashiiz.iiserverapi.model.request;

import lombok.Data;

/**
 * {
 *     "token": "xxxx"
 * }
 */
@Data
public class TokenRefreshRequest {
    private String token;
}
