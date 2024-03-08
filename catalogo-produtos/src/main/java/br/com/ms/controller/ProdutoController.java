package br.com.ms.controller;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.ms.controller.openapi.ProdutoControllerOpenApi;
import br.com.ms.model.Produto;
import br.com.ms.model.ProdutoRequest;
import br.com.ms.service.ProdutoRequestService;
import br.com.ms.service.ProdutoService;

@RestController
@RequestMapping("/produtosg")
@CrossOrigin
public class ProdutoController implements ProdutoControllerOpenApi {

	private final ProdutoService produtoService;
	private final ProdutoRequestService produtoRequestService;

	public ProdutoController(ProdutoService produtoService, ProdutoRequestService produtoRequestService) {
		this.produtoService = produtoService;
		this.produtoRequestService = produtoRequestService;
	}

	@Override
	@GetMapping
	public ResponseEntity<List<Produto>> buscarTodosProdutos() {

		List<Produto> produtos = produtoService.findAll();
		return ResponseEntity.ok(produtos);
	}

	@Override
	@GetMapping("/{uuid}")
	public ResponseEntity<Produto> buscarProdutoPorUuid(@PathVariable String uuid) {

		Optional<Produto> produto = produtoService.findByUuid(uuid);
		return produto.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
	}

	@Override
	@PostMapping("/addcart")
	public String adicionarAoCarrinho(@RequestHeader("Authorization") String authorizationHeader, @RequestBody List<ProdutoRequest> listaDeProdutos,
			Principal principal) {

		try {
			for (ProdutoRequest produtoRequest : listaDeProdutos) {

				produtoRequest.setUserId(principal.getName());

				// Realizando a Saida de Produtos
				Optional<Produto> produto = produtoService.findByUuid(produtoRequest.getUuid());
				if (produto.isPresent()) {
					Produto prod = produto.get();
					prod.setEstoque(prod.getEstoque() - produtoRequest.getQtde());
					produtoService.save(prod);
				} else {
					System.out.println("Erros encontrados ao localizar produto e realizar a baixa em estoque");
				}
			}
			produtoRequestService.saveAll(listaDeProdutos);
			return "Produto adicionado ao carrinho com sucesso!";

		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return "Erro";

	}

	@Override
	@GetMapping("/validatecart/{userId}")
	public ResponseEntity<?> validarDadosDoCarrinho(@RequestHeader("Authorization") String authorizationHeader, @PathVariable String userId) {

		List<ProdutoRequest> carrinho = produtoRequestService.findByUserId(userId);

		try {
			if (!carrinho.isEmpty()) {

				for (ProdutoRequest produtoRequest : carrinho) {
					Optional<Produto> produto = produtoService.findByUuid(produtoRequest.getUuid());
					if (produto.isPresent() && produto.get().getPreco().compareTo(produtoRequest.getPreco()) != 0)
						return ResponseEntity.status(HttpStatus.CONFLICT).body("Carrinho teve alteração de valores");
				}
			} else {
				return ResponseEntity.status(HttpStatus.CONFLICT).body("Carrinho esta Vazio");
			}
		} catch (Exception e) {
			System.out.println(e.getLocalizedMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erros");
		}

		return ResponseEntity.status(HttpStatus.OK).body("Carrinho aprovado");
	}

	@Override
	@DeleteMapping("/{userId}")
	public String removerCarrinho(@RequestHeader("Authorization") String authorizationHeader, @PathVariable String userId) {

		System.out.println("Carrinho Removido");
		int qtdeDeletada = produtoRequestService.deleteAllByUserId(userId);
		return "Carinho removido com Sucesso " + qtdeDeletada;
	}

	@Override
	@GetMapping("/getcart/{userId}")
	public ResponseEntity<List<ProdutoRequest>> buscarCarrinhodoUsuario(@RequestHeader("Authorization") String authorizationHeader,
			@PathVariable String userId) {

		List<ProdutoRequest> findByUserId = produtoRequestService.findByUserId(userId);
		return ResponseEntity.ok(findByUserId);
	}

}