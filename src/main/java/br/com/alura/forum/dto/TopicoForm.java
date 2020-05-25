package br.com.alura.forum.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import br.com.alura.forum.model.Topico;
import br.com.alura.forum.repository.CursoRepository;
import lombok.Getter;

@Getter
public class TopicoForm {
	@NotNull
	@NotBlank
	@Length(min = 5)
	private String titulo;
	@NotNull
	@NotBlank
	@Length(min = 10)
	private String mensagem;
	private String nomeCurso;

	public Topico converter(CursoRepository cursoRepository) {
		var curso = cursoRepository.findByNome(nomeCurso);
		return new Topico(titulo, mensagem, curso);

	}
}
