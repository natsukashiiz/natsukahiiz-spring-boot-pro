package com.natsukashiiz.iiboot.configuration.jwt;

import com.natsukashiiz.iiboot.configuration.jwt.model.Token;
import com.natsukashiiz.iiboot.configuration.jwt.model.TokenResponse;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@Slf4j
public class JwtService {
    @Resource
    private  JwtProperties jwtProperties;
    @Resource
    private  JwtEncoder encoder;
    @Resource
    private  JwtDecoder decoder;

    public TokenResponse generateToken(Authentication authentication) {
        Token accessToken = this.generateAccessToken(authentication);
        Token refreshToken = this.generateRefreshToken(authentication);
        return TokenResponse.builder()
                .access(accessToken.getToken())
                .accessExpire(accessToken.getExpire())
                .refresh(refreshToken.getToken())
                .refreshExpire(refreshToken.getExpire())
                .build();
    }

    private Token generateAccessToken(Authentication authentication) {
        return generateToken(authentication, this.jwtProperties.getExpirationMs(), true);
    }

    private Token generateRefreshToken(Authentication authentication) {
        return generateToken(authentication, this.jwtProperties.getExpirationMs() * 2, false);
    }

    private Token generateToken(Authentication authentication, long expire, boolean all) {
        Instant now = Instant.now();
        Instant expiresAt = now.plusMillis(expire);
        JwtClaimsSet.Builder builder = JwtClaimsSet.builder()
                .issuer(this.jwtProperties.getIssuer())
                .issuedAt(now)
                .expiresAt(expiresAt)
                .id(UUID.randomUUID().toString());

        if (all) {
            builder
                    .claim("uid", authentication.getUid())
                    .claim("name", authentication.getName())
                    .claim("email", authentication.getEmail());
        }

        JwtClaimsSet claims = builder.build();
        return Token.builder()
                .token(this.encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue())
                .expire(expiresAt.toEpochMilli())
                .build();
    }

    public boolean validate(String token) {
        try {
            this.decoder.decode(token);
            return true;
        } catch (JwtException e) {
            log.debug("JwtService-[validate](invalid). token: {}, error: {}", token, e.getMessage());
            return false;
        }
    }

    public String getUsername(String token) {
        Jwt decode = this.decoder.decode(token);
        return decode.getClaim("name");
    }
}
