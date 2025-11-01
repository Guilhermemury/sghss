package com.vidaplus.sghss.service;

import com.vidaplus.sghss.dto.ConsultaRequest;
import com.vidaplus.sghss.entity.Consulta;
import com.vidaplus.sghss.entity.Paciente;
import com.vidaplus.sghss.entity.Profissional;
import com.vidaplus.sghss.enums.StatusConsulta;
import com.vidaplus.sghss.exception.ResourceNotFoundException;
import com.vidaplus.sghss.repository.ConsultaRepository;
import com.vidaplus.sghss.repository.PacienteRepository;
import com.vidaplus.sghss.repository.ProfissionalRepository;
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

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private ProfissionalRepository profissionalRepository;

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
}
