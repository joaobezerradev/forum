package br.com.alura.forum.dto;

import java.time.LocalDateTime;

import br.com.alura.forum.model.Resposta;
import lombok.Getter;

@Getter
public class RespostasDto {
	private Long id;
	private String mensagem;
	private LocalDateTime dataCriacao;
	private String nomeAutor;
	
	public RespostasDto(Resposta resposta) {
		this.id = resposta.getId();
		this.mensagem = resposta.getMensagem();
		this.dataCriacao = resposta.getDataCriacao();
		this.nomeAutor = resposta.getAutor().getNome();
	}
}
