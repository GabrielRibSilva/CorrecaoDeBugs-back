package com.senai.jonatas.funcionarios.repository;

import com.senai.jonatas.funcionarios.entity.Departamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DepartamentoRepository extends JpaRepository<Departamento, Long> {

    boolean existsByNomeIgnoreCase(String nome);

    Optional<Departamento> findByNomeIgnoreCase(String nome);

    List<Departamento> findByAtivoOrderByNomeAsc(Boolean ativo);

    List<Departamento> findAllByOrderByNomeAsc();
}