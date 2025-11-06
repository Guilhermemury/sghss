package com.vidaplus.sghss.entity;

import com.vidaplus.sghss.enums.StatusExame;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entidade que representa um exame solicitado pelo médico.
 * Relacionada a uma consulta específica.
 */
@Entity
@Table(name = "exames")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Exame {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "consulta_id", nullable = false)
    @NotNull(message = "A consulta é obrigatória")
    private Consulta consulta;

    @NotBlank(message = "O tipo de exame é obrigatório")
    @Column(name = "tipo_exame", nullable = false, length = 100)
    private String tipoExame;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusExame status = StatusExame.SOLICITADO;

    @Column(name = "data_solicitacao", nullable = false)
    private LocalDateTime dataSolicitacao = LocalDateTime.now();

    @Column(name = "data_resultado")
    private LocalDateTime dataResultado;

    @Column(columnDefinition = "TEXT")
    private String resultado;
}
