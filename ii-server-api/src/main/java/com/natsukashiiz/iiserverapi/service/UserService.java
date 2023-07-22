package com.natsukashiiz.iiserverapi.service;

import com.natsukashiiz.iiboot.configuration.jwt.Authentication;
import com.natsukashiiz.iiboot.configuration.jwt.JwtService;
import com.natsukashiiz.iiboot.configuration.jwt.UserDetailsImpl;
import com.natsukashiiz.iiboot.configuration.jwt.model.TokenResponse;
import com.natsukashiiz.iicommon.common.DeviceCode;
import com.natsukashiiz.iicommon.common.ResponseCode;
import com.natsukashiiz.iicommon.model.Result;
import com.natsukashiiz.iicommon.utils.CommonUtil;
import com.natsukashiiz.iicommon.utils.MapperUtil;
import com.natsukashiiz.iicommon.utils.ResponseUtil;
import com.natsukashiiz.iicommon.utils.ValidateUtil;
import com.natsukashiiz.iiserverapi.entity.SignHistory;
import com.natsukashiiz.iiserverapi.entity.User;
import com.natsukashiiz.iiserverapi.model.request.*;
import com.natsukashiiz.iiserverapi.model.response.UserResponse;
import com.natsukashiiz.iiserverapi.repository.SignedHistoryRepository;
import com.natsukashiiz.iiserverapi.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class UserService {
    @Resource
    private UserRepository userRepository;
    @Resource
    private SignedHistoryRepository historyRepository;
    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private JwtService tokenService;

    public Result<?> getSelf(UserDetailsImpl auth) {
        Optional<User> opt = this.userRepository.findByUsername(auth.getUsername());
        if (!opt.isPresent()) {
            return ResponseUtil.error(ResponseCode.UNAUTHORIZED);
        }

        User user = opt.get();
        UserResponse response = this.build(user);
        return ResponseUtil.success(response);
    }

    public Result<?> update(UserDetailsImpl auth, UpdateUserRequest request) {
        if (ValidateUtil.invalidEmail(request.getEmail())) {
            return ResponseUtil.error(ResponseCode.INVALID_EMAIL);
        }
        Optional<User> opt = this.userRepository.findByUsername(auth.getUsername());
        if (!opt.isPresent()) {
            return ResponseUtil.error(ResponseCode.NOT_FOUND);
        }

        User user = opt.get();
        user.setEmail(request.getEmail());
        User save = this.userRepository.save(user);
        UserResponse response = this.build(save);
        return ResponseUtil.success(response);
    }

    public Result<?> changePassword(UserDetailsImpl auth, ChangePasswordRequest request) {
        if (ValidateUtil.invalidPassword(request.getCurrentPassword())) {
            return ResponseUtil.error(ResponseCode.INVALID_PASSWORD);
        }

        if (ValidateUtil.invalidPassword(request.getNewPassword())) {
            return ResponseUtil.error(ResponseCode.INVALID_NEW_PASSWORD);
        }

        if (!Objects.equals(request.getNewPassword(), request.getConfirmPassword())) {
            return ResponseUtil.error(ResponseCode.PASSWORD_NOT_MATCH);
        }

        Optional<User> opt = userRepository.findByUsername(auth.getUsername());
        if (!opt.isPresent()) {
            return ResponseUtil.error(ResponseCode.NOT_FOUND);
        }

        User user = opt.get();

        // check password
        if (notMatchPassword(request.getCurrentPassword(), user.getPassword())) {
            log.warn("ChangePassword-[block]:(incorrect password). request:{}, uid:{}", request, auth.getId());
            return ResponseUtil.error(ResponseCode.INVALID_PASSWORD);
        }

        // password encoded
        String passwordEncoded = passwordEncoder.encode(request.getNewPassword());

        user.setPassword(passwordEncoded);
        User save = userRepository.save(user);
        UserResponse response = this.build(save);
        return ResponseUtil.success(response);
    }

    public Result<?> create(RegisterRequest request) {
        // validate
        if (ValidateUtil.invalidEmail(request.getEmail())) {
            log.warn("Create-[block]:(validation email). request:{}", request);
            return ResponseUtil.error(ResponseCode.INVALID_EMAIL);
        }

        if (ValidateUtil.invalidUsername(request.getUsername())) {
            log.warn("Create-[block]:(validation username). request:{}", request);
            return ResponseUtil.error(ResponseCode.INVALID_USERNAME);
        }

        if (ValidateUtil.invalidPassword(request.getPassword())) {
            log.warn("Create-[block]:(validation password). request:{}", request);
            return ResponseUtil.error(ResponseCode.INVALID_PASSWORD);
        }

        String email = request.getEmail();
        String username = request.getUsername();
        String password = request.getPassword();

        // check email existed
        if (userRepository.existsByEmail(email)) {
            log.warn("Create-[block]:(email existed). request:{}", request);
            return ResponseUtil.error(ResponseCode.EXISTED_EMAIL);
        }

        // check username existed
        if (userRepository.existsByUsername(username)) {
            log.warn("Create-[block]:(username existed). request:{}", request);
            return ResponseUtil.error(ResponseCode.EXISTED_USERNAME);
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
        return ResponseUtil.success(response);
    }

    public Result<?> login(LoginRequest request, HttpServletRequest httpRequest) {

        // validate
        if (ValidateUtil.invalidUsername(request.getUsername())) {
            log.debug("Login-[block]:(validation username). request:{}", request);
            return ResponseUtil.error(ResponseCode.INVALID_USERNAME);
        }

        if (ValidateUtil.invalidPassword(request.getPassword())) {
            log.debug("Login-[block]:(validation password). request:{}", request);
            return ResponseUtil.error(ResponseCode.INVALID_PASSWORD);
        }

        String username = request.getUsername();
        String password = request.getPassword();

        // check user in db
        Optional<User> opt = this.userRepository.findByUsername(username);

        if (!opt.isPresent()) {
            log.debug("Login-[block]:(not found). request:{}", request);
            return ResponseUtil.error(ResponseCode.INVALID_USERNAME_PASSWORD);
        }

        User user = opt.get();
        if (this.notMatchPassword(password, user.getPassword())) {
            log.debug("Login-[block]:(incorrect password). request:{}", request);
            return ResponseUtil.error(ResponseCode.INVALID_USERNAME_PASSWORD);
        }

        String ipv4 = CommonUtil.getIpAddress(httpRequest);
        String userAgent = CommonUtil.getUserAgent(httpRequest);
        DeviceCode device = CommonUtil.getDevice(userAgent);

        // save signed history
        SignHistory history = new SignHistory();
        history.setUser(user);
        history.setIpv4(ipv4);
        history.setDevice(device.value());
        history.setUa(userAgent);
        this.historyRepository.save(history);

        // generate token
        return ResponseUtil.success(this.genToken(user));
    }

    public Result<?> refreshToken(TokenRefreshRequest request) {
        if (Objects.isNull(request.getRefreshToken())) {
            log.warn("RefreshToken-[block]:(validation refresh token). request:{}", request);
            return ResponseUtil.error(ResponseCode.INVALID_REQUEST);
        }

        String refreshToken = request.getRefreshToken();
        if (!this.tokenService.validate(refreshToken)) {
            log.warn("RefreshToken-[block]:(refresh token expired). request:{}", request);
            return ResponseUtil.error(ResponseCode.REFRESH_TOKEN_EXPIRE);
        }

        String username = this.tokenService.getUsername(refreshToken);
        Optional<User> opt = this.userRepository.findByUsername(username);
        if (!opt.isPresent()) {
            log.warn("RefreshToken-[block]:(user not found). request:{}", request);
            return ResponseUtil.error(ResponseCode.UNAUTHORIZED);
        }
        User user = opt.get();
        TokenResponse token = this.genToken(user);
        return ResponseUtil.success(token);
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
        return MapperUtil.mapOne(user, UserResponse.class);
    }
}
