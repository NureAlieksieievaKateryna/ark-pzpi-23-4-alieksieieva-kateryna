package org.example.reminder.user.dto;

import lombok.Builder;

@Builder
public record UserResponse(Long id, String login, String email) {
}
