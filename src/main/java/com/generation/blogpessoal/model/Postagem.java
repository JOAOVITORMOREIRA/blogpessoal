package com.generation.blogpessoal.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "tb_postagens")
public class Postagem {
	
	@Id // O atributo é a chave primaria
	@GeneratedValue(strategy = GenerationType.IDENTITY) // auto incremente
	private Long id;
	
	@NotBlank(message = "O Atributo título é obrigatório") //notblank impede espaço em branco | ele é exclusivo para String
	@Size(min = 5, max = 100, message = "O atributo título deve ter no minimo 05 e no maximo 100 caracteres") // size limita o tamanho
	private String titulo;
	
	@NotBlank(message = "O Atributo texto é obrigatório")
	@Size(min = 10, max = 1000, message = "O atributo texto deve ter no minimo 10 e no maximo 1000 caracteres")
	private String texto;
	
	@UpdateTimestamp
	private LocalDateTime data;
	
	//Relacionamento
	@ManyToOne
	@JsonIgnoreProperties("postagem")
	private Tema tema;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public LocalDateTime getData() {
		return data;
	}

	public void setData(LocalDateTime data) {
		this.data = data;
	}
	//Criar os metodos get e set do obj tema

	public Tema getTema() {
		return tema;
	}

	public void setTema(Tema tema) {
		this.tema = tema;
	}
	
}
