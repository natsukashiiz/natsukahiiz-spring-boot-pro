package com.natsukashiiz.iiserverapi.model.request;

import lombok.Data;
import lombok.ToString;

/**
 * {
 *     "email: "xxxx",
 *     "username: "xxxx",
 *     "password: "xxxx"
 * }
 */
@Data
@ToString(exclude = "password")
public class RegisterRequest {
    private String email;
    private String username;
    private String password;
}
