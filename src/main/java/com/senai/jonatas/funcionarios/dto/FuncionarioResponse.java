package com.senai.jonatas.funcionarios.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record FuncionarioResponse(
        Long id,
        String nome,
        String email,
        String cargo,
        BigDecimal salario,
        LocalDate dataAdmissao,
        Boolean ativo,
        Long departamentoId,
        String departamentoNome,
        String departamentoSigla
) {}
