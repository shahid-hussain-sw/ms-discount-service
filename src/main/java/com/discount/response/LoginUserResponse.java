package com.discount.response;

public record LoginUserResponse(String accessToken, Long expiry) {
}
