package com.vidaplus.sghss.controller;

import com.vidaplus.sghss.dto.PacienteRequest;
import com.vidaplus.sghss.entity.Paciente;
import com.vidaplus.sghss.service.PacienteService;
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
 * Controller para gerenciamento de pacientes.
 */
@RestController
@RequestMapping("/api/v1/pacientes")
@Tag(name = "Pacientes", description = "Endpoints para gerenciamento de pacientes")
@SecurityRequirement(name = "bearerAuth")
public class PacienteController {

    @Autowired
    private PacienteService pacienteService;

    /**
     * Cadastra um novo paciente.
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Cadastrar paciente", description = "Cadastra um novo paciente no sistema")
    public ResponseEntity<Paciente> cadastrarPaciente(@Valid @RequestBody PacienteRequest request) {
        Paciente paciente = pacienteService.criarPaciente(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(paciente);
    }

    /**
     * Lista todos os pacientes.
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'PROFISSIONAL')")
    @Operation(summary = "Listar pacientes", description = "Lista todos os pacientes cadastrados")
    public ResponseEntity<List<Paciente>> listarPacientes() {
        List<Paciente> pacientes = pacienteService.listarTodos();
        return ResponseEntity.ok(pacientes);
    }

    /**
     * Busca um paciente por ID.
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROFISSIONAL', 'PACIENTE')")
    @Operation(summary = "Buscar paciente", description = "Busca um paciente específico por ID")
    public ResponseEntity<Paciente> buscarPaciente(@PathVariable Long id) {
        Paciente paciente = pacienteService.buscarPorId(id);
        return ResponseEntity.ok(paciente);
    }
}
