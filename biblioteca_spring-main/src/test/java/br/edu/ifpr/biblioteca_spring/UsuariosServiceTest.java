package br.edu.ifpr.biblioteca_spring;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import br.edu.ifpr.biblioteca_spring.models.Usuario;
import org.junit.jupiter.api.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import br.edu.ifpr.biblioteca_spring.service.UsuariosService;

@SpringBootTest
class UsuariosServiceTest {

    private UsuariosService service;

    @BeforeEach
    void setUp() {
        service = new UsuariosService();
        service.limpar(); // limpa antes de cada teste
    }

    @Test
    void adicionarPessoa() {
        Usuario usuario = new Usuario();
        usuario.setNome("Carlos");

        Usuario adicionado = service.adicionar(usuario);

        assertNotNull(adicionado.getId());
        assertEquals("Carlos", adicionado.getNome());
    }

    @Test
    void pessoaPorIdExistente() {
        Usuario usuario = new Usuario();
        usuario.setNome("Ana");

        Usuario salvo = service.adicionar(usuario);
        Optional<Usuario> resultado = service.buscarPorId(salvo.getId());

        assertTrue(resultado.isPresent());
        assertEquals("Ana", resultado.get().getNome());
    }

    @Test
    void buscarIdInexistente() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            service.buscarPorId(999L);
        });

        assertEquals("Usuario inexistente.", ex.getMessage());
    }
}
