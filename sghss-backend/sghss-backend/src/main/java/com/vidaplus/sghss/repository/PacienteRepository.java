package com.vidaplus.sghss.repository;

import com.vidaplus.sghss.entity.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositório para a entidade Paciente.
 */
@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    
    Optional<Paciente> findByCpf(String cpf);
    
    boolean existsByCpf(String cpf);
    
    Optional<Paciente> findByUsuarioId(Long usuarioId);
}
