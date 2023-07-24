package com.natsukashiiz.iiserverapi.model.request;

import lombok.Data;

/**
 * {
 *     "email": "xxxx"
 * }
 */
@Data
public class UpdUserRequest {
    private String email;
}
