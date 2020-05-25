package br.com.alura.forum.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.alura.forum.dto.AtualizacaoTopicoForm;
import br.com.alura.forum.dto.DetalhesTopicoDto;
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

	@GetMapping("/{id}")
	public ResponseEntity<DetalhesTopicoDto> show(@PathVariable Long id) {
		var topico = topicoRepository.findById(id);

		if (topico.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		var detalheTopicoDto = new DetalhesTopicoDto(topico.get());
		return ResponseEntity.ok(detalheTopicoDto);
	}

	@Transactional
	@PostMapping
	public ResponseEntity<TopicoDto> store(@Validated @RequestBody TopicoForm form, UriComponentsBuilder uriBuilder) {
		var topicoForm = form.converter(cursoRepository);
		var topico = topicoRepository.save(topicoForm);
		var uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
		return ResponseEntity.created(uri).body(new TopicoDto(topico));
	}

	@Transactional
	@PutMapping("/{id}")
	public ResponseEntity<TopicoDto> update(@PathVariable Long id, @Validated @RequestBody AtualizacaoTopicoForm form) {
		var topico = topicoRepository.findById(id);

		if (topico.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		var topicoDto = form.atualizar(id, topicoRepository);
		return ResponseEntity.ok(new TopicoDto(topicoDto));
	}

	@Transactional
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		var topico = topicoRepository.findById(id);

		if (topico.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		topicoRepository.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
