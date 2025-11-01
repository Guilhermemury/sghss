package com.vidaplus.sghss.repository;

import com.vidaplus.sghss.entity.Consulta;
import com.vidaplus.sghss.enums.StatusConsulta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repositório para a entidade Consulta.
 */
@Repository
public interface ConsultaRepository extends JpaRepository<Consulta, Long> {
    
    List<Consulta> findByPacienteId(Long pacienteId);
    
    List<Consulta> findByProfissionalId(Long profissionalId);
    
    List<Consulta> findByStatus(StatusConsulta status);
    
    @Query("SELECT c FROM Consulta c WHERE c.profissional.id = :profissionalId " +
           "AND c.dataHora BETWEEN :inicio AND :fim AND c.status = :status")
    List<Consulta> findByProfissionalAndDataHoraBetweenAndStatus(
            @Param("profissionalId") Long profissionalId,
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim,
            @Param("status") StatusConsulta status
    );
}
