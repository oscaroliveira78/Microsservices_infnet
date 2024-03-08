package br.com.ms.model.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class DadosPagamento {

	private String nomeTitular;
	private String numeroCartao;
	private String dataExpiracao;
	private int codigoSeguranca;
	private String bandeira;
	private BigDecimal valor;
	private String emailUser;
	private String orderUuid;

}