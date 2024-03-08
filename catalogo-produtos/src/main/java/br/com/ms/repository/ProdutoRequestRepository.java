package br.com.ms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.ms.model.ProdutoRequest;

@Repository
public interface ProdutoRequestRepository extends JpaRepository<ProdutoRequest, Long> {

	int deleteAllByUserId(String userId);

	List<ProdutoRequest> findByUserId(String userId);

}
