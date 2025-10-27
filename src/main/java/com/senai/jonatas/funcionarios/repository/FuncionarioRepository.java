package com.senai.jonatas.funcionarios.repository;

import com.senai.jonatas.funcionarios.entity.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {

    Optional<Funcionario> findByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCase(String email);

    List<Funcionario> findByCargoIgnoreCaseContainingOrderByNomeAsc(String cargo);

    List<Funcionario> findByAtivoOrderByNomeAsc(Boolean ativo);

    List<Funcionario> findByCargoIgnoreCaseContainingAndAtivoOrderByNomeAsc(String cargo, Boolean ativo);

    List<Funcionario> findAllByOrderByNomeAsc();
}
