package br.com.ms;

import java.util.TimeZone;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableRabbit
public class PaymentApplication {

	public static void main(String[] args) {

		TimeZone.setDefault(TimeZone.getTimeZone("America/Sao_Paulo"));
		SpringApplication.run(PaymentApplication.class, args);

		System.out.println("http://localhost:8111/swagger/ui");
		System.out.println("http://localhost:8111/swagger/json");
		System.out.println("http://localhost:8111//h2-console");
		System.out.println("http://localhost:8761/eureka/apps/ms-payment");
		// Run Rabbitmq
		System.out.println("docker run -d --hostname my-rabbit --name rabbit13 -p 15672:15672 -p 5672:5672 -p 25676:25676 rabbitmq:3-management");
		// Access Rabbitmq (username: guest - password: guest)
		System.out.println("http://localhost:15672/");
	}
}
