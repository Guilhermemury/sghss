package com.vidaplus.sghss.security;

import com.vidaplus.sghss.entity.Usuario;
import com.vidaplus.sghss.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Serviço customizado para carregar os detalhes do usuário durante a autenticação.
 * Implementa a interface UserDetailsService do Spring Security.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + email));

        return new User(
                usuario.getEmail(),
                usuario.getSenha(),
                getAuthorities(usuario)
        );
    }

    /**
     * Converte os perfis do usuário em authorities do Spring Security.
     */
    private Collection<? extends GrantedAuthority> getAuthorities(Usuario usuario) {
        return usuario.getPerfis().stream()
                .map(perfil -> new SimpleGrantedAuthority(perfil.name()))
                .collect(Collectors.toList());
    }
}
