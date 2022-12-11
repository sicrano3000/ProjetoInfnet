package br.com.jp.pagamento.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.jp.pagamento.data.vo.VendaVO;
import br.com.jp.pagamento.entity.ProdutoVenda;
import br.com.jp.pagamento.entity.Venda;
import br.com.jp.pagamento.exception.ResourceNotFoundException;
import br.com.jp.pagamento.repository.ProdutoVendaRepository;
import br.com.jp.pagamento.repository.VendaRepository;

@Service
public class VendaService {
	
	private final VendaRepository repository;
	private final ProdutoVendaRepository produtoVendaRepository;

	@Autowired
	public VendaService(VendaRepository repository, ProdutoVendaRepository produtoVendaRepository) {
		this.repository = repository;
		this.produtoVendaRepository = produtoVendaRepository;
	}

	public VendaVO create(VendaVO vendaVO) {
		Venda venda =repository.save(Venda.create(vendaVO));
		
		List<ProdutoVenda> produtosSalvos = new ArrayList<>();
		
		vendaVO.getProdutos().forEach(p -> {
			ProdutoVenda pv = ProdutoVenda.create(p);
			pv.setVenda(venda);
			produtosSalvos.add(produtoVendaRepository.save(pv));
		});
		
		venda.setProdutos(produtosSalvos);
		
		return VendaVO.create(venda);
	}
	
	public Page<VendaVO> findAll(Pageable pageable) {
		var page = repository.findAll(pageable);
		
		return page.map(this::convertToVendaVO);
	}
	
	private VendaVO convertToVendaVO(Venda venda) {
		return VendaVO.create(venda);
	}
	
	public VendaVO findById(Long id) {
		var entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));
		
		return VendaVO.create(entity);
	}
	
	public VendaVO update(VendaVO vendaVO) {
		final Optional<Venda> optionalProduto = repository.findById(vendaVO.getId());
		
		if (!optionalProduto.isPresent()) {
			new ResourceNotFoundException("No records found for this ID");
		}
		
		List<ProdutoVenda> produtosAtualizados = new ArrayList<>();
		
		if (!vendaVO.getProdutos().isEmpty() ) {
			vendaVO.getProdutos().forEach(p -> {
				ProdutoVenda pv = ProdutoVenda.create(p);
				pv.setVenda(optionalProduto.get());
				produtosAtualizados.add(produtoVendaRepository.save(pv));
			});
		}
		
		optionalProduto.get().setProdutos(produtosAtualizados);
		 
		return VendaVO.create(repository.save(Venda.create(vendaVO)));
	}
	
	public void delete(Long id) {
		var entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));
		
		entity.getProdutos().forEach(p -> {
			deleteProdutos(p.getId());
		});
		
		repository.delete(entity);
	}
	
	public void deleteProdutos(Long id) {
		var entity = produtoVendaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));
		
		produtoVendaRepository.delete(entity);
	}

}
