package com.natsukashiiz.iicommon.model;

import com.natsukashiiz.iicommon.common.ResponseCode;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import lombok.Builder;
import lombok.Data;

/**
 * {
 *    "code": 0,
 *    "text": "SUCCESS",
 *    "result": null
 *    "records": 0
 * }
 */
@Data
@Builder
public class Result<E> {
    private Integer code;
    @Enumerated(value = EnumType.STRING)
    private ResponseCode text;
    private E result;
    private Long records;
}
