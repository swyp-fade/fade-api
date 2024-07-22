package com.fade;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class FadeApplication {

	public static void main(String[] args) {
		SpringApplication.run(FadeApplication.class, args);
	}

}
