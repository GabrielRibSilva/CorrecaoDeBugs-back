package com.senai.jonatas.funcionarios.dto;

public record DepartamentoResponse(
        Long id,
        String nome,
        String sigla,
        Boolean ativo
) {
}