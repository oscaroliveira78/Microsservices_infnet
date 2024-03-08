package br.com.ms.services;

import org.springframework.http.HttpStatus;

public enum TabelaDeErros {

	ASSINATURA_INVALIDA(HttpStatus.UNAUTHORIZED, "Assinatura inválida."),

	TOKEN_INVALIDO(HttpStatus.UNAUTHORIZED, "Token Inválido."),

	TOKEN_EXPIRADO(HttpStatus.UNAUTHORIZED, "Token Expirado."),
	TOKEN_NAO_SUPORTADO(HttpStatus.UNAUTHORIZED, "Token nao Suportado."),

	TOKEN_CLAINS_NULL(HttpStatus.UNAUTHORIZED, "Token Claims é Nulo."),

	ERRO_GENERICO(HttpStatus.INTERNAL_SERVER_ERROR, "Erro genérico com a validação do Token.");

	private final HttpStatus codigoHttp;
	private final String mensagem;

	private TabelaDeErros(HttpStatus codigoHttp, String mensagem) {
		this.codigoHttp = codigoHttp;
		this.mensagem = mensagem;
	}

	public int getCodigoHttp() {
		return codigoHttp.value();
	}

	public String getMensagem() {
		return mensagem;
	}

}