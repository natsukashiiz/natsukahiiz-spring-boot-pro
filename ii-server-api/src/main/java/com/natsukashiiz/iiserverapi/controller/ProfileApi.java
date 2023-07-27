package com.natsukashiiz.iiserverapi.controller;

import com.natsukashiiz.iiboot.configuration.jwt.AuthPrincipal;
import com.natsukashiiz.iicommon.model.Pagination;
import com.natsukashiiz.iicommon.model.Result;
import com.natsukashiiz.iiserverapi.model.request.ChangePasswordRequest;
import com.natsukashiiz.iiserverapi.model.request.UpdUserRequest;
import com.natsukashiiz.iiserverapi.service.SignHistoryService;
import com.natsukashiiz.iiserverapi.service.UserService;
import javax.annotation.Resource;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/profile")
public class ProfileApi {

  @Resource
  private UserService userService;
  @Resource
  private SignHistoryService signHistoryService;

  @GetMapping
  public Result<?> getSelf(@AuthenticationPrincipal AuthPrincipal auth) {
    return this.userService.getSelf(auth);
  }

  @GetMapping("/signHistory")
  public Result<?> signedHistory(
      @AuthenticationPrincipal AuthPrincipal auth,
      Pagination pagination
  ) {
    return this.signHistoryService.getAll(auth, pagination);
  }

  @PatchMapping
  public Result<?> update(
      @AuthenticationPrincipal AuthPrincipal auth,
      @RequestBody UpdUserRequest request
  ) {
    return this.userService.update(auth, request);
  }

  @PatchMapping("/password")
  public Result<?> changePassword(
      @AuthenticationPrincipal AuthPrincipal auth,
      @RequestBody ChangePasswordRequest request
  ) {
    return this.userService.changePassword(auth, request);
  }
}
