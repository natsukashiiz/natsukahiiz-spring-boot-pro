package com.natsukashiiz.iiserverapi.controller;

import com.natsukashiiz.iiboot.configuration.jwt.UserDetailsImpl;
import com.natsukashiiz.iicommon.model.Result;
import com.natsukashiiz.iicommon.utils.ResponseUtil;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/private")
public class PrivateApi {
    @GetMapping
    public Result<?> get(@AuthenticationPrincipal UserDetailsImpl auth) {
        return ResponseUtil.success(auth);
    }
}