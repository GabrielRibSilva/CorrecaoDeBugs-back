package com.senai.jonatas.funcionarios.service;

import com.senai.jonatas.funcionarios.dto.FuncionarioRequest;
import com.senai.jonatas.funcionarios.dto.FuncionarioResponse;
import com.senai.jonatas.funcionarios.entity.Departamento;
import com.senai.jonatas.funcionarios.exceptions.*;
import com.senai.jonatas.funcionarios.mapper.FuncionarioMapper;
import com.senai.jonatas.funcionarios.entity.Funcionario;
import com.senai.jonatas.funcionarios.repository.FuncionarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class FuncionarioService {

    private final FuncionarioRepository repository;

    private final DepartamentoService departamentoService;

    public FuncionarioService(FuncionarioRepository repository, DepartamentoService departamentoService) {
        this.repository = repository;
        this.departamentoService = departamentoService;
    }

    public List<FuncionarioResponse> listar(String cargo, Boolean ativo) {
        List<Funcionario> lista;

        if (cargo != null && !cargo.isBlank() && ativo != null) {
            lista = repository.findByCargoIgnoreCaseContainingAndAtivoOrderByNomeAsc(cargo.trim(), ativo);
        } else if (cargo != null && !cargo.isBlank()) {
            lista = repository.findByCargoIgnoreCaseContainingOrderByNomeAsc(cargo.trim());
        } else if (ativo != null) {
            lista = repository.findByAtivoOrderByNomeAsc(ativo);
        } else {
            lista = repository.findAllByOrderByNomeAsc();
        }

        return lista.stream().map(FuncionarioMapper::toResponse).toList();
    }

    public FuncionarioResponse buscarPorId(Long id) {
        var func = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Funcionário não encontrado"));
        return FuncionarioMapper.toResponse(func);
    }

    @Transactional
    public Result<FuncionarioResponse> cadastrar(FuncionarioRequest req) {
        validarRegrasComuns(req);
        Departamento depto = departamentoService.findDepartamentoById(req.departamentoId());
        validarDepartamentoAtivo(depto);

        var existenteOpt = repository.findByEmailIgnoreCase(req.email());
        if (existenteOpt.isPresent()) {
            var existente = existenteOpt.get();
            if (Boolean.TRUE.equals(existente.getAtivo())) {
                throw new EmailConflictException("E-mail já cadastrado");
            }
            aplicarAtualizacao(req, existente, depto, true);
            var salvo = repository.save(existente);
            return Result.reactivated(FuncionarioMapper.toResponse(salvo));
        }

        var novo = Funcionario.builder()
                .nome(req.nome())
                .email(req.email())
                .cargo(req.cargo())
                .salario(req.salario())
                .dataAdmissao(req.dataAdmissao())
                .ativo(true)
                .departamento(depto)
                .build();

        var salvo = repository.save(novo);
        return Result.created(FuncionarioMapper.toResponse(salvo));
    }

    @Transactional
    public FuncionarioResponse atualizar(Long id, FuncionarioRequest req) {
        validarRegrasComuns(req);
        Departamento depto = departamentoService.findDepartamentoById(req.departamentoId());

        var existente = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Funcionário não encontrado"));

        if (!Boolean.TRUE.equals(existente.getAtivo())) {
            throw new BusinessException("Apenas funcionários ativos podem ser editados");
        }

        if (!existente.getEmail().equalsIgnoreCase(req.email()) &&
                repository.existsByEmailIgnoreCase(req.email())) {
            throw new BusinessException("E-mail informado já está em uso por outro funcionário");
        }

        if (req.salario().compareTo(existente.getSalario()) < 0) {
            throw new BusinessException("Salário não pode ser reduzido");
        }

        aplicarAtualizacao(req, existente, depto, false);
        var salvo = repository.save(existente);
        return FuncionarioMapper.toResponse(salvo);
    }

    @Transactional
    public FuncionarioResponse inativar(Long id) {
        var existente = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Funcionário não encontrado"));
        existente.setAtivo(false);
        var salvo = repository.save(existente);
        return FuncionarioMapper.toResponse(salvo);
    }

    private void aplicarAtualizacao(FuncionarioRequest req, Funcionario entidade, Departamento depto, boolean reativacao) {
        entidade.setNome(req.nome());
        entidade.setCargo(req.cargo());
        entidade.setSalario(req.salario());
        entidade.setDataAdmissao(req.dataAdmissao());
        entidade.setEmail(req.email());
        entidade.setDepartamento(depto);
        if (reativacao) {
            entidade.setAtivo(true);
        }
    }

    private void validarDepartamentoAtivo(Departamento depto) {
        if (!Boolean.TRUE.equals(depto.getAtivo())) {
            throw new BusinessException("Não é possível vincular funcionário a um departamento inativo.");
        }
    }

    private void validarRegrasComuns(FuncionarioRequest req) {
        // Nenhum campo só com espaços (Bean Validation + sanity check)
        if (req.nome().isBlank() || req.email().isBlank() || req.cargo().isBlank()) {
            throw new BusinessException("Campos não podem conter apenas espaços em branco");
        }
        // Data não futura (já coberto por @PastOrPresent, reforçando regra de negócio)
        if (req.dataAdmissao().isAfter(LocalDate.now())) {
            throw new BusinessException("Data de admissão não pode ser posterior à data atual");
        }
        // Salário > 0 (já coberto, reforçado)
        if (req.salario() == null || req.salario().signum() <= 0) {
            throw new BusinessException("Salário deve ser maior que zero");
        }
    }

    // Resultado para diferenciar 201 (criado) de 200 (reativado) no controller
    public record Result<T>(T body, boolean created, boolean reactivated) {
        public static <T> Result<T> created(T body) { return new Result<>(body, true, false); }
        public static <T> Result<T> reactivated(T body) { return new Result<>(body, false, true); }
    }
}
