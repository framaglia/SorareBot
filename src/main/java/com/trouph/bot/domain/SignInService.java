package com.trouph.bot.domain;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import com.graphql_java_generator.exception.GraphQLRequestExecutionException;
import com.graphql_java_generator.exception.GraphQLRequestPreparationException;
import com.trouph.bot.infrastracture.HttpSorareClient;
import com.trouph.graphql.generated.signInInput;
import com.trouph.graphql.generated.signInPayload;
import com.trouph.graphql.generated.util.MutationExecutor;


@Service
public class SignInService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SignInService.class);
    private static final String SIGN_IN_QUERY_RESPONSE_DEF = " {\n" +
            "    currentUser {\n" +
            "      slug\n" +
            "      jwtToken(aud: \"trouphe\") {\n" +
            "        token\n" +
            "        expiredAt\n" +
            "      }\n" +
            "    }\n" +
            "    errors {\n" +
            "      message\n" +
            "    }\n" +
            "  }\n" +
            "}";

    private final MutationExecutor mutationExecutor;
    private final HttpSorareClient sorareClient;
    private final UserSessionService userSessionService;

    public SignInService(MutationExecutor mutationExecutor,
                         HttpSorareClient sorareClient,
                         UserSessionService userSessionService) {
        this.mutationExecutor = mutationExecutor;
        this.sorareClient = sorareClient;
        this.userSessionService = userSessionService;
    }

    public SignInResponse signIn(String email, String password) {
        String hashPw = BCrypt.hashpw(password, sorareClient.fetchUserPasswordSalt(email).salt());
        try {
            signInPayload signInPayload = mutationExecutor.signIn(SIGN_IN_QUERY_RESPONSE_DEF, signInInput.builder()
                            .withEmail(email)
                            .withPassword(hashPw)
                            .build(),
                    "email", email, "password", hashPw);
            UUID sessionId = userSessionService.create(new AuthenticatedUser(email, signInPayload.getCurrentUser().getJwtToken()));

            return new SignInResponse(signInPayload.getCurrentUser().getJwtToken(), sessionId);
        } catch (GraphQLRequestExecutionException | GraphQLRequestPreparationException e) {
            LOGGER.error("Failed to signIn", e);
            return null;
        }
    }
}
