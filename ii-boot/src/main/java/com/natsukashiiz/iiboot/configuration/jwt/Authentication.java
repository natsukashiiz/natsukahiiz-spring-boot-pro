package com.natsukashiiz.iiboot.configuration.jwt;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Authentication {
    private Long uid;
    private String name;
    private String email;
    @JsonIgnore
    private String password;
}
