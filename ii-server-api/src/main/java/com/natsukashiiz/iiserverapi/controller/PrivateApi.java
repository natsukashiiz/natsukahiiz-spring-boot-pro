package com.natsukashiiz.iiserverapi.controller;

import com.natsukashiiz.iiboot.configuration.jwt.AuthPrincipal;
import com.natsukashiiz.iicommon.model.Result;
import com.natsukashiiz.iicommon.utils.MapperUtils;
import com.natsukashiiz.iicommon.utils.ResultUtils;
import com.natsukashiiz.iiserverapi.entity.User;
import com.natsukashiiz.iiserverapi.model.response.UserResponse;
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
        UserResponse response = MapperUtils.mapOne(user, UserResponse.class);
        return ResultUtils.success(response);
    }
}
