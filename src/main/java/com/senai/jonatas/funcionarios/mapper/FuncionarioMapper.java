package com.senai.jonatas.funcionarios.mapper;


import com.senai.jonatas.funcionarios.dto.FuncionarioRequest;
import com.senai.jonatas.funcionarios.dto.FuncionarioResponse;
import com.senai.jonatas.funcionarios.entity.Funcionario;

public final class FuncionarioMapper {

    private FuncionarioMapper() {}

    public static Funcionario toEntity(FuncionarioRequest req) {
        return Funcionario.builder()
                .nome(req.nome())
                .email(req.email())
                .cargo(req.cargo())
                .salario(req.salario())
                .dataAdmissao(req.dataAdmissao())
                .ativo(true)
                .build();
    }

    public static void copyToEntity(FuncionarioRequest req, Funcionario entity) {
        entity.setNome(req.nome());
        entity.setCargo(req.cargo());
        entity.setSalario(req.salario());
        entity.setDataAdmissao(req.dataAdmissao());
        // email só atualiza se for alterado; regra é tratada no service
        entity.setEmail(req.email());
    }

    public static FuncionarioResponse toResponse(Funcionario e) {
        return new FuncionarioResponse(
                e.getId(), e.getNome(), e.getEmail(),
                e.getCargo(), e.getSalario(), e.getDataAdmissao(), e.getAtivo()
        );
    }
}
