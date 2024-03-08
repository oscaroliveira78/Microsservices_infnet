package br.com.ms.config.subscriber;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.ms.model.PaymentStatus;
import br.com.ms.model.PurchaseOrder;
import br.com.ms.model.StatusPurchase;
import br.com.ms.service.EmailService;
import br.com.ms.service.PurchaseOrderService;

@Component
public class SubscriberOrderStatusFromPayment {

	private final WebClient webClient;

	@Autowired
	private EmailService emailService;

	@Autowired
	private PurchaseOrderService purcheaseOrderService;

	public SubscriberOrderStatusFromPayment(WebClient webClient, EmailService emailService, PurchaseOrderService purcheaseOrderService) {
		this.webClient = webClient;
		this.emailService = emailService;
		this.purcheaseOrderService = purcheaseOrderService;
	}

	@RabbitListener(queues = { "${rabbitmq.queue.status}" })
	public void recebendoMensagens(@Payload String payload) {

		try {
			ObjectMapper objectMapper = new ObjectMapper();
			PaymentStatus status = objectMapper.readValue(payload, PaymentStatus.class);

			PurchaseOrder orderStatus = purcheaseOrderService.findByOrderUuid(status.getOrderUuid());

			if (status.getStatus().equals(StatusPurchase.CANCELLED)) {
				System.out.println("Compra Cancelada");
				emailService.sendEmail("Compra Cancelada", "oscarroliveira@gmail.com", "Bandeira não aceita");
			} else {
				System.out.println("Compra Efetivada");
				emailService.sendEmail("Parabéns, compra efetivada com sucesso", "oscarroliveira@gmail.com", "Produtos");
			}

			// remove ShoppingCart
			webClient
			.delete()
			.uri("/produtosg/{userId}", orderStatus.getUserId())
			.headers(header -> {
				header.set("authorization", "eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJtaWNyb3NlcnZpY2VzIiwic3ViIjoiSldUIFRva2VuIiwidXNlcm5hbWUiOiJvc2NhciIsImF1dGhvcml0aWVzIjoiUk9MRV9BRE1JTiIsImlhdCI6MTcwOTgzMzk1OCwiZXhwIjoxNzEwODMzOTU4fQ.GfbmS6Zlr30eOv8hCGPU_tx05j-RTXLHNDXoJxNmhsE");
			})
			.retrieve()
			.bodyToMono(Void.class).block();

			// Change Status OrderPay
			orderStatus.setStatus(status.getStatus());
			
			purcheaseOrderService.save(orderStatus);
		} catch (

		Exception e) {
			System.err.println("Erro ao converter a mensagem para o objeto: " + e.getMessage());
		}
	}

}
