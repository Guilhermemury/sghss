package com.vidaplus.sghss.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para criar uma prescrição médica.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrescricaoRequest {

    @NotNull(message = "O ID da consulta é obrigatório")
    private Long consultaId;

    @NotBlank(message = "O medicamento é obrigatório")
    private String medicamento;

    @NotBlank(message = "A dosagem é obrigatória")
    private String dosagem;

    private String observacoes;
}
