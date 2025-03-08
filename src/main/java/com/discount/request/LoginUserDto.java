package com.discount.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Login user info details")
public record LoginUserDto(
        @NotBlank(message = "Username cannot null or empty") String username,
        @NotBlank(message = "Password cannot null or empty") String password) {
}
