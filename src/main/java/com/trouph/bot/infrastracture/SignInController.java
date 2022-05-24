package com.trouph.bot.infrastracture;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.trouph.bot.domain.SignInResponse;
import com.trouph.bot.domain.SignInService;

@RestController
public class SignInController {
    private final SignInService signInService;

    public SignInController(SignInService signInService) {
        this.signInService = signInService;
    }

    @GetMapping("/signIn")
    public SignInResponse signIn(@RequestParam String email, @RequestParam String password) {
        return signInService.signIn(email, password);
    }
}
