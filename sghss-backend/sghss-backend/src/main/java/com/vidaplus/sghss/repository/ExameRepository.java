package com.vidaplus.sghss.repository;

import com.vidaplus.sghss.entity.Exame;
import com.vidaplus.sghss.enums.StatusExame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositório para a entidade Exame.
 */
@Repository
public interface ExameRepository extends JpaRepository<Exame, Long> {
    
    List<Exame> findByConsultaId(Long consultaId);
    
    List<Exame> findByConsultaPacienteId(Long pacienteId);
    
    List<Exame> findByStatus(StatusExame status);
}
