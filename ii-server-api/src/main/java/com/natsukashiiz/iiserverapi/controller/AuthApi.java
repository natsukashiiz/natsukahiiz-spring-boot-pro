package com.natsukashiiz.iiserverapi.controller;

import com.natsukashiiz.iicommon.model.Result;
import com.natsukashiiz.iiserverapi.model.request.LoginRequest;
import com.natsukashiiz.iiserverapi.model.request.RegisterRequest;
import com.natsukashiiz.iiserverapi.model.request.TokenRefreshRequest;
import com.natsukashiiz.iiserverapi.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/v1/auth")
public class AuthApi {
    @Resource
    private UserService userService;

    @PostMapping("/signUp")
    public Result<?> signup(@RequestBody RegisterRequest request) {
        return this.userService.create(request);
    }

    @PostMapping("/signIn")
    public Result<?> login(HttpServletRequest httpRequest, @RequestBody LoginRequest request) {
        return this.userService.login(request, httpRequest);
    }

    @PostMapping("/refresh")
    public Result<?> refresh(@RequestBody TokenRefreshRequest request) {
        return this.userService.refreshToken(request);
    }

}
