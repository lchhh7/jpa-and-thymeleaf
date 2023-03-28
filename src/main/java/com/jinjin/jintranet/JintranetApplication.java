package com.jinjin.jintranet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class JintranetApplication extends SpringBootServletInitializer{

	public static void main(String[] args) {
		SpringApplication.run(JintranetApplication.class, args);
	}

}
