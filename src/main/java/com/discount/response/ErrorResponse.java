package com.discount.response;

import java.util.Map;

public record ErrorResponse(String message, Map<String, String> errorDetails) {
}
