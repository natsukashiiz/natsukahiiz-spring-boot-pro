package com.natsukashiiz.iiserverapi.service.other;

import com.natsukashiiz.iiboot.configuration.jwt.Authentication;
import com.natsukashiiz.iiboot.configuration.jwt.AuthPrincipal;
import com.natsukashiiz.iiserverapi.entity.User;
import com.natsukashiiz.iiserverapi.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Optional;

@Service
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {
    @Resource
    private UserRepository userRepository;

    @Override
    public AuthPrincipal loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> opt = this.userRepository.findByUsername(username);
        if (!opt.isPresent()) {
            log.warn("UserDetailsServiceImpl-[loadUserByUsername](not found)");
            return null;
        }

        User user = opt.get();
        return AuthPrincipal.build(
                Authentication.builder()
                        .uid(user.getId())
                        .name(user.getUsername())
                        .email(user.getEmail())
                        .build()
        );
    }
}
