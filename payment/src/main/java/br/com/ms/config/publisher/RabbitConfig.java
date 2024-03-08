package br.com.ms.config.publisher;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class RabbitConfig {

	@Value("${rabbitmq.queue.order}")
	private String queueOrderPayment;

	@Value("${rabbitmq.queue.status}")
	private String queueStatusOrder;

	@Value("${rabbitmq.exchange.name}")
	private String exchange;

	@Value("${rabbitmq.routing.key}")
	private String routingkey;

	@Value("${rabbitmq.routing.statuskey}")
	private String routingkeyStatus;

	@Bean
	public Queue queueOrder() {
		return new Queue(queueOrderPayment);
	}

	@Bean
	public Queue queueStatus() {
		return new Queue(queueStatusOrder);
	}

	@Bean
	public TopicExchange exchange() {
		return new TopicExchange(exchange);
	}

	@Bean
	public Binding bindindOrder() {
		return BindingBuilder.bind(queueStatus()).to(exchange()).with(routingkeyStatus);
	}

	@Bean
	public Binding bindindStatus() {
		return BindingBuilder.bind(queueOrder()).to(exchange()).with(routingkey);
	}

}
