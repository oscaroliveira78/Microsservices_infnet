package br.com.ms.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.ms.model.ProdutoRequest;
import br.com.ms.repository.ProdutoRequestRepository;

@Service
public class ProdutoRequestService {

	private final ProdutoRequestRepository produtoRequestRepository;

	@Autowired
	public ProdutoRequestService(ProdutoRequestRepository produtoRequestRepository) {
		
		this.produtoRequestRepository = produtoRequestRepository;
	}

	public void saveAll(List<ProdutoRequest> listaDeProdutos) {

		produtoRequestRepository.saveAll(listaDeProdutos);
	}

	public List<ProdutoRequest> findByUserId(String userId) {

		return produtoRequestRepository.findByUserId(userId);
	}

	@Transactional 
	public int deleteAllByUserId(String userId) {

		return produtoRequestRepository.deleteAllByUserId(userId);
	}

}
