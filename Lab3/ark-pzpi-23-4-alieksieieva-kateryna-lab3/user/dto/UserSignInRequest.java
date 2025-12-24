package org.example.reminder.user.dto;

import lombok.Builder;

@Builder
public record UserSignInRequest (String login, String password) {}
