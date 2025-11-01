package com.vidaplus.sghss.service;

import com.vidaplus.sghss.dto.PacienteRequest;
import com.vidaplus.sghss.entity.Paciente;
import com.vidaplus.sghss.entity.Usuario;
import com.vidaplus.sghss.enums.Perfil;
import com.vidaplus.sghss.exception.ResourceNotFoundException;
import com.vidaplus.sghss.repository.PacienteRepository;
import com.vidaplus.sghss.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Serviço para gerenciamento de pacientes.
 */
@Service
public class PacienteService {

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Cria um novo paciente no sistema.
     */
    @Transactional
    public Paciente criarPaciente(PacienteRequest request) {
        // Verifica se o email já está em uso
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email já cadastrado");
        }

        // Verifica se o CPF já está em uso
        if (pacienteRepository.existsByCpf(request.getCpf())) {
            throw new IllegalArgumentException("CPF já cadastrado");
        }

        // Cria o usuário
        Usuario usuario = new Usuario();
        usuario.setNome(request.getNome());
        usuario.setEmail(request.getEmail());
        usuario.setSenha(passwordEncoder.encode(request.getSenha()));
        
        Set<Perfil> perfis = new HashSet<>();
        perfis.add(Perfil.ROLE_PACIENTE);
        usuario.setPerfis(perfis);
        
        usuario = usuarioRepository.save(usuario);

        // Cria o paciente
        Paciente paciente = new Paciente();
        paciente.setCpf(request.getCpf());
        paciente.setDataNascimento(request.getDataNascimento());
        paciente.setTelefone(request.getTelefone());
        paciente.setEndereco(request.getEndereco());
        paciente.setUsuario(usuario);

        return pacienteRepository.save(paciente);
    }

    /**
     * Lista todos os pacientes.
     */
    public List<Paciente> listarTodos() {
        return pacienteRepository.findAll();
    }

    /**
     * Busca um paciente por ID.
     */
    public Paciente buscarPorId(Long id) {
        return pacienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente não encontrado"));
    }

    /**
     * Busca um paciente por CPF.
     */
    public Paciente buscarPorCpf(String cpf) {
        return pacienteRepository.findByCpf(cpf)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente não encontrado"));
    }
}
