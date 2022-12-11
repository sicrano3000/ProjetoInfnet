package br.com.jp.crud.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.jp.crud.entity.Produto;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

}
