package br.com.ms.model;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentoOrder {

	private String nomeTitular;
	private String numeroCartao;
	private String dataExpiracao;
	private int codigoSeguranca;
	private String bandeira;
	private BigDecimal valor;
	private String emailUser;
	private String orderUuid;

}
