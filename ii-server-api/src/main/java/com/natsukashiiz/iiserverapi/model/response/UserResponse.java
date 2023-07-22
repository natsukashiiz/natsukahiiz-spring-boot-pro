package com.natsukashiiz.iiserverapi.model.response;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * {
 *     "id": xxxx,
 *     "email": "xxxx",
 *     "username": "xxxx"
 * }
 */
@Data
public class UserResponse {
    @NotNull
    private Long id;
    @NotNull
    private String email;
    @NotNull
    private String username;
    @NotNull
    private LocalDateTime cdt;
}
