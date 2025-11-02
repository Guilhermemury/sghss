package com.vidaplus.sghss.dto;

import com.vidaplus.sghss.enums.Especialidade;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO (Record) para receber os dados de cadastro de um novo Profissional.
 * A anotação @Valid no Controller vai usar essas regras para validar a entrada.
 */
public record ProfissionalRequest(
        @NotBlank(message = "Nome é obrigatório")
        String nome,

        @NotBlank(message = "Email é obrigatório")
        @Email(message = "Formato de email inválido")
        String email,

        @NotBlank(message = "Senha é obrigatória")
        @Size(min = 6, message = "Senha deve ter no mínimo 6 caracteres")
        String senha,

        @NotBlank(message = "CRM é obrigatório")
        String crm,

        @NotNull(message = "Especialidade é obrigatória")
        Especialidade especialidade
) {
}