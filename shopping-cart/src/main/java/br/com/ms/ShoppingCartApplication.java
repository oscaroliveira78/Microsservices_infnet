package br.com.ms;

import java.util.TimeZone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ShoppingCartApplication {

	public static void main(String[] args) {

		TimeZone.setDefault(TimeZone.getTimeZone("America/Sao_Paulo"));
		SpringApplication.run(ShoppingCartApplication.class, args);

		System.out.println("http://localhost:8113/swagger/ui");
		System.out.println("http://localhost:8113/swagger/json");
		System.out.println("http://localhost:8113/h2-console");
		System.out.println("http://localhost:8761/eureka/apps/ms-shoppingcart");
	}

}
