package br.com.ms.services;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.crypto.SecretKey;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class generateJwtToken {
	
	public String generateJwtTokenB(Authentication auth) {
		
		Map<String, Object> claims = new HashMap<>();
		claims.put("granted", auth.getAuthorities().toString());
		
		SecretKey key = Keys.hmacShaKeyFor(SecurityConstants.JWT_KEY.getBytes(StandardCharsets.UTF_8));
		String jwt = Jwts.builder()
				.setIssuer("microservices")
				.setSubject("JWT Token")
				.claim("username", auth.getName())
				.claim("authorities", populateAuthorities(auth.getAuthorities()))
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + 1000000000))
				.signWith(key)
			.compact();
		
		return jwt;
	}
	
	
	
	private String populateAuthorities(Collection<? extends GrantedAuthority> collection) {
		Set<String> authoritiesSet = new HashSet<>();
		for (GrantedAuthority authority : collection) {
			authoritiesSet.add(authority.getAuthority());
		}
		return String.join(",", authoritiesSet);
	}
}