package br.com.jp.pagamento.controller;

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

import br.com.jp.pagamento.data.vo.VendaVO;
import br.com.jp.pagamento.services.VendaService;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("/venda")
public class VendaController {
	
	private final VendaService vendaService;
	private final PagedResourcesAssembler<VendaVO> assembler;

	@Autowired
	public VendaController(VendaService vendaService, PagedResourcesAssembler<VendaVO> assembler) {
		this.vendaService = vendaService;
		this.assembler = assembler;
	}
	
	@GetMapping(value = "/{id}", produces = {"application/json", "application/xml", "application/x-yaml",})
	public VendaVO findById(@PathVariable("id") Long id) {
		VendaVO vendaVO = vendaService.findById(id);		
		vendaVO.add(linkTo(methodOn(VendaController.class).findById(id)).withSelfRel());
		
		log.info("Buscando Venda de id {}", id);
		
		return vendaVO;
	}
	
	@GetMapping(produces = {"application/json", "application/xml", "application/x-yaml",})
	public ResponseEntity<?> findAll(@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "limit", defaultValue = "12") int limit,
			@RequestParam(value = "direction", defaultValue = "asc") String direction) {
		var sorDirection = "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;
		
		Pageable pageable = PageRequest.of(page, limit, Sort.by(sorDirection, "data"));
		
		Page<VendaVO> vendas = vendaService.findAll(pageable);
		vendas.stream().forEach(p -> p.add(linkTo(methodOn(VendaController.class).findById(p.getId())).withSelfRel()));
		
		PagedModel<EntityModel<VendaVO>> pageModel = assembler.toModel(vendas);
		
		log.info("Buscando Todos as Vendas}");
		
		return new ResponseEntity<>(pageModel, HttpStatus.OK);
	}
	
	@PostMapping(produces = {"application/json", "application/xml", "application/x-yaml",}, 
				 consumes = {"application/json", "application/xml", "application/x-yaml",})
	public VendaVO create(@RequestBody VendaVO vendaVO) {
		VendaVO prodVO = vendaService.create(vendaVO);
		prodVO.add(linkTo(methodOn(VendaController.class).findById(prodVO.getId())).withSelfRel());
		
		log.info("Criando Venda - id {}", prodVO.getId());
		
		return prodVO;
	}
	
	@PutMapping(produces = {"application/json", "application/xml", "application/x-yaml",}, 
		    	consumes = {"application/json", "application/xml", "application/x-yaml",})
	public VendaVO update(@RequestBody VendaVO vendaVO) {
		VendaVO prodVO = vendaService.update(vendaVO);
		prodVO.add(linkTo(methodOn(VendaController.class).findById(prodVO.getId())).withSelfRel());
		
		log.info("Atualizando Venda - id {}", prodVO.getId());
		
		return prodVO;
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") Long id) {
		vendaService.delete(id);
		
		log.info("Deletando Venda de id {}", id);
		
		return ResponseEntity.ok().build();
	}

}
