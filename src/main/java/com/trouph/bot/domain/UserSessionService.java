package com.trouph.bot.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;

import org.springframework.stereotype.Service;

@Service
public class UserSessionService {

    private final static Map<UUID, AuthenticatedUser> userSessions = new HashMap<>();

    public UUID create(AuthenticatedUser authenticatedUser) {
        UUID sessionId = UUID.randomUUID();
        userSessions.put(sessionId, authenticatedUser);
        return sessionId;
    }

    public AuthenticatedUser fetch(UUID uuid) {
        return Optional.ofNullable(userSessions.get(uuid)).orElseThrow(() ->
                new NotFoundException(Response.status(Response.Status.UNAUTHORIZED).build()));
    }
}
