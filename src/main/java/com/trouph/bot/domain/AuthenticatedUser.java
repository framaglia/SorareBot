package com.trouph.bot.domain;

import com.trouph.graphql.generated.JwtToken;

public record AuthenticatedUser(String email, JwtToken jwtToken) {
}
