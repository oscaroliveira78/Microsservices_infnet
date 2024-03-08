package br.com.ms.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JwtController {

	@GetMapping(value = "jwt")
	public String getToken() {

		return "Token no Response Header - Utilizar Basic Auth";
	}

}
