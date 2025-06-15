package br.edu.ifpr.biblioteca_spring;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


import br.edu.ifpr.biblioteca_spring.models.Livro;
import br.edu.ifpr.biblioteca_spring.service.LivroService;

import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class LivroServiceTest {

    private LivroService service;

    @BeforeEach
    void setUp() {
        service = new LivroService();
        service.limpar(); // limpa antes de cada teste
    }

    @Test
    void adicionarLivro() {
        Livro livro = service.adicionar("1984", "George Orwell");

        assertNotNull(livro.getId());
        assertEquals("1984", livro.getTitulo());
        assertTrue(livro.isDisponivel());
    }

    @Test
    void listarLivrosDisponiveis() {
        Livro l1 = service.adicionar("Livro 1", "Autor A");
        Livro l2 = service.adicionar("Livro 2", "Autor B");
        l2.setDisponivel(false); // simula que foi emprestado

        List<Livro> todos = service.listarTodos();

        long disponiveis = todos.stream().filter(Livro::isDisponivel).count();

        assertEquals(1, disponiveis);
        assertTrue(todos.contains(l1));
        assertTrue(todos.contains(l2)); // l2 ainda está na lista geral
    }

    @Test
    void marcarLivroComoEmprestado() {
        Livro livro = service.adicionar("Dom Casmurro", "Machado de Assis");

        assertTrue(livro.isDisponivel());

        livro.setDisponivel(false); // simula empréstimo

        assertFalse(livro.isDisponivel());
    }

    @Test
    void marcarLivroComoDisponivel() {
        Livro livro = service.adicionar("A Moreninha", "Joaquim Manuel");

        livro.setDisponivel(false); // emprestado
        assertFalse(livro.isDisponivel());

        livro.setDisponivel(true); // devolvido
        assertTrue(livro.isDisponivel());
    }
}
