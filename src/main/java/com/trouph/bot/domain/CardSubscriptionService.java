package com.trouph.bot.domain;

import java.util.Collections;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.graphql_java_generator.client.SubscriptionCallback;
import com.graphql_java_generator.exception.GraphQLRequestExecutionException;
import com.graphql_java_generator.exception.GraphQLRequestPreparationException;
import com.trouph.graphql.generated.CardSubscription;
import com.trouph.graphql.generated.util.SubscriptionExecutor;

@Service
public class CardSubscriptionService {

    private final static Logger LOGGER = LoggerFactory.getLogger(CardSubscriptionService.class);

    private final SubscriptionExecutor subscriptionExecutor;
    private final UserSessionService userSessionService;

    public CardSubscriptionService(SubscriptionExecutor subscriptionExecutor,
                                   UserSessionService userSessionService) {
        this.subscriptionExecutor = subscriptionExecutor;
        this.userSessionService = userSessionService;
    }

    public void subscribe(UUID userSessionId) {
        try {
            subscriptionExecutor.aCardWasUpdated("{ slug }", new SubscriptionCallback<>() {
                        @Override
                        public void onConnect() {
                            LOGGER.info("Connected");
                        }

                        @Override
                        public void onMessage(CardSubscription cardSubscription) {
                            LOGGER.info(cardSubscription.getName());
                        }

                        @Override
                        public void onClose(int statusCode, String reason) {
                            LOGGER.info(reason);
                        }

                        @Override
                        public void onError(Throwable cause) {
                            LOGGER.error(cause.getMessage(), cause);
                        }
                    },
                    Collections.emptyList(),
                    Collections.emptyList(),
                    Collections.emptyList(),
                    Collections.emptyList(),
                    false,
                    Collections.emptyList(),
                    Collections.emptyList(),
                    Collections.emptyList(),
                    Collections.emptyList(),
                    Collections.emptyList(),
                    Collections.emptyList(),
                    "auth", userSessionService.fetch(userSessionId).jwtToken().getToken());
        } catch (GraphQLRequestExecutionException | GraphQLRequestPreparationException e) {
            throw new RuntimeException(e);
        }
    }
}
