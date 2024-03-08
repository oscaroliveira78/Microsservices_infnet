package br.com.ms.model.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Data;

@Data
public class ProdutoResponse implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private BigDecimal preco;
	private String uuid;
	private int qtde;
	private LocalDate data;
	private String userId;

}
