package br.com.alura.forum.dto;

import br.com.alura.forum.model.Topico;
import br.com.alura.forum.repository.CursoRepository;
import lombok.Data;

@Data
public class TopicoForm {
	private String titulo;
	private String mensagem;
	private String nomeCurso;

	public Topico converter(CursoRepository cursoRepository) {
		var curso = cursoRepository.findByNome(nomeCurso);
		return new Topico(titulo, mensagem, curso);

	}
}
