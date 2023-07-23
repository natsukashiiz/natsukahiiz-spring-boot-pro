package com.natsukashiiz.iiserverapi.model.request;

import lombok.Data;
import lombok.ToString;

/**
 * {
 *     "username": "xxxx",
 *     "password": "xxxx"
 * }
 */
@Data
@ToString(exclude = "password")
public class LoginRequest {
    private String username;
    private String password;
}
