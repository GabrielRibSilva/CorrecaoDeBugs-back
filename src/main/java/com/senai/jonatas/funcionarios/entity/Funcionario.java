package com.senai.jonatas.funcionarios.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects; // Importar Objects para equals/hashCode

@Entity
@Table(name = "funcionarios", uniqueConstraints = {
        @UniqueConstraint(name = "uk_funcionario_email", columnNames = "email")
})
public class Funcionario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, length = 200, unique = true)
    private String email;

    @Column(nullable = false)
    private String cargo;

    @Column(nullable = false, precision = 16, scale = 2)
    private BigDecimal salario;

    @Column(nullable = false)
    private LocalDate dataAdmissao;

    @NotNull
    @Column(nullable = false)
    private Boolean ativo = Boolean.TRUE;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "departamento_id", nullable = false, foreignKey = @ForeignKey(name = "fk_funcionario_departamento"))
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "funcionarios"})
    private Departamento departamento;

    public Funcionario() {
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getCargo() { return cargo; }
    public void setCargo(String cargo) { this.cargo = cargo; }
    public BigDecimal getSalario() { return salario; }
    public void setSalario(BigDecimal salario) { this.salario = salario; }
    public LocalDate getDataAdmissao() { return dataAdmissao; }
    public void setDataAdmissao(LocalDate dataAdmissao) { this.dataAdmissao = dataAdmissao; }
    public Boolean getAtivo() { return ativo; }
    public void setAtivo(Boolean ativo) { this.ativo = ativo; }
    public Departamento getDepartamento() { return departamento; }
    public void setDepartamento(Departamento departamento) { this.departamento = departamento; }


    @PrePersist @PreUpdate
    private void normalize() {
        if (nome != null) nome = nome.trim();
        if (email != null) email = email.trim().toLowerCase();
        if (cargo != null) cargo = cargo.trim();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Funcionario that = (Funcionario) o;

        if (id != null && that.id != null) {
            return Objects.equals(id, that.id);
        }

        return Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {

        return id != null ? Objects.hash(id) : Objects.hash(email);
    }


    @Override
    public String toString() {
        return "Funcionario{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", cargo='" + cargo + '\'' +
                ", ativo=" + ativo +
                ", departamentoId=" + (departamento != null ? departamento.getId() : null) +
                '}';
    }

    public static FuncionarioBuilder builder() {
        return new FuncionarioBuilder();
    }

    public static class FuncionarioBuilder {
        private String nome;
        private String email;
        private String cargo;
        private BigDecimal salario;
        private LocalDate dataAdmissao;
        private Boolean ativo = true; // Default
        private Departamento departamento;

        FuncionarioBuilder() {}

        public FuncionarioBuilder nome(String nome) { this.nome = nome; return this; }
        public FuncionarioBuilder email(String email) { this.email = email; return this; }
        public FuncionarioBuilder cargo(String cargo) { this.cargo = cargo; return this; }
        public FuncionarioBuilder salario(BigDecimal salario) { this.salario = salario; return this; }
        public FuncionarioBuilder dataAdmissao(LocalDate dataAdmissao) { this.dataAdmissao = dataAdmissao; return this; }
        public FuncionarioBuilder ativo(Boolean ativo) { this.ativo = ativo; return this; }
        public FuncionarioBuilder departamento(Departamento departamento) { this.departamento = departamento; return this; }

        public Funcionario build() {
            Funcionario func = new Funcionario();
            func.setNome(this.nome);
            func.setEmail(this.email);
            func.setCargo(this.cargo);
            func.setSalario(this.salario);
            func.setDataAdmissao(this.dataAdmissao);
            func.setAtivo(this.ativo);
            func.setDepartamento(this.departamento);
            return func;
        }
    }
}