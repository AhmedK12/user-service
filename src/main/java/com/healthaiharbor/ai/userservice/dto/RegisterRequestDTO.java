package com.healthaiharbor.ai.userservice.dto;

import java.security.Permissions;
import java.util.List;

public record RegisterRequestDTO(String username, String email, String Firstname, String Lastname, String Role, List<Permissions> permissions, String password) {
}
