package com.natsukashiiz.iiserverapi.model.response;

import java.time.LocalDateTime;
import lombok.Data;

/**
 * {
 *     "id": 1,
 *     "ipv4": "xxx",
 *     "device": 10,
 *     "cdt": "2021-01-01T00:00:00.000Z"
 * }
 */
@Data
public class SignHistoryResponse {

    private Long id;
    private String ipv4;
    private Integer device;
    private LocalDateTime cdt;
}
