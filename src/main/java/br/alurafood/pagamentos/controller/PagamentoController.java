package br.alurafood.pagamentos.controller;

import br.alurafood.pagamentos.dto.PagamentoDto;
import br.alurafood.pagamentos.service.PagamentoService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/pagamentos")
public class PagamentoController {

    @Autowired
    private PagamentoService pagamentoService;

    @GetMapping
    public ResponseEntity<Page<PagamentoDto>> obterTodos(@PageableDefault(size = 10)Pageable pageable){
        return ResponseEntity.ok(pagamentoService.obterTodos(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PagamentoDto> obterPorId(@PathVariable @NotNull Long id){
        PagamentoDto dto = pagamentoService.obterPorId(id);

        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<PagamentoDto> criarPagamento(@RequestBody @Valid PagamentoDto dto, UriComponentsBuilder uriBuilder){
        PagamentoDto pagamentoCriado = pagamentoService.criarPagamento(dto);
        URI endereco = uriBuilder.path("/pagamentos/{id}").buildAndExpand(pagamentoCriado.getId()).toUri();

        return ResponseEntity.created(endereco).body(pagamentoCriado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PagamentoDto> atualizarPagamento(@PathVariable @NotNull Long id, @RequestBody @Valid PagamentoDto dto){
        PagamentoDto pagamentoAtualizado = pagamentoService.atualizarPagamento(id, dto);

        return ResponseEntity.ok(pagamentoAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<PagamentoDto> excluirPagamento(@PathVariable @NotNull Long id){
        pagamentoService.excluirPagamento(id);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/confirmar")
    public void confirmarPagamento(@PathVariable @NotNull Long id){
        pagamentoService.confirmarPagamento(id);
    }
}
