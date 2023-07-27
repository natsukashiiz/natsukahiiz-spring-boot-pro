package com.natsukashiiz.iiserverapi.model.response;

import lombok.Data;

/**
 * {
 *     "id": xxxx,
 *     "email": "xxxx",
 *     "username": "xxxx"
 * }
 */
@Data
public class UserResponse {
    private Long id;
    private String email;
    private String username;
}
