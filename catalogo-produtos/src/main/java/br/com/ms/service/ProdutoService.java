package br.com.ms.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.ms.model.Produto;
import br.com.ms.repository.ProdutoRepository;

@Service
public class ProdutoService {

	private final ProdutoRepository produtoRepository;

	@Autowired
	public ProdutoService(ProdutoRepository produtoRepository) {
		this.produtoRepository = produtoRepository;
	}

	public List<Produto> findAll() {
		return produtoRepository.findAll();
	}

	public Optional<Produto> findByUuid(String uuid) {

		return produtoRepository.findByUuid(uuid);
	}

	public void save(Produto produto) {

		produtoRepository.save(produto);
	}
}