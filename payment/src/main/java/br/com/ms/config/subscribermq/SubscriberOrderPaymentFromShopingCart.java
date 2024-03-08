package br.com.ms.config.subscribermq;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.ms.config.publisher.PublisherStatusOrderToShoppingCart;
import br.com.ms.model.PaymentStatus;
import br.com.ms.model.PaymentoOrder;
import br.com.ms.model.StatusPurchase;

@Component
public class SubscriberOrderPaymentFromShopingCart {

	private final PublisherStatusOrderToShoppingCart publisherStatus;

	public SubscriberOrderPaymentFromShopingCart(PublisherStatusOrderToShoppingCart publisher) {
		this.publisherStatus = publisher;
	}

	@RabbitListener(queues = { "${rabbitmq.queue.order}" })
	public void recebendoMensagens(@Payload String payload) {

		try {
			ObjectMapper objectMapper = new ObjectMapper();
			PaymentoOrder orderPay = objectMapper.readValue(payload, PaymentoOrder.class);

			// Só aceita o pagamento se for VISA
			if (orderPay.getBandeira().equalsIgnoreCase("visa")) {
				publisherStatus.EnviarMensagemFilaJson(new PaymentStatus(orderPay.getOrderUuid().toString(), StatusPurchase.CONFIRMED));
				System.out.println("Pagamento APROVADO !!!");
			} else {
				publisherStatus.EnviarMensagemFilaJson(new PaymentStatus(orderPay.getOrderUuid().toString(), StatusPurchase.CANCELLED));
				System.out.println("Problemas com a compra, CANCELADA !!!");
			}

		} catch (Exception e) {
			System.err.println("Erro ao converter a mensagem para o objeto: " + e.getMessage());
		}
	}

}
