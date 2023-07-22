package com.natsukashiiz.iiboot.configuration.jwt;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Authentication {
    private Long uid;
    private String name;
    private String email;
    private String password;
}
