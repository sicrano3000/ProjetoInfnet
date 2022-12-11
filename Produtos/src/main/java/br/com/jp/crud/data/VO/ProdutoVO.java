package br.com.jp.crud.data.VO;

import java.io.Serializable;

import org.modelmapper.ModelMapper;
import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import br.com.jp.crud.entity.Produto;

@JsonPropertyOrder({"id", "nome", "estoque", "preco"})
public class ProdutoVO extends RepresentationModel<ProdutoVO> implements Serializable{
	
	private static final long serialVersionUID = 4259278263424287193L;

	@JsonProperty("id")
	private Long id;
	
	@JsonProperty("nome")
	private String nome;
	
	@JsonProperty("estoque")
	private Integer estoque;
	
	@JsonProperty("preco")
	private Double preco;
	
	public static ProdutoVO create(Produto produto) {
		return new ModelMapper().map(produto, ProdutoVO.class);
	} 
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}

	public Integer getEstoque() {
		return estoque;
	}
	public void setEstoque(Integer estoque) {
		this.estoque = estoque;
	}

	public Double getPreco() {
		return preco;
	}
	public void setPreco(Double preco) {
		this.preco = preco;
	}

	public ProdutoVO() {
	}

	public ProdutoVO(Long id, String nome, Integer estoque, Double preco) {
		this.id = id;
		this.nome = nome;
		this.estoque = estoque;
		this.preco = preco;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((estoque == null) ? 0 : estoque.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		result = prime * result + ((preco == null) ? 0 : preco.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProdutoVO other = (ProdutoVO) obj;
		if (estoque == null) {
			if (other.estoque != null)
				return false;
		} else if (!estoque.equals(other.estoque))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		if (preco == null) {
			if (other.preco != null)
				return false;
		} else if (!preco.equals(other.preco))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ProdutoVO [id=" + id + ", nome=" + nome + ", estoque=" + estoque + ", preco=" + preco + "]";
	}
	
}
