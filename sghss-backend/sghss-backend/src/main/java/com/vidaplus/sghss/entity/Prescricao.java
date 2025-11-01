package com.vidaplus.sghss.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidade que representa uma prescrição médica.
 * Relacionada a uma consulta específica.
 */
@Entity
@Table(name = "prescricoes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Prescricao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "consulta_id", nullable = false)
    @NotNull(message = "A consulta é obrigatória")
    private Consulta consulta;

    @NotBlank(message = "O medicamento é obrigatório")
    @Column(nullable = false, length = 200)
    private String medicamento;

    @NotBlank(message = "A dosagem é obrigatória")
    @Column(nullable = false, length = 100)
    private String dosagem;

    @Column(columnDefinition = "TEXT")
    private String observacoes;
}
