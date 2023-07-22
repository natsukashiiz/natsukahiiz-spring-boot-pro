package com.natsukashiiz.iiserverapi.controller;

import com.natsukashiiz.iiboot.configuration.jwt.UserDetailsImpl;
import com.natsukashiiz.iicommon.model.Pagination;
import com.natsukashiiz.iicommon.model.Result;
import com.natsukashiiz.iiserverapi.model.request.ChangePasswordRequest;
import com.natsukashiiz.iiserverapi.model.request.UpdateUserRequest;
import com.natsukashiiz.iiserverapi.service.SignHistoryService;
import com.natsukashiiz.iiserverapi.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/v1/profile")
public class ProfileApi {
    @Resource
    private UserService userService;
    @Resource
    private SignHistoryService signHistoryService;

    @GetMapping
    public Result<?> getSelf(@AuthenticationPrincipal UserDetailsImpl auth) {
        return this.userService.getSelf(auth);
    }

    /**
     * {baseUrl}/signedHistory?page=1&limit=3
     */
    @GetMapping("/signHistory")
    public Result<?> signedHistory(@AuthenticationPrincipal UserDetailsImpl auth, Pagination pagination) {
        return this.signHistoryService.getAll(auth, pagination);
    }

    @PatchMapping
    public Result<?> update(@AuthenticationPrincipal UserDetailsImpl auth, @RequestBody UpdateUserRequest request) {
        return this.userService.update(auth, request);
    }

    @PatchMapping("/password")
    public Result<?> changePassword(@AuthenticationPrincipal UserDetailsImpl auth, @RequestBody ChangePasswordRequest request) {
        return this.userService.changePassword(auth, request);
    }
}
