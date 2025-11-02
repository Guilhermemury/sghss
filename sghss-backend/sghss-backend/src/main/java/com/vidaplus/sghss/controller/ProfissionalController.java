package com.vidaplus.sghss.controller;

import com.vidaplus.sghss.dto.ProfissionalRequest;
import com.vidaplus.sghss.entity.Profissional;
import com.vidaplus.sghss.service.ProfissionalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/profissionais")
@Tag(name = "Profissionais", description = "Endpoints para gerenciamento de profissionais de saúde")
public class ProfissionalController {

    @Autowired
    private ProfissionalService profissionalService;

    @PostMapping
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Cadastrar novo profissional",
            description = "Registra um novo profissional e seu usuário de acesso. (Requer ROLE_ADMIN)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Profissional cadastrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos (email/crm já existe ou campos faltando)"),
            @ApiResponse(responseCode = "4O3", description = "Acesso negado (Não é ADMIN)")
    })
    public ResponseEntity<Void> cadastrar(
            @RequestBody @Valid ProfissionalRequest dados,
            UriComponentsBuilder uriBuilder) {

        Profissional profissionalSalvo = profissionalService.cadastrarNovoProfissional(dados);

        URI uri = uriBuilder.path("/profissionais/{id}")
                .buildAndExpand(profissionalSalvo.getId())
                .toUri();

        return ResponseEntity.created(uri).build();
    }
}