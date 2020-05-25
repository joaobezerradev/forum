package br.com.alura.forum.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.alura.forum.dto.TopicoDto;
import br.com.alura.forum.dto.TopicoForm;
import br.com.alura.forum.repository.CursoRepository;
import br.com.alura.forum.repository.TopicoRepository;

@RestController
@RequestMapping("/topicos")
public class TopicosController {

	@Autowired
	TopicoRepository topicoRepository;
	@Autowired
	CursoRepository cursoRepository;

	@GetMapping
	public ResponseEntity<List<TopicoDto>> index(@RequestParam(required = false) String nomeCurso) {
		if (nomeCurso != null) {
			var topico = topicoRepository.findByCursoNome(nomeCurso);
			var topicoDto = TopicoDto.converter(topico);
			return ResponseEntity.ok(topicoDto);
		} else {
			var topico = topicoRepository.findAll();
			var topicoDto = TopicoDto.converter(topico);
			return ResponseEntity.ok(topicoDto);
		}

	}

	@PostMapping
	public ResponseEntity<TopicoDto> store(@RequestBody TopicoForm form, UriComponentsBuilder uriBuilder) {
		var topicoForm = form.converter(cursoRepository);
		var topico = topicoRepository.save(topicoForm);
		var uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
		return ResponseEntity.created(uri).body(new TopicoDto(topico));
	}
}
