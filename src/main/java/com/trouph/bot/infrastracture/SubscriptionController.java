package com.trouph.bot.infrastracture;

import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.trouph.bot.domain.CardSubscriptionService;

@RestController
public class SubscriptionController {

    private final CardSubscriptionService cardSubscriptionService;

    public SubscriptionController(CardSubscriptionService cardSubscriptionService) {
        this.cardSubscriptionService = cardSubscriptionService;
    }

    @GetMapping("subscribe/cardUpdated")
    public String subscribeToCardUpdate(@RequestParam UUID sessionId) {
        cardSubscriptionService.subscribe(sessionId);
        return "Subscribed";
    }
}
