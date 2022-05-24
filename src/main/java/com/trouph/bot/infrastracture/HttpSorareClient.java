package com.trouph.bot.infrastracture;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class HttpSorareClient {


    private final String uri;
    private final RestTemplate restTemplate;

    public HttpSorareClient(@Value("${trouph.sorare.uri}") String uri,
                            RestTemplate restTemplate) {
        this.uri = uri;
        this.restTemplate = restTemplate;
    }

    public UserPasswordSalt fetchUserPasswordSalt(String email) {
        return restTemplate.getForObject(uri + "/" + email, UserPasswordSalt.class);
    }
}
