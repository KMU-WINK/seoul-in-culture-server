package com.github.kmu_wink.seoul_in_culture.common.firebase;

import java.io.IOException;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import jakarta.annotation.PostConstruct;
import lombok.SneakyThrows;

@Configuration
public class FirebaseConfig {

	@PostConstruct
	@SneakyThrows(IOException.class)
	public void initialize() {

		Resource resource = new ClassPathResource("firebase-adminsdk.json");

		FirebaseOptions options = FirebaseOptions.builder()
			.setCredentials(GoogleCredentials.fromStream(resource.getInputStream()))
			.build();

		FirebaseApp.initializeApp(options);
	}
}
