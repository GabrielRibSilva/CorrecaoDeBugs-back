package com.senai.jonatas.funcionarios.service;

import com.senai.jonatas.funcionarios.dto.DepartamentoRequest;
import com.senai.jonatas.funcionarios.dto.DepartamentoResponse;
import com.senai.jonatas.funcionarios.entity.Departamento;
import com.senai.jonatas.funcionarios.exceptions.BusinessException;
import com.senai.jonatas.funcionarios.exceptions.NomeConflictException;
import com.senai.jonatas.funcionarios.exceptions.ResourceNotFoundException;
import com.senai.jonatas.funcionarios.mapper.DepartamentoMapper;
import com.senai.jonatas.funcionarios.repository.DepartamentoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartamentoService {

    private final DepartamentoRepository repository;

    public DepartamentoService(DepartamentoRepository repository) {
        this.repository = repository;
    }

    public List<DepartamentoResponse> listarTodos() {
        return repository.findAllByOrderByNomeAsc().stream()
                .map(DepartamentoMapper::toResponse)
                .toList();
    }

    public List<DepartamentoResponse> listarAtivos() {
        return repository.findByAtivoOrderByNomeAsc(true).stream()
                .map(DepartamentoMapper::toResponse)
                .toList();
    }

    public DepartamentoResponse buscarPorId(Long id) {
        Departamento depto = findDepartamentoById(id);
        return DepartamentoMapper.toResponse(depto);
    }

    @Transactional
    public DepartamentoResponse cadastrar(DepartamentoRequest req) {
        validarNomeUnico(req.nome(), null);
        Departamento novo = DepartamentoMapper.toEntity(req);
        Departamento salvo = repository.save(novo);
        return DepartamentoMapper.toResponse(salvo);
    }

    @Transactional
    public DepartamentoResponse atualizar(Long id, DepartamentoRequest req) {
        Departamento existente = findDepartamentoById(id);
        validarNomeUnico(req.nome(), id); // Verifica se o novo nome já existe em outro ID
        DepartamentoMapper.updateEntityFromRequest(req, existente);
        Departamento salvo = repository.save(existente);
        return DepartamentoMapper.toResponse(salvo);
    }

    @Transactional
    public DepartamentoResponse inativar(Long id) {
        Departamento existente = findDepartamentoById(id);
        if (!existente.getAtivo()) {
            throw new BusinessException("Departamento já está inativo");
        }
        // Regra: Não permitir inativar se houver funcionários ATIVOS associados?
        // (Opcional, não especificado no requisito, mas comum)
        // if (existente.getFuncionarios().stream().anyMatch(Funcionario::getAtivo)) {
        //     throw new BusinessException("Não é possível inativar departamento com funcionários ativos.");
        // }
        existente.setAtivo(false);
        Departamento salvo = repository.save(existente);
        return DepartamentoMapper.toResponse(salvo);
    }

    // Método utilitário para buscar e lançar exceção se não encontrado
    protected Departamento findDepartamentoById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Departamento não encontrado com ID: " + id));
    }

    private void validarNomeUnico(String nome, Long idExcluir) {
        repository.findByNomeIgnoreCase(nome).ifPresent(depto -> {
            if (idExcluir == null || !depto.getId().equals(idExcluir)) {
                throw new NomeConflictException("Nome de departamento já cadastrado: " + nome);
            }
        });
        // Validação adicional caso não queira depender só do banco
        if (repository.existsByNomeIgnoreCase(nome) && (idExcluir == null || repository.findByNomeIgnoreCase(nome).map(d -> !d.getId().equals(idExcluir)).orElse(false))) {
            throw new NomeConflictException("Nome de departamento já cadastrado: " + nome);
        }
    }
}