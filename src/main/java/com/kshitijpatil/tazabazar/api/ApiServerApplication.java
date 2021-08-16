package com.kshitijpatil.tazabazar.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ApiServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiServerApplication.class, args);
	}

	@Bean
	@Autowired
	CommandLineRunner initInMemoryDatabase(@Qualifier("in_memory_product") AppInitializer productsInitializer) {
		return args -> productsInitializer.init();
	}

}
