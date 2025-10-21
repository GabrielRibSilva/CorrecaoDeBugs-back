package com.senai.jonatas.funcionarios.controller;

import com.senai.jonatas.funcionarios.dto.FuncionarioRequest;
import com.senai.jonatas.funcionarios.dto.FuncionarioResponse;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/funcionarios")
public class FuncionarioController {

    private final FuncionarioService service;

    // GET /api/funcionarios?cargo=Analista&ativo=true
    @GetMapping
    public ResponseEntity<List<FuncionarioResponse>> listar(
            @RequestParam(required = false) String cargo,
            @RequestParam(required = false) Boolean ativo) {
        var lista = service.listar(cargo, ativo);
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FuncionarioResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<FuncionarioResponse> cadastrar(@Valid @RequestBody FuncionarioRequest request,
                                                         UriComponentsBuilder uriBuilder) {
        var result = service.cadastrar(request);
        if (result.created()) {
            var location = uriBuilder.path("/api/funcionarios/{id}")
                    .buildAndExpand(result.body().id()).toUri();
            return ResponseEntity.created(location).body(result.body()); // 201 Created
        }
        // Reativado: 200 OK
        return ResponseEntity.ok(result.body());
    }

    @PutMapping("/{id}")
    public ResponseEntity<FuncionarioResponse> atualizar(@PathVariable Long id,
                                                         @Valid @RequestBody FuncionarioRequest request) {
        return ResponseEntity.ok(service.atualizar(id, request));
    }

    @PatchMapping("/{id}/inativar")
    public ResponseEntity<FuncionarioResponse> inativar(@PathVariable Long id) {
        return ResponseEntity.ok(service.inativar(id));
    }
}
