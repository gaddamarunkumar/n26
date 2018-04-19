package com.n26.codechallenge.test;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * AppConfig
 * 
 * @author Gaddam
 *
 */
@Configuration
@ComponentScan(basePackages = { "com.n26.codechallenge.transactions" })
public class AppConfig {
}