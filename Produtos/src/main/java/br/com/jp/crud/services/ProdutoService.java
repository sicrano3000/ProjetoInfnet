package br.com.jp.crud.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.jp.crud.data.VO.ProdutoVO;
import br.com.jp.crud.entity.Produto;
import br.com.jp.crud.excpetion.ResourceNotFoundException;
import br.com.jp.crud.repository.ProdutoRepository;

@Service
public class ProdutoService {

	private ProdutoRepository repository;

	@Autowired
	public ProdutoService(ProdutoRepository repository) {
		this.repository = repository;
	}

	public ProdutoVO create(ProdutoVO produtoVO) {
		ProdutoVO produtoVORetorno = ProdutoVO.create(repository.save(Produto.create(produtoVO)));
		
		return produtoVORetorno;
	}
	
	public Page<ProdutoVO> findAll(Pageable pageable) {
		var page = repository.findAll(pageable);
		
		return page.map(this::convertToProdutoVO);
	}
	
	private ProdutoVO convertToProdutoVO(Produto produto) {
		return ProdutoVO.create(produto);
	}
	
	public ProdutoVO findById(Long id) {
		var entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));
		
		return ProdutoVO.create(entity);
	}
	
	public ProdutoVO update(ProdutoVO produtoVO) {
		final Optional<Produto> optionalProduto = repository.findById(produtoVO.getId());
		
		if (!optionalProduto.isPresent()) {
			new ResourceNotFoundException("No records found for this ID");
		}
		
		return ProdutoVO.create(repository.save(Produto.create(produtoVO)));
	}
	
	public void delete(Long id) {
		var entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));
		
		repository.delete(entity);
	}
	
}
