package com.vidaplus.sghss.repository;

import com.vidaplus.sghss.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositório para a entidade Usuario.
 * Fornece métodos para acesso aos dados de usuários no banco de dados.
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    /**
     * Busca um usuário pelo email.
     */
    Optional<Usuario> findByEmail(String email);
    
    /**
     * Verifica se existe um usuário com o email fornecido.
     */
    boolean existsByEmail(String email);
}
