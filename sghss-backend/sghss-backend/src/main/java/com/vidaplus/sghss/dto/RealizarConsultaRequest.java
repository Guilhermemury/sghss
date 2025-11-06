package com.vidaplus.sghss.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para realizar uma consulta.
 * Contém as informações registradas durante o atendimento.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RealizarConsultaRequest {

    private String observacoes;
    
    private String diagnostico;
    
    private String tratamento;
}
