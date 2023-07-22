package com.natsukashiiz.iicommon.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.natsukashiiz.iicommon.common.ResponseCode;
import lombok.Builder;
import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Result<E> {
    private Integer code;
    @Enumerated(value = EnumType.STRING)
    private ResponseCode text;
    private E result;
    private Long records;
    private PaginationResponse pagination;
}