package com.natsukashiiz.iicommon.utils;

import com.natsukashiiz.iicommon.common.ResponseCode;
import com.natsukashiiz.iicommon.model.PaginationResponse;
import com.natsukashiiz.iicommon.model.Result;
import org.springframework.data.domain.Page;

import java.util.Collections;
import java.util.List;

public class ResponseUtil {

    public static <E> Result<?> success() {
        return success(null);
    }

    public static <E> Result<?> success(E result) {
        ResponseCode code = ResponseCode.SUCCESS;
        return Result.builder()
                .code(code.value())
                .text(code)
                .result(result)
                .build();
    }

    public static Result<?> successEmpty() {
        ResponseCode code = ResponseCode.SUCCESS;
        return Result.builder()
                .code(code.value())
                .text(code)
                .result(Collections.EMPTY_LIST)
                .records(0L)
                .build();
    }

    public static <T> Result<?> successList(Page<T> result) {
        PaginationResponse pagination = PaginationResponse.builder()
                .limit(result.getPageable().getPageSize())
                .current(result.getPageable().getPageNumber() + 1)
                .records((int) result.getTotalElements())
                .pages(result.getTotalPages())
                .first(result.isFirst())
                .last(result.isLast())
                .build();
        ResponseCode code = ResponseCode.SUCCESS;
        return Result.builder()
                .code(code.value())
                .result(result.getContent())
                .text(code)
                .pagination(pagination)
                .build();
    }

    public static <T> Result<?> successList(List<T> result, Long records) {
        ResponseCode code = ResponseCode.SUCCESS;
        return Result.builder()
                .code(code.value())
                .result(result)
                .text(code)
                .records(records)
                .build();
    }

    public static Result<?> error(ResponseCode code) {
        return Result.builder()
                .code(code.value())
                .text(code)
                .build();
    }

    public static Result<?> unauthorized() {
        ResponseCode code = ResponseCode.UNAUTHORIZED;
        return Result.builder()
                .code(code.value())
                .text(code)
                .build();
    }

    public static Result<?> unknown() {
        ResponseCode code = ResponseCode.UNKNOWN;
        return Result.builder()
                .code(code.value())
                .text(code)
                .build();
    }
}
