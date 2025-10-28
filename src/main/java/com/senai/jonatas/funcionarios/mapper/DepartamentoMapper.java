package com.senai.jonatas.funcionarios.mapper;

import com.senai.jonatas.funcionarios.dto.DepartamentoRequest;
import com.senai.jonatas.funcionarios.dto.DepartamentoResponse;
import com.senai.jonatas.funcionarios.entity.Departamento;

public final class DepartamentoMapper {

    private DepartamentoMapper() {}

    public static Departamento toEntity(DepartamentoRequest req) {
        return Departamento.builder()
                .nome(req.nome())
                .sigla(req.sigla())
                .ativo(true) // Default ao criar
                .build();
    }

    public static void updateEntityFromRequest(DepartamentoRequest req, Departamento entity) {
        entity.setNome(req.nome());
        entity.setSigla(req.sigla());
        // O status 'ativo' é gerenciado por endpoint específico (inativar)
    }

    public static DepartamentoResponse toResponse(Departamento entity) {
        return new DepartamentoResponse(
                entity.getId(),
                entity.getNome(),
                entity.getSigla(),
                entity.getAtivo()
        );
    }
}