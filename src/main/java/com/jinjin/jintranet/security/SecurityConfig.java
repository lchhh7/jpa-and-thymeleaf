package com.jinjin.jintranet.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.jinjin.jintranet.security.auth.CustomOAuth2UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig{ //webSecurityConfigurerAdapter was deprecated
	
	private final CustomOAuth2UserService customOAuth2UserService;
	
	public SecurityConfig(CustomOAuth2UserService customOAuth2UserService) {
		this.customOAuth2UserService = customOAuth2UserService;
	}
	
	@Bean
	public BCryptPasswordEncoder encodePWD() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.headers().frameOptions().sameOrigin();
		
		http.cors().disable().csrf().disable();
		
		http.authorizeRequests()
		.antMatchers("/login.do" , "/auth/**" , "/common/**" , "/join.do")
		.permitAll()
		.antMatchers("/admin/**")
		.hasRole("ADMIN")
		.anyRequest().authenticated()
		
		.and().formLogin().loginPage("/login.do")
		.loginProcessingUrl("/auth/loginProc")
		.defaultSuccessUrl("/main.do")
		
		.and().oauth2Login().
		loginPage("/login.do").defaultSuccessUrl("/main.do").userInfoEndpoint().userService(customOAuth2UserService);
		
		return http.build();
	}
}
