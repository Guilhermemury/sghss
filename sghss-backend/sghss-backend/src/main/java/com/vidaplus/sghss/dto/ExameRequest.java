package com.vidaplus.sghss.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para solicitar um exame.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExameRequest {

    @NotNull(message = "O ID da consulta é obrigatório")
    private Long consultaId;

    @NotBlank(message = "O tipo de exame é obrigatório")
    private String tipoExame;

    private String descricao;
}
