package com.vidaplus.sghss.service;

import com.vidaplus.sghss.dto.ConsultaRequest;
import com.vidaplus.sghss.dto.RealizarConsultaRequest;
import com.vidaplus.sghss.entity.Consulta;
import com.vidaplus.sghss.entity.Paciente;
import com.vidaplus.sghss.entity.Profissional;
import com.vidaplus.sghss.enums.StatusConsulta;
import com.vidaplus.sghss.exception.ResourceNotFoundException;
import com.vidaplus.sghss.repository.ConsultaRepository;
import com.vidaplus.sghss.repository.PacienteRepository;
import com.vidaplus.sghss.repository.ProfissionalRepository;
import com.vidaplus.sghss.repository.ProntuarioRepository;
import com.vidaplus.sghss.entity.Prontuario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Serviço para gerenciamento de consultas.
 */
@Service
public class ConsultaService {

    private static final Logger logger = LoggerFactory.getLogger(ConsultaService.class);

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private ProfissionalRepository profissionalRepository;

    @Autowired
    private ProntuarioRepository prontuarioRepository;

    /**
     * Agenda uma nova consulta.
     */
    @Transactional
    public Consulta agendarConsulta(ConsultaRequest request) {
        Paciente paciente = pacienteRepository.findById(request.getPacienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Paciente não encontrado"));

        Profissional profissional = profissionalRepository.findById(request.getProfissionalId())
                .orElseThrow(() -> new ResourceNotFoundException("Profissional não encontrado"));

        // Verifica se o profissional está disponível no horário
        LocalDateTime inicio = request.getDataHora().minusMinutes(30);
        LocalDateTime fim = request.getDataHora().plusMinutes(30);
        
        List<Consulta> consultasConflitantes = consultaRepository
                .findByProfissionalAndDataHoraBetweenAndStatus(
                        profissional.getId(), 
                        inicio, 
                        fim, 
                        StatusConsulta.AGENDADA
                );

        if (!consultasConflitantes.isEmpty()) {
            throw new IllegalArgumentException("Profissional não disponível neste horário");
        }

        Consulta consulta = new Consulta();
        consulta.setPaciente(paciente);
        consulta.setProfissional(profissional);
        consulta.setDataHora(request.getDataHora());
        consulta.setStatus(StatusConsulta.AGENDADA);

        return consultaRepository.save(consulta);
    }

    /**
     * Lista consultas por paciente.
     */
    public List<Consulta> listarPorPaciente(Long pacienteId) {
        return consultaRepository.findByPacienteId(pacienteId);
    }

    /**
     * Lista consultas por profissional.
     */
    public List<Consulta> listarPorProfissional(Long profissionalId) {
        return consultaRepository.findByProfissionalId(profissionalId);
    }

    /**
     * Cancela uma consulta.
     */
    @Transactional
    public void cancelarConsulta(Long id) {
        Consulta consulta = consultaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Consulta não encontrada"));

        if (consulta.getStatus() == StatusConsulta.CANCELADA) {
            throw new IllegalArgumentException("Consulta já está cancelada");
        }

        if (consulta.getStatus() == StatusConsulta.REALIZADA) {
            throw new IllegalArgumentException("Não é possível cancelar uma consulta já realizada");
        }

        consulta.setStatus(StatusConsulta.CANCELADA);
        consultaRepository.save(consulta);
    }

    /**
     * Realiza uma consulta (marca como realizada e atualiza prontuário).
     */
    @Transactional
    public Consulta realizarConsulta(Long id, RealizarConsultaRequest request) {
        logger.info("Realizando consulta ID: {}", id);
        
        Consulta consulta = consultaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Consulta não encontrada"));

        if (consulta.getStatus() == StatusConsulta.CANCELADA) {
            logger.error("Tentativa de realizar consulta cancelada. Consulta ID: {}", id);
            throw new IllegalArgumentException("Não é possível realizar uma consulta cancelada");
        }

        if (consulta.getStatus() == StatusConsulta.REALIZADA) {
            logger.error("Tentativa de realizar consulta já realizada. Consulta ID: {}", id);
            throw new IllegalArgumentException("Consulta já foi realizada");
        }

        // Marca a consulta como realizada
        consulta.setStatus(StatusConsulta.REALIZADA);
        Consulta consultaAtualizada = consultaRepository.save(consulta);

        // Atualiza o prontuário do paciente com as informações da consulta
        if (request.getObservacoes() != null || request.getDiagnostico() != null || request.getTratamento() != null) {
            atualizarProntuario(consulta, request);
        }

        logger.info("Consulta realizada com sucesso. ID: {}", id);
        return consultaAtualizada;
    }

    /**
     * Atualiza o prontuário do paciente com informações da consulta.
     */
    private void atualizarProntuario(Consulta consulta, RealizarConsultaRequest request) {
        Paciente paciente = consulta.getPaciente();
        
        // Busca ou cria o prontuário do paciente
        Prontuario prontuario = prontuarioRepository.findByPacienteId(paciente.getId())
                .orElseGet(() -> {
                    Prontuario novoProntuario = new Prontuario();
                    novoProntuario.setPaciente(paciente);
                    novoProntuario.setRegistro("");
                    return novoProntuario;
                });

        // Adiciona novo registro ao prontuário
        StringBuilder novoRegistro = new StringBuilder();
        novoRegistro.append("\n\n=== CONSULTA - ").append(LocalDateTime.now()).append(" ===");
        novoRegistro.append("\nProfissional: ").append(consulta.getProfissional().getNome());
        novoRegistro.append(" - CRM: ").append(consulta.getProfissional().getCrm());
        
        if (request.getObservacoes() != null && !request.getObservacoes().isBlank()) {
            novoRegistro.append("\nObservações: ").append(request.getObservacoes());
        }
        
        if (request.getDiagnostico() != null && !request.getDiagnostico().isBlank()) {
            novoRegistro.append("\nDiagnóstico: ").append(request.getDiagnostico());
        }
        
        if (request.getTratamento() != null && !request.getTratamento().isBlank()) {
            novoRegistro.append("\nTratamento: ").append(request.getTratamento());
        }

        prontuario.setRegistro(prontuario.getRegistro() + novoRegistro.toString());
        prontuarioRepository.save(prontuario);
        
        logger.info("Prontuário atualizado para paciente ID: {}", paciente.getId());
    }
}
