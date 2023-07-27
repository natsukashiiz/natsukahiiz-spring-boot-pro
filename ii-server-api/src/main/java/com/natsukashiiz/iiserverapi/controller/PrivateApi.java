package com.natsukashiiz.iiserverapi.controller;

import com.natsukashiiz.iiboot.configuration.jwt.AuthPrincipal;
import com.natsukashiiz.iicommon.model.Result;
import com.natsukashiiz.iicommon.utils.ResponseUtil;
import com.natsukashiiz.iiserverapi.entity.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/private")
public class PrivateApi {
    @GetMapping
    public Result<?> get(@AuthenticationPrincipal AuthPrincipal auth) {
        User user = User.from(auth);
        return ResponseUtil.success(User.toResponse(user));
    }
}
