package br.com.ms.config.publisher;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.ms.model.PaymentStatus;

@Component
public class PublisherStatusOrderToShoppingCart {

	@Value("${rabbitmq.exchange.name}")
	private String exchange;

	@Value("${rabbitmq.routing.statuskey}")
	private String routingKey;

	private AmqpTemplate rabbitTemplate;

	public PublisherStatusOrderToShoppingCart(AmqpTemplate rabbitTemplate) {

		this.rabbitTemplate = rabbitTemplate;
	}

	public void EnviarMensagemFilaJson(PaymentStatus paymentStatus) {

		ObjectMapper mapper = new ObjectMapper();

		String json;
		try {
			json = mapper.writeValueAsString(paymentStatus);
			rabbitTemplate.convertAndSend(this.exchange, this.routingKey, json);
		} catch (JsonProcessingException e) {

			System.out.println("Erro ao processar Json");
		}
	}

}
