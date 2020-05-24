package br.com.alura.forum.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.alura.forum.model.Topico;
import br.com.alura.forum.repository.TopicoRepository;

@RestController
@RequestMapping("/topicos")
public class TopicosController {

	@Autowired
	TopicoRepository topicoRepository;

	@GetMapping
	public ResponseEntity<List<Topico>> index(@RequestParam(required = false) String nomeCurso) {
		List<Topico> topico;
		if (nomeCurso != null) {
			topico = topicoRepository.findByCursoNome(nomeCurso);
		} else {
			topico = topicoRepository.findAll();
		}
		return ResponseEntity.ok(topico);
	}
}
