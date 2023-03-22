package com.example.swd;

import com.example.swd.data.Data;
import com.example.swd.server.RecommendationSystem;
import org.springframework.boot.SpringApplication;
import org.springfdramework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SwdApplication {

	public static void main(String[] args) {
		SpringApplication.run(SwdApplication.class, args);
		RecommendationSystem.setupAndStart();
	}
}
