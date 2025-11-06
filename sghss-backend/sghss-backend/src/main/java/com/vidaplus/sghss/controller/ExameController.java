package com.vidaplus.sghss.controller;

import com.vidaplus.sghss.dto.ExameRequest;
import com.vidaplus.sghss.entity.Exame;
import com.vidaplus.sghss.enums.StatusExame;
import com.vidaplus.sghss.service.ExameService;
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
import java.util.Map;

/**
 * Controller para gerenciamento de exames.
 */
@RestController
@RequestMapping("/api/v1/exames")
@Tag(name = "Exames", description = "Endpoints para gerenciamento de exames médicos")
@SecurityRequirement(name = "bearerAuth")
public class ExameController {

    @Autowired
    private ExameService exameService;

    /**
     * Solicita um novo exame.
     */
    @PostMapping
    @PreAuthorize("hasRole('PROFISSIONAL')")
    @Operation(summary = "Solicitar exame", description = "Solicita um novo exame para uma consulta realizada")
    public ResponseEntity<Exame> solicitarExame(@Valid @RequestBody ExameRequest request) {
        Exame exame = exameService.solicitarExame(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(exame);
    }

    /**
     * Lista exames por consulta.
     */
    @GetMapping("/consulta/{consultaId}")
    @PreAuthorize("hasAnyRole('PACIENTE', 'PROFISSIONAL', 'ADMIN')")
    @Operation(summary = "Listar exames da consulta", description = "Lista todos os exames de uma consulta")
    public ResponseEntity<List<Exame>> listarExamesPorConsulta(@PathVariable Long consultaId) {
        List<Exame> exames = exameService.listarPorConsulta(consultaId);
        return ResponseEntity.ok(exames);
    }

    /**
     * Lista exames por paciente.
     */
    @GetMapping("/paciente/{pacienteId}")
    @PreAuthorize("hasAnyRole('PACIENTE', 'PROFISSIONAL', 'ADMIN')")
    @Operation(summary = "Listar exames do paciente", description = "Lista todos os exames de um paciente")
    public ResponseEntity<List<Exame>> listarExamesPorPaciente(@PathVariable Long pacienteId) {
        List<Exame> exames = exameService.listarPorPaciente(pacienteId);
        return ResponseEntity.ok(exames);
    }

    /**
     * Lista exames por status.
     */
    @GetMapping("/status/{status}")
    @PreAuthorize("hasAnyRole('PROFISSIONAL', 'ADMIN')")
    @Operation(summary = "Listar exames por status", description = "Lista todos os exames com um status específico")
    public ResponseEntity<List<Exame>> listarExamesPorStatus(@PathVariable StatusExame status) {
        List<Exame> exames = exameService.listarPorStatus(status);
        return ResponseEntity.ok(exames);
    }

    /**
     * Busca um exame por ID.
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('PACIENTE', 'PROFISSIONAL', 'ADMIN')")
    @Operation(summary = "Buscar exame", description = "Busca um exame por ID")
    public ResponseEntity<Exame> buscarExame(@PathVariable Long id) {
        Exame exame = exameService.buscarPorId(id);
        return ResponseEntity.ok(exame);
    }

    /**
     * Atualiza o status de um exame.
     */
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('PROFISSIONAL', 'ADMIN')")
    @Operation(summary = "Atualizar status do exame", description = "Atualiza o status de um exame")
    public ResponseEntity<Exame> atualizarStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        StatusExame novoStatus = StatusExame.valueOf(body.get("status"));
        Exame exame = exameService.atualizarStatus(id, novoStatus);
        return ResponseEntity.ok(exame);
    }

    /**
     * Adiciona resultado a um exame.
     */
    @PatchMapping("/{id}/resultado")
    @PreAuthorize("hasAnyRole('PROFISSIONAL', 'ADMIN')")
    @Operation(summary = "Adicionar resultado ao exame", description = "Adiciona o resultado a um exame e marca como concluído")
    public ResponseEntity<Exame> adicionarResultado(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        String resultado = body.get("resultado");
        Exame exame = exameService.adicionarResultado(id, resultado);
        return ResponseEntity.ok(exame);
    }

    /**
     * Cancela um exame.
     */
    @PatchMapping("/{id}/cancelar")
    @PreAuthorize("hasAnyRole('PROFISSIONAL', 'ADMIN')")
    @Operation(summary = "Cancelar exame", description = "Cancela um exame solicitado")
    public ResponseEntity<Void> cancelarExame(@PathVariable Long id) {
        exameService.cancelarExame(id);
        return ResponseEntity.noContent().build();
    }
}
