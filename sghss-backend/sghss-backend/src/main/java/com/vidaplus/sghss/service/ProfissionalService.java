package com.vidaplus.sghss.service;

import com.vidaplus.sghss.dto.ProfissionalRequest;
import com.vidaplus.sghss.entity.Profissional;
import com.vidaplus.sghss.entity.Usuario;
import com.vidaplus.sghss.enums.Perfil;
import com.vidaplus.sghss.repository.ProfissionalRepository;
import com.vidaplus.sghss.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
public class ProfissionalService {

    @Autowired
    private ProfissionalRepository profissionalRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public Profissional cadastrarNovoProfissional(ProfissionalRequest dados) {

        if (usuarioRepository.existsByEmail(dados.email())) {
            throw new IllegalArgumentException("Erro: O email informado já está em uso.");
        }

        if (profissionalRepository.existsByCrm(dados.crm())) {
            throw new IllegalArgumentException("Erro: O CRM informado já está cadastrado.");
        }

        Usuario novoUsuario = new Usuario();
        novoUsuario.setEmail(dados.email());
        novoUsuario.setSenha(passwordEncoder.encode(dados.senha()));


        novoUsuario.setPerfis(Set.of(Perfil.ROLE_PROFISSIONAL));

        novoUsuario.setNome(dados.nome());

        Usuario usuarioSalvo = usuarioRepository.save(novoUsuario);

        Profissional novoProfissional = new Profissional();
        novoProfissional.setNome(dados.nome());
        novoProfissional.setCrm(dados.crm());
        novoProfissional.setEspecialidade(dados.especialidade());
        novoProfissional.setUsuario(usuarioSalvo);

        return profissionalRepository.save(novoProfissional);
    }
}