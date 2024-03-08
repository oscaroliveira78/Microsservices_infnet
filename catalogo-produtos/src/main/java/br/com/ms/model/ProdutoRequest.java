package br.com.ms.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "produtosRequest")
@Data
@EqualsAndHashCode(of = { "uuid" })
@NoArgsConstructor
@AllArgsConstructor
public class ProdutoRequest implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private BigDecimal preco;
	private String uuid;
	private int qtde;
	private LocalDate data = LocalDate.now();

	// gerado um UUID baseado no nome do usuario
	private String userId;

	public static ProdutoRequest fromString(String encodedString) {

		String[] parts = encodedString.split("\\|");
		ProdutoRequest produtoRequest = new ProdutoRequest();
		produtoRequest.setUuid(String.valueOf(parts[0]));
		produtoRequest.setPreco(new BigDecimal(parts[1]));
		produtoRequest.setQtde(Integer.parseInt(parts[2]));
		return produtoRequest;
	}
}
