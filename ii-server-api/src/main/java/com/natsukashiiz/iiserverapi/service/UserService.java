package com.natsukashiiz.iiserverapi.service;

import com.natsukashiiz.iiboot.configuration.jwt.AuthPrincipal;
import com.natsukashiiz.iiboot.configuration.jwt.Authentication;
import com.natsukashiiz.iiboot.configuration.jwt.JwtService;
import com.natsukashiiz.iiboot.configuration.jwt.model.TokenResponse;
import com.natsukashiiz.iicommon.common.ResponseCode;
import com.natsukashiiz.iicommon.model.Http;
import com.natsukashiiz.iicommon.model.Result;
import com.natsukashiiz.iicommon.utils.CommonUtils;
import com.natsukashiiz.iicommon.utils.MapperUtils;
import com.natsukashiiz.iicommon.utils.ResultUtils;
import com.natsukashiiz.iicommon.utils.ValidationUtils;
import com.natsukashiiz.iiserverapi.entity.SignHistory;
import com.natsukashiiz.iiserverapi.entity.User;
import com.natsukashiiz.iiserverapi.model.request.ChangePasswordRequest;
import com.natsukashiiz.iiserverapi.model.request.LoginRequest;
import com.natsukashiiz.iiserverapi.model.request.RegisterRequest;
import com.natsukashiiz.iiserverapi.model.request.TokenRefreshRequest;
import com.natsukashiiz.iiserverapi.model.request.UpdUserRequest;
import com.natsukashiiz.iiserverapi.model.response.UserResponse;
import com.natsukashiiz.iiserverapi.repository.SignHistoryRepository;
import com.natsukashiiz.iiserverapi.repository.UserRepository;
import java.util.Objects;
import java.util.Optional;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserService {
    @Resource
    private UserRepository userRepository;
    @Resource
    private SignHistoryRepository historyRepository;
    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private JwtService tokenService;

    public Result<?> getSelf(AuthPrincipal auth) {
        User user = this.userRepository.findById(auth.getId()).get();
        UserResponse response = this.build(user);
        return ResultUtils.success(response);
    }

    public Result<?> update(AuthPrincipal auth, UpdUserRequest request) {
        if (ValidationUtils.invalidEmail(request.getEmail())) {
            log.warn("Update-[block]:(validation email). request:{}, uid:{}", request, auth.getId());
            return ResultUtils.error(ResponseCode.INVALID_EMAIL);
        }

        User user = this.userRepository.findById(auth.getId()).get();
        user.setEmail(request.getEmail());
        User save = this.userRepository.save(user);
        UserResponse response = this.build(save);
        return ResultUtils.success(response);
    }

    public Result<?> changePassword(AuthPrincipal auth, ChangePasswordRequest request) {
        if (ValidationUtils.invalidPassword(request.getCurrentPassword())) {
            log.warn("ChangePassword-[block]:(validation current password). request:{}, uid:{}", request, auth.getId());
            return ResultUtils.error(ResponseCode.INVALID_PASSWORD);
        }

        if (ValidationUtils.invalidPassword(request.getNewPassword())) {
            log.warn("ChangePassword-[block]:(validation new password). request:{}, uid:{}", request, auth.getId());
            return ResultUtils.error(ResponseCode.INVALID_NEW_PASSWORD);
        }

        if (ValidationUtils.invalidPassword(request.getConfirmPassword())) {
            log.warn("ChangePassword-[block]:(validation confirm password). request:{}, uid:{}", request, auth.getId());
            return ResultUtils.error(ResponseCode.INVALID_NEW_PASSWORD);
        }

        if (!Objects.equals(request.getNewPassword(), request.getConfirmPassword())) {
            log.warn("ChangePassword-[block]:(password not match). request:{}, uid:{}", request, auth.getId());
            return ResultUtils.error(ResponseCode.PASSWORD_NOT_MATCH);
        }

        User user = this.userRepository.findById(auth.getId()).get();

        // check password
        if (this.notMatchPassword(request.getCurrentPassword(), user.getPassword())) {
            log.warn("ChangePassword-[block]:(incorrect password). request:{}, uid:{}", request, auth.getId());
            return ResultUtils.error(ResponseCode.INVALID_PASSWORD);
        }

        // password encoded
        String passwordEncoded = this.passwordEncoder.encode(request.getNewPassword());

        user.setPassword(passwordEncoded);
        User save = this.userRepository.save(user);
        UserResponse response = this.build(save);
        return ResultUtils.success(response);
    }

    public Result<?> create(RegisterRequest request) {
        // validate
        if (ValidationUtils.invalidEmail(request.getEmail())) {
            log.warn("Create-[block]:(validation email). request:{}", request);
            return ResultUtils.error(ResponseCode.INVALID_EMAIL);
        }

        if (ValidationUtils.invalidUsername(request.getUsername())) {
            log.warn("Create-[block]:(validation username). request:{}", request);
            return ResultUtils.error(ResponseCode.INVALID_USERNAME);
        }

        if (ValidationUtils.invalidPassword(request.getPassword())) {
            log.warn("Create-[block]:(validation password). request:{}", request);
            return ResultUtils.error(ResponseCode.INVALID_PASSWORD);
        }

        String email = request.getEmail();
        String username = request.getUsername();
        String password = request.getPassword();

        // check email existed
        if (this.userRepository.existsByEmail(email)) {
            log.warn("Create-[block]:(email existed). request:{}", request);
            return ResultUtils.error(ResponseCode.EXISTED_EMAIL);
        }

        // check username existed
        if (this.userRepository.existsByUsername(username)) {
            log.warn("Create-[block]:(username existed). request:{}", request);
            return ResultUtils.error(ResponseCode.EXISTED_USERNAME);
        }

        // encode password
        String passwordEncoded = this.passwordEncoder.encode(password);

        // to entity
        User entity = new User();
        entity.setEmail(email);
        entity.setUsername(username);
        entity.setPassword(passwordEncoded);

        // save
        User save = this.userRepository.save(entity);
        UserResponse response = this.build(save);
        return ResultUtils.success(response);
    }

    public Result<?> login(LoginRequest request, HttpServletRequest httpRequest) {

        // validate
        if (ValidationUtils.invalidUsername(request.getUsername())) {
            log.debug("Login-[block]:(validation username). request:{}", request);
            return ResultUtils.error(ResponseCode.INVALID_USERNAME);
        }

        if (ValidationUtils.invalidPassword(request.getPassword())) {
            log.debug("Login-[block]:(validation password). request:{}", request);
            return ResultUtils.error(ResponseCode.INVALID_PASSWORD);
        }

        String username = request.getUsername();
        String password = request.getPassword();

        // check user in db
        Optional<User> opt = this.userRepository.findByUsername(username);

        if (!opt.isPresent()) {
            log.warn("Login-[block]:(not found). request:{}", request);
            return ResultUtils.error(ResponseCode.INVALID_USERNAME_PASSWORD);
        }

        User user = opt.get();
        if (this.notMatchPassword(password, user.getPassword())) {
            log.warn("Login-[block]:(incorrect password). request:{}", request);
            return ResultUtils.error(ResponseCode.INVALID_USERNAME_PASSWORD);
        }

        Http http = CommonUtils.getHttp(httpRequest);

        // save sign history
        SignHistory history = new SignHistory();
        history.setUser(user);
        history.setIpv4(http.getIpv4());
        history.setDevice(http.getDevice().value());
        history.setUa(http.getUa());
        this.historyRepository.save(history);

        // generate token
        return ResultUtils.success(this.genToken(user));
    }

    public Result<?> refreshToken(TokenRefreshRequest request) {
        if (Objects.isNull(request.getToken())) {
            log.warn("RefreshToken-[block]:(validation refresh token). request:{}", request);
            return ResultUtils.unknown();
        }

        if (!this.tokenService.validate(request.getToken())) {
            log.warn("RefreshToken-[block]:(refresh token expired). request:{}", request);
            return ResultUtils.error(ResponseCode.REFRESH_TOKEN_EXPIRE);
        }

        String username = this.tokenService.getUsername(request.getToken());
        Optional<User> opt = this.userRepository.findByUsername(username);
        if (!opt.isPresent()) {
            log.warn("RefreshToken-[block]:(user not found). request:{}", request);
            return ResultUtils.unknown();
        }
        User user = opt.get();
        TokenResponse token = this.genToken(user);
        return ResultUtils.success(token);
    }

    private TokenResponse genToken(User user) {
        return this.tokenService.generateToken(
                Authentication.builder()
                        .uid(user.getId())
                        .name(user.getUsername())
                        .email(user.getEmail())
                        .build()
        );
    }

    private boolean notMatchPassword(String rawPassword, String encodedPassword) {
        return !this.passwordEncoder.matches(rawPassword, encodedPassword);
    }

    private UserResponse build(User user) {
        return MapperUtils.mapOne(user, UserResponse.class);
    }
}
