package com.app.controller.auth.dto;

import jakarta.validation.constraints.Size;
import org.springframework.validation.annotation.Validated;

import java.util.Set;

@Validated
public record AuthCreateRoleRequest(
        @Size(max = 3, message = "User cannot has more than 3 roles") Set<String> roleListName) {
}
