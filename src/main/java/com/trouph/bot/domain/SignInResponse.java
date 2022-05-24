package com.trouph.bot.domain;

import java.util.UUID;

import com.trouph.graphql.generated.JwtToken;

public record SignInResponse(JwtToken jwtToken, UUID sessionId) {
}
