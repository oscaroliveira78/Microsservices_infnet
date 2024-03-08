package br.com.ms.service;

import org.springframework.stereotype.Service;

import br.com.ms.repository.ShoppingCartRepository;

@Service
public class ShoppingCartService {

	private final ShoppingCartRepository shoppingCartRepository;

	public ShoppingCartService(ShoppingCartRepository shoppingCartRepository) {
		this.shoppingCartRepository = shoppingCartRepository;
	}

}
