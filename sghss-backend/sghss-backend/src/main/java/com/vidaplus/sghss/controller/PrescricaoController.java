package com.vidaplus.sghss.controller;

import com.vidaplus.sghss.dto.PrescricaoRequest;
import com.vidaplus.sghss.entity.Prescricao;
import com.vidaplus.sghss.service.PrescricaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller para gerenciamento de prescrições médicas.
 */
@RestController
@RequestMapping("/api/v1/prescricoes")
@Tag(name = "Prescrições", description = "Endpoints para gerenciamento de prescrições médicas")
@SecurityRequirement(name = "bearerAuth")
public class PrescricaoController {

    @Autowired
    private PrescricaoService prescricaoService;

    /**
     * Cria uma nova prescrição médica.
     */
    @PostMapping
    @PreAuthorize("hasRole('PROFISSIONAL')")
    @Operation(summary = "Prescrever medicamento", description = "Cria uma nova prescrição médica para uma consulta realizada")
    public ResponseEntity<Prescricao> criarPrescricao(@Valid @RequestBody PrescricaoRequest request) {
        Prescricao prescricao = prescricaoService.criarPrescricao(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(prescricao);
    }

    /**
     * Lista prescrições por consulta.
     */
    @GetMapping("/consulta/{consultaId}")
    @PreAuthorize("hasAnyRole('PACIENTE', 'PROFISSIONAL', 'ADMIN')")
    @Operation(summary = "Listar prescrições da consulta", description = "Lista todas as prescrições de uma consulta")
    public ResponseEntity<List<Prescricao>> listarPrescricoesPorConsulta(@PathVariable Long consultaId) {
        List<Prescricao> prescricoes = prescricaoService.listarPorConsulta(consultaId);
        return ResponseEntity.ok(prescricoes);
    }

    /**
     * Busca uma prescrição por ID.
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('PACIENTE', 'PROFISSIONAL', 'ADMIN')")
    @Operation(summary = "Buscar prescrição", description = "Busca uma prescrição por ID")
    public ResponseEntity<Prescricao> buscarPrescricao(@PathVariable Long id) {
        Prescricao prescricao = prescricaoService.buscarPorId(id);
        return ResponseEntity.ok(prescricao);
    }

    /**
     * Atualiza uma prescrição existente.
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('PROFISSIONAL')")
    @Operation(summary = "Atualizar prescrição", description = "Atualiza uma prescrição existente")
    public ResponseEntity<Prescricao> atualizarPrescricao(
            @PathVariable Long id,
            @Valid @RequestBody PrescricaoRequest request) {
        Prescricao prescricao = prescricaoService.atualizarPrescricao(id, request);
        return ResponseEntity.ok(prescricao);
    }

    /**
     * Remove uma prescrição.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('PROFISSIONAL')")
    @Operation(summary = "Deletar prescrição", description = "Remove uma prescrição do sistema")
    public ResponseEntity<Void> deletarPrescricao(@PathVariable Long id) {
        prescricaoService.deletarPrescricao(id);
        return ResponseEntity.noContent().build();
    }
}
