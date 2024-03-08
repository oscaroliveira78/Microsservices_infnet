package br.com.ms.model;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentStatus {

	private String orderUuid;

	@Enumerated(value = EnumType.STRING)
	private StatusPurchase status;
}
