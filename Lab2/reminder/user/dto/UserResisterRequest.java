package org.example.reminder.user.dto;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record UserResisterRequest(@NonNull String login, @NonNull String name, @NonNull String email, @NonNull String password) {
}
