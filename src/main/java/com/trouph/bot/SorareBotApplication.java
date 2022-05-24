package com.trouph.bot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude =
		org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class
)
public class SorareBotApplication {

	public static void main(String[] args) {
		SpringApplication.run(SorareBotApplication.class, args);
	}

}
