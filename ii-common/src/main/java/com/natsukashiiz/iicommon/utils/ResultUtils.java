package com.natsukashiiz.iicommon.utils;

import com.natsukashiiz.iicommon.common.ResponseCode;
import com.natsukashiiz.iicommon.model.Result;
import java.util.Collections;
import java.util.List;

public class ResultUtils {

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
        return ResultUtils.successList(Collections.emptyList(), 0L);
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
