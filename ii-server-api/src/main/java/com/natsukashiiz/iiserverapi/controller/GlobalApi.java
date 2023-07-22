package com.natsukashiiz.iiserverapi.controller;

import com.natsukashiiz.iicommon.model.Result;
import com.natsukashiiz.iicommon.utils.ResponseUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/global")
public class GlobalApi {
    @GetMapping
    public Result<?> get() {
        return ResponseUtil.success();
    }
}
