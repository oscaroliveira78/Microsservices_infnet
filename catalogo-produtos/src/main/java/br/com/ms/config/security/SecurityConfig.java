package br.com.ms.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@EnableWebSecurity(debug = false)
public class SecurityConfig {

	@Autowired
	JWTTokenValidatorFilter vali;

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
	}

	@Bean
	public UserDetailsService userDetailsService() {

		UserDetails admin = User.withUsername("oscar")
									.password("{bcrypt}$2a$10$H/acntKbG3d0hLIPkWKwmOfrGMagj.eQiaKL9VI2VgEtxTmSKLU32")
									.roles("ADMIN")
									.build();

		return new InMemoryUserDetailsManager(admin);
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http.csrf(csrf -> {
			csrf.disable();
		})
		.headers(header -> header.frameOptions().disable())
		.authorizeHttpRequests((authz) -> {
			authz.antMatchers("/h2-console/**").permitAll();
			authz.antMatchers("/produtosg").permitAll();
			authz.antMatchers("/produtosg/{uuid}").hasAnyRole("ADMIN");
			authz.antMatchers("/produtosg/addcart/**").hasAnyRole("ADMIN");
			authz.antMatchers("/produtosg/{userId}").hasAnyRole("ADMIN");
			authz.antMatchers("/produtosg/getcart/**").hasAnyRole("ADMIN");
			
			authz.anyRequest().authenticated();
		})
		.addFilterBefore(vali, BasicAuthenticationFilter.class)
		.sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		return http.build();

	}

}
