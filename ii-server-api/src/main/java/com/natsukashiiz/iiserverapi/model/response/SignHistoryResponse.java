package com.natsukashiiz.iiserverapi.model.response;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class SignHistoryResponse {
    @NotNull
    private Long id;
    @NotNull
    private String ipv4;
    @NotNull
    private Integer device;
    @NotNull
    private LocalDateTime cdt;
}
