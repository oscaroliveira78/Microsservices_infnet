package br.com.ms.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import br.com.ms.config.publisher.PublisherOrderToPayment;
import br.com.ms.model.PurchaseOrder;
import br.com.ms.model.ShoppingCart;
import br.com.ms.model.StatusPurchase;
import br.com.ms.model.dto.DadosPagamento;
import br.com.ms.model.dto.ProdutoResponse;
import br.com.ms.service.PurchaseOrderService;
import br.com.ms.service.ShoppingCartService;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/cart")
@CrossOrigin
public class ShoppingCartController {

	private final WebClient webClient;
	private final PurchaseOrderService purchaseOrderService;
	private final PublisherOrderToPayment publicar;

	@Autowired
	public ShoppingCartController(WebClient webClient, PurchaseOrderService purchaseOrderService, ShoppingCartService shoppingCartService,
			PublisherOrderToPayment publicar) {
		this.webClient = webClient;
		this.purchaseOrderService = purchaseOrderService;
		this.publicar = publicar;
	}

	@GetMapping
	public ResponseEntity<?> getAllPurchases() {

		return ResponseEntity.ok(purchaseOrderService.findAll());
	}

	@PostMapping("/checkout") // o Usuario eu busco pelo Principal
	public ResponseEntity<?> checkOut(@RequestHeader("Authorization") String authorizationHeader, @RequestBody DadosPagamento dadosPagamento) {

		// Pegar do Principal o Usuario
		dadosPagamento.setEmailUser(dadosPagamento.getEmailUser());

		// validando Carrinho / parametro é o Id do Usuario
		Boolean valido = validarDadosDoCarrinhoEContinuar("oscar", authorizationHeader.replace("Bearer ", "")).block();
		if (valido) {
			List<ProdutoResponse> buscarCarrinhoDoUsuario = buscarCarrinhoDoUsuario("oscar", authorizationHeader.replace("Bearer ", ""));

			// Save DB Purchase_Order
			PurchaseOrder order = persistDb(buscarCarrinhoDoUsuario);
			dadosPagamento.setOrderUuid(order.getOrderUuid());

			// Generate Purchase Order - Authenticated - Rabbitmq Publisher
			try {
				publicar.EnviarMensagemFilaJson(dadosPagamento);
				return ResponseEntity.ok("Seu pagamento esta sendo processado e em breve voce receberá informações");
			} catch (Exception e) {
				System.out.println(e.getMessage());
				return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Erro ao processar dados!!!");
			}
		}
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Erro ao processar dados, ou API de Produtos fora do AR!!!");
	}

	private PurchaseOrder persistDb(List<ProdutoResponse> carrinhoDeCompras) {

		List<ShoppingCart> listaDeProdutos = new ArrayList<>();

		PurchaseOrder pO = new PurchaseOrder();
		pO.setOrderUuid(UUID.randomUUID().toString());
		pO.setUserId(carrinhoDeCompras.get(0).getUserId());
		pO.setTotalAmount(new BigDecimal("0"));
		pO.setStatus(StatusPurchase.PENDING);

		PurchaseOrder save = purchaseOrderService.save(pO);

		for (ProdutoResponse produtoResponse : carrinhoDeCompras) {

			ShoppingCart sC = new ShoppingCart();
			sC.setPriceProduct(produtoResponse.getPreco());
			sC.setUuidProduct(produtoResponse.getUuid());
			sC.setPurchaseOrder(save);

			pO.setTotalAmount(pO.getTotalAmount().add(sC.getPriceProduct()));

			listaDeProdutos.add(sC);
		}

		save.setShoppingCarts(listaDeProdutos);
		return purchaseOrderService.save(save);
	}

	public List<ProdutoResponse> buscarCarrinhoDoUsuario(String userId, @RequestHeader("Authorization") String authorizationHeader) {

		return webClient.get().uri("/produtosg/getcart/{userId}", userId).headers(header -> {
			header.set("authorization", authorizationHeader.replace("Bearer ", ""));
		}).accept(MediaType.APPLICATION_JSON).retrieve().bodyToMono(new ParameterizedTypeReference<List<ProdutoResponse>>() {
		}).blockOptional() // Bloqueia e obtém o valor, ou Optional vazio se a resposta for vazia
				.orElse(List.of()); // Trata o caso em que a resposta é vazia, retornando uma lista vazia
	}

	public Mono<Boolean> validarDadosDoCarrinhoEContinuar(String userId, String authorizationHeader) {

		return webClient.get().uri("/produtosg/validatecart/{userId}", userId).headers(header -> {
			header.set("authorization", authorizationHeader.replace("Bearer ", ""));
		}).retrieve().toEntity(String.class).flatMap(responseEntity -> {
			HttpStatus status = responseEntity.getStatusCode();

			if (status.is2xxSuccessful()) {
				System.out.println("Carrinho aprovado");
				return Mono.just(true);
			} else if (status.equals(HttpStatus.CONFLICT)) {
				System.out.println("Carrinho teve alteração de valores");
				return Mono.just(false);
			} else {
				System.out.println("Erro: " + status);
				return Mono.just(false);
			}
		}).onErrorResume(erro -> {
			System.out.println("Erro ao processar a resposta: " + erro.getMessage());
			return Mono.just(false);
		});
	}
}
