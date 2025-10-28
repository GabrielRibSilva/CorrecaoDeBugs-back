package com.senai.jonatas.funcionarios.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DepartamentoRequest(
        @NotBlank(message = "Nome é obrigatório")
        @Size(min = 3, message = "Nome deve ter no mínimo 3 caracteres")
        String nome,

        @NotBlank(message = "Sigla é obrigatória")
        @Size(max = 10, message = "Sigla não pode exceder 10 caracteres")
        String sigla
) {
}