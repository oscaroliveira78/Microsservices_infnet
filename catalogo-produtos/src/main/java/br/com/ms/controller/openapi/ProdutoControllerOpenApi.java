package br.com.ms.controller.openapi;

import java.security.Principal;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import br.com.ms.model.Produto;
import br.com.ms.model.ProdutoRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.Explode;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;

public interface ProdutoControllerOpenApi {

	@Operation(summary = "Buscar Produtos", description = "Busca todos os produtos registrados no DB")
	ResponseEntity<List<Produto>> buscarTodosProdutos();

	@Operation(summary = "Buscar produto por UUID", description = "Realiza a busca de um produto através do seu UUID", parameters = {
			@Parameter(name = "uuid", explode = Explode.TRUE, schema = @Schema(type = "String"), in = ParameterIn.PATH, description = "Id de indentificação da request") })
	ResponseEntity<Produto> buscarProdutoPorUuid(@PathVariable String uuid);

	@Operation(summary = "Adicionar ao carrinho", description = "Adiciona o(s) produto(s) ao carrinho", parameters = {
			@Parameter(name = "listaDeProdutos", explode = Explode.TRUE, schema = @Schema(type = "List"), in = ParameterIn.DEFAULT , description = "Lista de produtos") })
	String adicionarAoCarrinho(@RequestHeader("Authorization") String authorizationHeader, @RequestBody List<ProdutoRequest> listaDeProdutos, Principal principal);

	@Operation(summary = "Validar produtos do Carrinho", description = "Valida se os produtos continuam com o mesmo preço de quando foram adicionados ao carrinho", parameters = {
			@Parameter(name = "userId", explode = Explode.TRUE, schema = @Schema(type = "String"), in = ParameterIn.PATH, description = "Id do usuário") })
	ResponseEntity<?> validarDadosDoCarrinho(@RequestHeader("Authorization") String authorizationHeader, @PathVariable String userId);
	
	@Operation(summary = "Excluir carrinho do Usuário", description = "Remove o carrinho de um usua´rio especifico", parameters = {
			@Parameter(name = "userId", explode = Explode.TRUE, schema = @Schema(type = "String"), in = ParameterIn.PATH, description = "Id do usuário") })
	String removerCarrinho(@RequestHeader("Authorization") String authorizationHeader, @PathVariable String userId);
	
	@Operation(summary = "Busca o carrinho do usuário", description = "Retorna o carrinho de um usuário especifico", parameters = {
			@Parameter(name = "userId", explode = Explode.TRUE, schema = @Schema(type = "String"), in = ParameterIn.PATH, description = "Id do usuário") })
	ResponseEntity<List<ProdutoRequest>> buscarCarrinhodoUsuario(@RequestHeader("Authorization") String authorizationHeader, @PathVariable String userId);

}
