package com.vidaplus.sghss.service;

import com.vidaplus.sghss.dto.ExameRequest;
import com.vidaplus.sghss.entity.Consulta;
import com.vidaplus.sghss.entity.Exame;
import com.vidaplus.sghss.enums.StatusConsulta;
import com.vidaplus.sghss.enums.StatusExame;
import com.vidaplus.sghss.exception.ResourceNotFoundException;
import com.vidaplus.sghss.repository.ConsultaRepository;
import com.vidaplus.sghss.repository.ExameRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Serviço para gerenciamento de exames.
 */
@Service
public class ExameService {

    private static final Logger logger = LoggerFactory.getLogger(ExameService.class);

    @Autowired
    private ExameRepository exameRepository;

    @Autowired
    private ConsultaRepository consultaRepository;

    /**
     * Solicita um novo exame.
     */
    @Transactional
    public Exame solicitarExame(ExameRequest request) {
        logger.info("Solicitando exame para consulta ID: {}", request.getConsultaId());
        
        Consulta consulta = consultaRepository.findById(request.getConsultaId())
                .orElseThrow(() -> new ResourceNotFoundException("Consulta não encontrada"));

        // Verifica se a consulta foi realizada
        if (consulta.getStatus() != StatusConsulta.REALIZADA) {
            logger.error("Tentativa de solicitar exame para consulta não realizada. Consulta ID: {}", request.getConsultaId());
            throw new IllegalArgumentException("Só é possível solicitar exames para consultas realizadas");
        }

        Exame exame = new Exame();
        exame.setConsulta(consulta);
        exame.setTipoExame(request.getTipoExame());
        exame.setDescricao(request.getDescricao());
        exame.setStatus(StatusExame.SOLICITADO);
        exame.setDataSolicitacao(LocalDateTime.now());

        Exame exameSalvo = exameRepository.save(exame);
        logger.info("Exame solicitado com sucesso. ID: {}", exameSalvo.getId());
        
        return exameSalvo;
    }

    /**
     * Lista exames por consulta.
     */
    public List<Exame> listarPorConsulta(Long consultaId) {
        logger.info("Listando exames da consulta ID: {}", consultaId);
        return exameRepository.findByConsultaId(consultaId);
    }

    /**
     * Lista exames por paciente.
     */
    public List<Exame> listarPorPaciente(Long pacienteId) {
        logger.info("Listando exames do paciente ID: {}", pacienteId);
        return exameRepository.findByConsultaPacienteId(pacienteId);
    }

    /**
     * Lista exames por status.
     */
    public List<Exame> listarPorStatus(StatusExame status) {
        logger.info("Listando exames com status: {}", status);
        return exameRepository.findByStatus(status);
    }

    /**
     * Busca um exame por ID.
     */
    public Exame buscarPorId(Long id) {
        logger.info("Buscando exame ID: {}", id);
        return exameRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Exame não encontrado"));
    }

    /**
     * Atualiza o status de um exame.
     */
    @Transactional
    public Exame atualizarStatus(Long id, StatusExame novoStatus) {
        logger.info("Atualizando status do exame ID: {} para {}", id, novoStatus);
        
        Exame exame = buscarPorId(id);
        exame.setStatus(novoStatus);

        if (novoStatus == StatusExame.CONCLUIDO) {
            exame.setDataResultado(LocalDateTime.now());
        }

        Exame exameAtualizado = exameRepository.save(exame);
        logger.info("Status do exame atualizado com sucesso. ID: {}", id);
        
        return exameAtualizado;
    }

    /**
     * Adiciona resultado a um exame.
     */
    @Transactional
    public Exame adicionarResultado(Long id, String resultado) {
        logger.info("Adicionando resultado ao exame ID: {}", id);
        
        Exame exame = buscarPorId(id);
        exame.setResultado(resultado);
        exame.setStatus(StatusExame.CONCLUIDO);
        exame.setDataResultado(LocalDateTime.now());

        Exame exameAtualizado = exameRepository.save(exame);
        logger.info("Resultado adicionado com sucesso ao exame ID: {}", id);
        
        return exameAtualizado;
    }

    /**
     * Cancela um exame.
     */
    @Transactional
    public void cancelarExame(Long id) {
        logger.info("Cancelando exame ID: {}", id);
        
        Exame exame = buscarPorId(id);
        
        if (exame.getStatus() == StatusExame.CONCLUIDO) {
            logger.error("Tentativa de cancelar exame já concluído. Exame ID: {}", id);
            throw new IllegalArgumentException("Não é possível cancelar um exame já concluído");
        }

        exame.setStatus(StatusExame.CANCELADO);
        exameRepository.save(exame);
        
        logger.info("Exame cancelado com sucesso. ID: {}", id);
    }
}
