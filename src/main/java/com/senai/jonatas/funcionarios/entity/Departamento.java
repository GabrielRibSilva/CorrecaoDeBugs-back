package com.senai.jonatas.funcionarios.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "departamentos", uniqueConstraints = {
        @UniqueConstraint(name = "uk_departamento_nome", columnNames = "nome")
})
public class Departamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String nome;

    @NotBlank
    @Column(nullable = false, length = 10)
    private String sigla;

    @NotNull
    @Column(nullable = false)
    private Boolean ativo = Boolean.TRUE;


    @OneToMany(mappedBy = "departamento", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Funcionario> funcionarios = new ArrayList<>();

    public Departamento() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public List<Funcionario> getFuncionarios() {
        return funcionarios;
    }

    public void setFuncionarios(List<Funcionario> funcionarios) {
        this.funcionarios = funcionarios;
    }

    @PrePersist @PreUpdate
    private void normalize() {
        if (nome != null) nome = nome.trim();
        if (sigla != null) sigla = sigla.trim().toUpperCase();
    }


    public void addFuncionario(Funcionario funcionario) {
        funcionarios.add(funcionario);
        funcionario.setDepartamento(this);
    }


    public void removeFuncionario(Funcionario funcionario) {
        funcionarios.remove(funcionario);
        funcionario.setDepartamento(null);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Departamento that = (Departamento) o;

        if (id != null && that.id != null) {
            return Objects.equals(id, that.id);
        }

        return Objects.equals(nome, that.nome) && Objects.equals(sigla, that.sigla);
    }

    @Override
    public int hashCode() {

        return id != null ? Objects.hash(id) : Objects.hash(nome, sigla);
    }


    @Override
    public String toString() {
        return "Departamento{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", sigla='" + sigla + '\'' +
                ", ativo=" + ativo +
                '}';
    }

    public static DepartamentoBuilder builder() {
        return new DepartamentoBuilder();
    }

    public static class DepartamentoBuilder {
        private String nome;
        private String sigla;
        private Boolean ativo = true; // Default

        DepartamentoBuilder() {}

        public DepartamentoBuilder nome(String nome) {
            this.nome = nome;
            return this;
        }

        public DepartamentoBuilder sigla(String sigla) {
            this.sigla = sigla;
            return this;
        }

        public DepartamentoBuilder ativo(Boolean ativo) {
            this.ativo = ativo;
            return this;
        }

        public Departamento build() {
            Departamento depto = new Departamento();
            depto.setNome(this.nome);
            depto.setSigla(this.sigla);
            depto.setAtivo(this.ativo);

            return depto;
        }
    }
}