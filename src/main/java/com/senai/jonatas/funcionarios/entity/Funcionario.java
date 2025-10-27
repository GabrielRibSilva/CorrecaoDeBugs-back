package com.senai.jonatas.funcionarios.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
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

    @Column(nullable = false, length = 200)
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

    @PrePersist @PreUpdate
    private void normalize() {
        if (nome != null) nome = nome.trim();
        if (email != null) email = email.trim().toLowerCase();
        if (cargo != null) cargo = cargo.trim();
    }
}
