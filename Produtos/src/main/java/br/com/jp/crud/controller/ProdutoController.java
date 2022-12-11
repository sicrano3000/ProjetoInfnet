package br.com.jp.crud.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.jp.crud.data.VO.ProdutoVO;
import br.com.jp.crud.services.ProdutoService;

@RestController
@RequestMapping("/produto")
public class ProdutoController {
	
	private final ProdutoService produtoService;
	private final PagedResourcesAssembler<ProdutoVO> assembler;

	@Autowired
	public ProdutoController(ProdutoService produtoService, PagedResourcesAssembler<ProdutoVO> assembler) {
		this.produtoService = produtoService;
		this.assembler = assembler;
	}
	
	@GetMapping(value = "/{id}", produces = {"application/json", "application/xml", "application/x-yaml",})
	public ProdutoVO findById(@PathVariable("id") Long id) {
		ProdutoVO produtoVO = produtoService.findById(id);		
		produtoVO.add(linkTo(methodOn(ProdutoController.class).findById(id)).withSelfRel());
		
		return produtoVO;
	}
	
	@GetMapping(produces = {"application/json", "application/xml", "application/x-yaml",})
	public ResponseEntity<?> findAll(@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "limit", defaultValue = "12") int limit,
			@RequestParam(value = "direction", defaultValue = "asc") String direction) {
		var sorDirection = "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;
		
		Pageable pageable = PageRequest.of(page, limit, Sort.by(sorDirection, "nome"));
		
		Page<ProdutoVO> produtos = produtoService.findAll(pageable);
		produtos.stream().forEach(p -> p.add(linkTo(methodOn(ProdutoController.class).findById(p.getId())).withSelfRel()));
		
		PagedModel<EntityModel<ProdutoVO>> pageModel = assembler.toModel(produtos);
		
		return new ResponseEntity<>(pageModel, HttpStatus.OK);
	}
	
	@PostMapping(produces = {"application/json", "application/xml", "application/x-yaml",}, 
				 consumes = {"application/json", "application/xml", "application/x-yaml",})
	public ProdutoVO create(@RequestBody ProdutoVO produtoVO) {
		ProdutoVO prodVO = produtoService.create(produtoVO);
		prodVO.add(linkTo(methodOn(ProdutoController.class).findById(prodVO.getId())).withSelfRel());
		
		return prodVO;
	}
	
	@PutMapping(produces = {"application/json", "application/xml", "application/x-yaml",}, 
			    consumes = {"application/json", "application/xml", "application/x-yaml",})
	public ProdutoVO update(@RequestBody ProdutoVO produtoVO) {
		ProdutoVO prodVO = produtoService.update(produtoVO);
		prodVO.add(linkTo(methodOn(ProdutoController.class).findById(prodVO.getId())).withSelfRel());
		
		return prodVO;
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") Long id) {
		produtoService.delete(id);
		
		return ResponseEntity.ok().build();
	}

}
