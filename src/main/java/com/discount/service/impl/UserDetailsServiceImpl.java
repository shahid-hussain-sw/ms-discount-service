package com.discount.service.impl;

import com.discount.entity.User;
import com.discount.exception.BusinessException;
import com.discount.repository.UserRepository;
import com.discount.request.LoginUserDto;
import com.discount.response.LoginUserResponse;
import com.discount.service.JwtService;
import com.discount.service.UserDetailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailService {

    private final UserRepository userRepository;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    @Override
    public LoginUserResponse authenticate(LoginUserDto userLoginDto) {
        try {
            userAuthenticate(userLoginDto);
            User user = userRepository.findByUsername(userLoginDto.username()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
            return generateJwtToken(user);
        } catch (BadCredentialsException e) {
            log.error("Credentials are not valid", e);
            throw new BusinessException("Credentials are not valid", 400);
        } catch (Exception e) {
            log.error("Failed to login user", e);
            throw new BusinessException("Failed to login", 500);
        }
    }

    private void userAuthenticate(LoginUserDto userLoginDto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userLoginDto.username(), userLoginDto.password()));
    }

    private LoginUserResponse generateJwtToken(User user) {
        String jwtToken = jwtService.generateToken(user);
        return new LoginUserResponse(jwtToken, jwtService.getExpirationTime());
    }

}
