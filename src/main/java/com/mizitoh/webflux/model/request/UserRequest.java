package com.mizitoh.webflux.model.request;

import com.mizitoh.webflux.validator.TrimString;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserRequest(
        @NotBlank
        @TrimString
        String name,
        @Email
        String email,
        @NotBlank
        String password
) {
}
