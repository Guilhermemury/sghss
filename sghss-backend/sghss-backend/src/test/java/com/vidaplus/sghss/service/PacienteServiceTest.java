package com.vidaplus.sghss.service;

import com.vidaplus.sghss.dto.PacienteRequest;
import com.vidaplus.sghss.entity.Paciente;
import com.vidaplus.sghss.entity.Usuario;
import com.vidaplus.sghss.enums.Perfil;
import com.vidaplus.sghss.repository.PacienteRepository;
import com.vidaplus.sghss.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para o serviço de pacientes.
 */
@ExtendWith(MockitoExtension.class)
class PacienteServiceTest {

    @Mock
    private PacienteRepository pacienteRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private PacienteService pacienteService;

    private PacienteRequest pacienteRequest;

    @BeforeEach
    void setUp() {
        pacienteRequest = new PacienteRequest();
        pacienteRequest.setNome("João da Silva");
        pacienteRequest.setEmail("joao.silva@email.com");
        pacienteRequest.setSenha("senha123");
        pacienteRequest.setCpf("123.456.789-00");
        pacienteRequest.setDataNascimento(LocalDate.of(1990, 5, 15));
        pacienteRequest.setTelefone("(11) 98765-4321");
        pacienteRequest.setEndereco("Rua das Flores, 123");
    }

    @Test
    void deveCriarPacienteComSucesso() {
        // Arrange
        when(usuarioRepository.existsByEmail(anyString())).thenReturn(false);
        when(pacienteRepository.existsByCpf(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("senhaEncriptada");
        
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);
        
        Paciente paciente = new Paciente();
        paciente.setId(1L);
        when(pacienteRepository.save(any(Paciente.class))).thenReturn(paciente);

        // Act
        Paciente resultado = pacienteService.criarPaciente(pacienteRequest);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
        verify(pacienteRepository, times(1)).save(any(Paciente.class));
    }

    @Test
    void deveLancarExcecaoQuandoEmailJaExiste() {
        // Arrange
        when(usuarioRepository.existsByEmail(anyString())).thenReturn(true);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            pacienteService.criarPaciente(pacienteRequest);
        });
        
        verify(usuarioRepository, never()).save(any(Usuario.class));
        verify(pacienteRepository, never()).save(any(Paciente.class));
    }

    @Test
    void deveLancarExcecaoQuandoCpfJaExiste() {
        // Arrange
        when(usuarioRepository.existsByEmail(anyString())).thenReturn(false);
        when(pacienteRepository.existsByCpf(anyString())).thenReturn(true);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            pacienteService.criarPaciente(pacienteRequest);
        });
        
        verify(usuarioRepository, never()).save(any(Usuario.class));
        verify(pacienteRepository, never()).save(any(Paciente.class));
    }
}
