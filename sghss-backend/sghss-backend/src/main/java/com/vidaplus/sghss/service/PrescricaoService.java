package com.vidaplus.sghss.service;

import com.vidaplus.sghss.dto.PrescricaoRequest;
import com.vidaplus.sghss.entity.Consulta;
import com.vidaplus.sghss.entity.Prescricao;
import com.vidaplus.sghss.enums.StatusConsulta;
import com.vidaplus.sghss.exception.ResourceNotFoundException;
import com.vidaplus.sghss.repository.ConsultaRepository;
import com.vidaplus.sghss.repository.PrescricaoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Serviço para gerenciamento de prescrições médicas.
 */
@Service
public class PrescricaoService {

    private static final Logger logger = LoggerFactory.getLogger(PrescricaoService.class);

    @Autowired
    private PrescricaoRepository prescricaoRepository;

    @Autowired
    private ConsultaRepository consultaRepository;

    /**
     * Cria uma nova prescrição médica.
     */
    @Transactional
    public Prescricao criarPrescricao(PrescricaoRequest request) {
        logger.info("Criando prescrição para consulta ID: {}", request.getConsultaId());
        
        Consulta consulta = consultaRepository.findById(request.getConsultaId())
                .orElseThrow(() -> new ResourceNotFoundException("Consulta não encontrada"));

        // Verifica se a consulta foi realizada
        if (consulta.getStatus() != StatusConsulta.REALIZADA) {
            logger.error("Tentativa de prescrever medicamento para consulta não realizada. Consulta ID: {}", request.getConsultaId());
            throw new IllegalArgumentException("Só é possível prescrever medicamentos para consultas realizadas");
        }

        Prescricao prescricao = new Prescricao();
        prescricao.setConsulta(consulta);
        prescricao.setMedicamento(request.getMedicamento());
        prescricao.setDosagem(request.getDosagem());
        prescricao.setObservacoes(request.getObservacoes());

        Prescricao prescricaoSalva = prescricaoRepository.save(prescricao);
        logger.info("Prescrição criada com sucesso. ID: {}", prescricaoSalva.getId());
        
        return prescricaoSalva;
    }

    /**
     * Lista prescrições por consulta.
     */
    public List<Prescricao> listarPorConsulta(Long consultaId) {
        logger.info("Listando prescrições da consulta ID: {}", consultaId);
        return prescricaoRepository.findByConsultaId(consultaId);
    }

    /**
     * Busca uma prescrição por ID.
     */
    public Prescricao buscarPorId(Long id) {
        logger.info("Buscando prescrição ID: {}", id);
        return prescricaoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Prescrição não encontrada"));
    }

    /**
     * Atualiza uma prescrição existente.
     */
    @Transactional
    public Prescricao atualizarPrescricao(Long id, PrescricaoRequest request) {
        logger.info("Atualizando prescrição ID: {}", id);
        
        Prescricao prescricao = buscarPorId(id);
        
        prescricao.setMedicamento(request.getMedicamento());
        prescricao.setDosagem(request.getDosagem());
        prescricao.setObservacoes(request.getObservacoes());

        Prescricao prescricaoAtualizada = prescricaoRepository.save(prescricao);
        logger.info("Prescrição atualizada com sucesso. ID: {}", id);
        
        return prescricaoAtualizada;
    }

    /**
     * Remove uma prescrição.
     */
    @Transactional
    public void deletarPrescricao(Long id) {
        logger.info("Deletando prescrição ID: {}", id);
        
        Prescricao prescricao = buscarPorId(id);
        prescricaoRepository.delete(prescricao);
        
        logger.info("Prescrição deletada com sucesso. ID: {}", id);
    }
}
