package br.com.ms;

import java.util.TimeZone;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CatalogoProdutosApplication {

	public static void main(String[] args) {

		TimeZone.setDefault(TimeZone.getTimeZone("America/Sao_Paulo"));
		SpringApplication.run(CatalogoProdutosApplication.class, args);

		System.out.println("http://localhost:8111/swagger/ui");
		System.out.println("http://localhost:8111/swagger/json");
		System.out.println("http://localhost:8111/h2-console");
		System.out.println("http://localhost:8761/eureka/apps/ms-products");
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

}
