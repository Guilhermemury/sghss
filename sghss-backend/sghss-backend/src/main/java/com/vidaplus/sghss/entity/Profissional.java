package com.vidaplus.sghss.entity;

import com.vidaplus.sghss.enums.Especialidade;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidade que representa um profissional de saúde no sistema.
 * Pode ser médico, enfermeiro ou técnico.
 */
@Entity
@Table(name = "profissionais")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Profissional {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O CRM é obrigatório")
    @Column(nullable = false, unique = true, length = 20)
    private String crm;

    @NotNull(message = "A especialidade é obrigatória")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private Especialidade especialidade;

    @OneToOne
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    private Usuario usuario;
}
