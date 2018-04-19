package com.n26.codechallenge.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * StarterApp
 * 
 * @author Gaddam
 *
 */
@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan({ "com.n26.codechallenge" })
@Configuration
@EntityScan({ "com.n26.codechallenge" })
public class StarterApp extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(StarterApp.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(StarterApp.class, args);
	}
}
