package com.app.controller.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record AuthLogin(@NotBlank String username, @NotBlank String password) {
}
