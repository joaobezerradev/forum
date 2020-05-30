package br.com.alura.forum.controller;

import static org.springframework.data.domain.Sort.Direction.ASC;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
	public ResponseEntity<Page<TopicoDto>> index(@RequestParam(required = false) final String nomeCurso,
			@PageableDefault(sort = "id", direction = ASC) final Pageable paginacao) {
		if (nomeCurso != null) {
			final var topico = topicoRepository.findByCursoNome(nomeCurso, paginacao);
			final var topicoDto = TopicoDto.converter(topico);
			return ResponseEntity.ok(topicoDto);
		} else {
			final var topico = topicoRepository.findAll(paginacao);
			final var topicoDto = TopicoDto.converter(topico);
			return ResponseEntity.ok(topicoDto);
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<DetalhesTopicoDto> show(@PathVariable final Long id) {
		final var topico = topicoRepository.findById(id);

		if (topico.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		final var detalheTopicoDto = new DetalhesTopicoDto(topico.get());
		return ResponseEntity.ok(detalheTopicoDto);
	}

	@Transactional
	@PostMapping
	public ResponseEntity<TopicoDto> store(@Validated @RequestBody final TopicoForm form,
			final UriComponentsBuilder uriBuilder) {
		final var topicoForm = form.converter(cursoRepository);
		final var topico = topicoRepository.save(topicoForm);
		final var uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
		return ResponseEntity.created(uri).body(new TopicoDto(topico));
	}

	@Transactional
	@PutMapping("/{id}")
	public ResponseEntity<TopicoDto> update(@PathVariable final Long id,
			@Validated @RequestBody final AtualizacaoTopicoForm form) {
		final var topico = topicoRepository.findById(id);

		if (topico.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		final var topicoDto = form.atualizar(id, topicoRepository);
		return ResponseEntity.ok(new TopicoDto(topicoDto));
	}

	@Transactional
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable final Long id) {
		final var topico = topicoRepository.findById(id);

		if (topico.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		topicoRepository.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
