package br.com.ms.config.security;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JWTTokenValidatorFilter extends OncePerRequestFilter {

	@Autowired
	private ErrorsJwt errosJwt;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

		String jwt = request.getHeader(SecurityConstants.JWT_HEADER);
		if (null != jwt) {		

			jwt = jwt.replace("Bearer", "").trim();
			try {
				SecretKey key = Keys.hmacShaKeyFor(SecurityConstants.JWT_KEY.getBytes(StandardCharsets.UTF_8));

				Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt).getBody();

				String username = String.valueOf(claims.get("username"));
				String authorities = (String) claims.get("authorities");
				Authentication auth = new UsernamePasswordAuthenticationToken(username, null, AuthorityUtils.commaSeparatedStringToAuthorityList(authorities));
				SecurityContextHolder.getContext().setAuthentication(auth);

				filterChain.doFilter(request, response);

			} catch (Exception e) {

				System.err.println(e.getMessage());
				TabelaDeErros buildError = errosJwt.buildError(e.getClass());
				response.setContentType("text/plain");
				response.setStatus(buildError.getCodigoHttp());
				response.getOutputStream().write(buildError.getMensagem().getBytes("UTF-8"));
			}
		}
		filterChain.doFilter(request, response);
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
	
		return request.getServletPath().equals("/produtosg") && request.getMethod().equals("GET");
	}

}