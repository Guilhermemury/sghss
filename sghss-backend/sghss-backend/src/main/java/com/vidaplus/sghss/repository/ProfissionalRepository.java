package com.vidaplus.sghss.repository;

import com.vidaplus.sghss.entity.Profissional;
import com.vidaplus.sghss.enums.Especialidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório para a entidade Profissional.
 */
@Repository
public interface ProfissionalRepository extends JpaRepository<Profissional, Long> {
    
    Optional<Profissional> findByCrm(String crm);
    
    boolean existsByCrm(String crm);
    
    List<Profissional> findByEspecialidade(Especialidade especialidade);
    
    Optional<Profissional> findByUsuarioId(Long usuarioId);
}
