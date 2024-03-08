package br.com.ms.config.publisher;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import br.com.ms.model.dto.DadosPagamento;

@Component
public class PublisherOrderToPayment {

	@Value("${rabbitmq.exchange.name}")
	private String exchange;

	@Value("${rabbitmq.routing.key}")
	private String routingKey;

	private AmqpTemplate rabbitTemplate;

	public PublisherOrderToPayment(AmqpTemplate rabbitTemplate) {

		this.rabbitTemplate = rabbitTemplate;
	}

	public void EnviarMensagemFilaJson(DadosPagamento pagto) throws Exception {

		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());

		String json;
		try {
			json = mapper.writeValueAsString(pagto);
			rabbitTemplate.convertAndSend(this.exchange, this.routingKey, json);
		} catch (JsonProcessingException e) {

			throw new Exception("Erro ao processar Json");
		}

	}

}
