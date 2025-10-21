package com.senai.jonatas.funcionarios.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public record FuncionarioRequest(
        @NotBlank(message = "Nome é obrigatório")
        @Size(min = 3, message = "Nome deve ter no mínimo 3 caracteres")
        String nome,

        @NotBlank(message = "E-mail é obrigatório")
        @Email(message = "E-mail inválido")
        String email,

        @NotBlank(message = "Cargo é obrigatório")
        String cargo,

        @NotNull(message = "Salário é obrigatório")
        @DecimalMin(value = "0.01", inclusive = true, message = "Salário deve ser maior que zero")
        BigDecimal salario,

        @NotNull(message = "Data de admissão é obrigatória")
        @PastOrPresent(message = "Data de admissão não pode ser futura")
        LocalDate dataAdmissao
) {
}
