package br.com.ms.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.ms.model.PurchaseOrder;
import br.com.ms.repository.PurchaseOrderRepository;

@Service
public class PurchaseOrderService {

	private final PurchaseOrderRepository purchaseOrderRepository;

	public PurchaseOrderService(PurchaseOrderRepository purchaseOrderRepository) {
		this.purchaseOrderRepository = purchaseOrderRepository;
	}

	public PurchaseOrder save(PurchaseOrder pO) {

		return purchaseOrderRepository.save(pO);

	}

	public PurchaseOrder findByOrderUuid(String orderUuid) {

		return purchaseOrderRepository.findByOrderUuid(orderUuid);

	}

	public List<PurchaseOrder> findAll() {

		return purchaseOrderRepository.findAll();
	}

}
