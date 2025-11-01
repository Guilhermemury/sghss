package com.vidaplus.sghss.repository;

import com.vidaplus.sghss.entity.Prontuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositório para a entidade Prontuario.
 */
@Repository
public interface ProntuarioRepository extends JpaRepository<Prontuario, Long> {
    
    Optional<Prontuario> findByPacienteId(Long pacienteId);
}
