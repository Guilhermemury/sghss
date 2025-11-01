package com.vidaplus.sghss.repository;

import com.vidaplus.sghss.entity.Prescricao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositório para a entidade Prescricao.
 */
@Repository
public interface PrescricaoRepository extends JpaRepository<Prescricao, Long> {
    
    List<Prescricao> findByConsultaId(Long consultaId);
}
