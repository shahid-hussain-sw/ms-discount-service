package com.discount.controller;

import com.discount.request.LoginUserDto;
import com.discount.response.LoginUserResponse;
import com.discount.service.UserDetailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Authentication Controller")
public class AuthenticationController {

    private final UserDetailService userDetailService;

    @Operation(summary = "Authenticat user")
    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public LoginUserResponse authenticate(@Valid @RequestBody LoginUserDto loginUserDto) {
        return userDetailService.authenticate(loginUserDto);
    }
}
