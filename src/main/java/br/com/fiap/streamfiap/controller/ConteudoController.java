package br.com.fiap.streamfiap.controller;

import br.com.fiap.streamfiap.exception.ConteudoNaoEncontradoException;
import br.com.fiap.streamfiap.model.Conteudo;
import br.com.fiap.streamfiap.model.Documentario;
import br.com.fiap.streamfiap.model.Filme;
import br.com.fiap.streamfiap.model.Serie;
import br.com.fiap.streamfiap.repository.ConteudoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/conteudos")
public class ConteudoController {

    @Autowired
    private ConteudoRepository conteudoRepository;

    // GET /api/conteudos - Listar todos
    @GetMapping
    public List<Conteudo> listarTodos() {
        return conteudoRepository.findAll();
    }

    // BUG 10 CORRIGIDO: Sem try/catch e retornando ResponseEntity
    @GetMapping("/{id}")
    public ResponseEntity<Conteudo> buscarPorId(@PathVariable Long id) {
        Conteudo conteudo = conteudoRepository.findById(id)
                .orElseThrow(() -> new ConteudoNaoEncontradoException("Conteúdo não encontrado: " + id));

        return ResponseEntity.ok(conteudo);
    }

    // BUG 11 CORRIGIDO: Usando findByCategoria() do Spring Data
    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<Conteudo>> listarPorCategoria(@PathVariable String categoria) {
        List<Conteudo> filtrados = conteudoRepository.findByCategoria(categoria);
        return ResponseEntity.ok(filtrados);
    }

    // GET /api/conteudos/{id}/preco-promocional - Preço com promoção
    @GetMapping("/{id}/preco-promocional")
    public double precoPromocional(@PathVariable Long id) {
        Conteudo conteudo = conteudoRepository.findById(id)
                .orElseThrow(() -> new ConteudoNaoEncontradoException("Conteúdo não encontrado: " + id));
        return conteudo.calcularPrecoPromocional();
    }

    // POST /api/conteudos/filme - cadastra um filme
    @PostMapping("/filme")
    public ResponseEntity<Filme> cadastrarFilme(@RequestBody Filme filme) {
        // CORREÇÃO: filme.getDuracaoMinutos() em vez de filme.duracaoMinutos
        Filme novo = new Filme(filme.getTitulo(), filme.getCategoria(), filme.getDuracaoMinutos(),
                filme.getClassificacaoEtaria(), filme.isDisponivel(), filme.isEstreia());
        return ResponseEntity.status(201).body(conteudoRepository.save(novo));
    }

    // POST /api/conteudos/serie - cadastra uma série
    @PostMapping("/serie")
    public ResponseEntity<Serie> cadastrarSerie(@RequestBody Serie serie) {
        // CORREÇÃO: serie.getDuracaoMinutos() e serie.isDisponivel()
        Serie nova = new Serie(serie.getTitulo(), serie.getCategoria(), serie.getDuracaoMinutos(),
                serie.getClassificacaoEtaria(), serie.isDisponivel(), serie.getNumeroTemporadas());
        return ResponseEntity.status(201).body(conteudoRepository.save(nova));
    }

    // POST /api/conteudos/documentario - cadastra um documentário
    @PostMapping("/documentario")
    public ResponseEntity<Documentario> cadastrarDocumentario(@RequestBody Documentario documentario) {
        // CORREÇÃO: documentario.getDuracaoMinutos() em vez de documentario.duracaoMinutos
        Documentario novo = new Documentario(documentario.getTitulo(), documentario.getCategoria(),
                documentario.getDuracaoMinutos(), documentario.getClassificacaoEtaria(),
                documentario.isDisponivel(), documentario.getTema());
        return ResponseEntity.status(201).body(conteudoRepository.save(novo));
    }
}