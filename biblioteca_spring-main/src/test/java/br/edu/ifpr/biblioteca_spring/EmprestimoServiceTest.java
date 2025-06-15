package br.edu.ifpr.biblioteca_spring;
import org.springframework.boot.test.context.SpringBootTest;

import br.edu.ifpr.biblioteca_spring.models.Emprestimo;
import br.edu.ifpr.biblioteca_spring.models.Livro;
import br.edu.ifpr.biblioteca_spring.models.Usuario;
import br.edu.ifpr.biblioteca_spring.service.EmprestimoService;

import org.junit.jupiter.api.*;

import java.time.DayOfWeek;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class EmprestimoServiceTest {

    private EmprestimoService service;

    @BeforeEach
    void setup() {
        service = new EmprestimoService();
        service.limpar();
    }

    @Test
    void permitirEmprestimo() {
        Usuario u = new Usuario();
        Livro l = new Livro(1L, "Título", "Autor");

        Emprestimo e = service.emprestarLivro(u, l);

        assertNotNull(e);
        assertEquals(u, e.getUsuario());
        assertEquals(l, e.getLivro());
        assertFalse(l.isDisponivel());
    }

    @Test
    void naoPermitirEmprestimoSeLivroIndisponivel() {
        Usuario u = new Usuario();
        Livro l = new Livro(1L, "Indisponível", "Autor");
        l.setDisponivel(false);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            service.emprestarLivro(u, l);
        });

        assertEquals("Livro indisponível ou já emprestado.", ex.getMessage());
    }

    @Test
    void naoPermitirEmprestimoSeUsuarioTiver3Emprestimos() {
        Usuario u = new Usuario();
        u.setId(1L);
        for (int i = 0; i < 3; i++) {
            Livro l = new Livro((long) i, "Livro " + i, "Autor");
            service.emprestarLivro(u, l);
        }

        Livro novo = new Livro(99L, "Novo Livro", "Autor");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            service.emprestarLivro(u, novo);
        });

        assertEquals("Usuário bloqueado ou com limite de livros atingido.", ex.getMessage());
    }

    @Test
    void naoPermitirEmprestimoSeUsuarioBloqueado() {
        Usuario u = new Usuario();
        u.bloquearAte(LocalDate.now().plusDays(3));
        Livro l = new Livro(1L, "Livro", "Autor");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            service.emprestarLivro(u, l);
        });

        assertEquals("Usuário bloqueado ou com limite de livros atingido.", ex.getMessage());
    }

    @Test
    void calcularDataDesbloqueio() {
        Usuario u = new Usuario();
        Livro l = new Livro(1L, "Livro", "Autor");

        Emprestimo e = service.emprestarLivro(u, l);

        // simula atraso de 2 dias
        e.setDataPrevistaDevolucao(LocalDate.now().minusDays(2));
        service.devolverLivro(e.getId());

        LocalDate esperado = LocalDate.now().plusDays(5 + 2);
        assertEquals(esperado, u.getDataDeDesbloqueio());
    }

    @Test
    void agendarDevolucao() {
        LocalDate sexta = LocalDate.of(2025, 5, 16); // sexta-feira
        LocalDate esperada = service.calcularDataDevolucao(sexta); // +7 = sexta -> sabado -> pula

        assertNotEquals(DayOfWeek.SATURDAY, esperada.getDayOfWeek());
        assertNotEquals(DayOfWeek.SUNDAY, esperada.getDayOfWeek());
    }
}