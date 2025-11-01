package com.vidaplus.sghss.controller;

import com.vidaplus.sghss.dto.ConsultaRequest;
import com.vidaplus.sghss.entity.Consulta;
import com.vidaplus.sghss.service.ConsultaService;
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
 * Controller para gerenciamento de consultas.
 */
@RestController
@RequestMapping("/api/v1/consultas")
@Tag(name = "Consultas", description = "Endpoints para gerenciamento de consultas")
@SecurityRequirement(name = "bearerAuth")
public class ConsultaController {

    @Autowired
    private ConsultaService consultaService;

    /**
     * Agenda uma nova consulta.
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('PACIENTE', 'ADMIN')")
    @Operation(summary = "Agendar consulta", description = "Agenda uma nova consulta")
    public ResponseEntity<Consulta> agendarConsulta(@Valid @RequestBody ConsultaRequest request) {
        Consulta consulta = consultaService.agendarConsulta(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(consulta);
    }

    /**
     * Lista consultas por paciente.
     */
    @GetMapping("/paciente/{pacienteId}")
    @PreAuthorize("hasAnyRole('PACIENTE', 'PROFISSIONAL', 'ADMIN')")
    @Operation(summary = "Listar consultas do paciente", description = "Lista todas as consultas de um paciente")
    public ResponseEntity<List<Consulta>> listarConsultasPorPaciente(@PathVariable Long pacienteId) {
        List<Consulta> consultas = consultaService.listarPorPaciente(pacienteId);
        return ResponseEntity.ok(consultas);
    }

    /**
     * Lista consultas por profissional.
     */
    @GetMapping("/profissional/{profissionalId}")
    @PreAuthorize("hasAnyRole('PROFISSIONAL', 'ADMIN')")
    @Operation(summary = "Listar consultas do profissional", description = "Lista todas as consultas de um profissional")
    public ResponseEntity<List<Consulta>> listarConsultasPorProfissional(@PathVariable Long profissionalId) {
        List<Consulta> consultas = consultaService.listarPorProfissional(profissionalId);
        return ResponseEntity.ok(consultas);
    }

    /**
     * Cancela uma consulta.
     */
    @PatchMapping("/{id}/cancelar")
    @PreAuthorize("hasAnyRole('PACIENTE', 'ADMIN')")
    @Operation(summary = "Cancelar consulta", description = "Cancela uma consulta agendada")
    public ResponseEntity<Void> cancelarConsulta(@PathVariable Long id) {
        consultaService.cancelarConsulta(id);
        return ResponseEntity.noContent().build();
    }
}
