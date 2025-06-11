
package br.edu.ifpr.biblioteca_spring.service;


import org.springframework.stereotype.Service;

import br.edu.ifpr.biblioteca_spring.models.Emprestimo;
import br.edu.ifpr.biblioteca_spring.models.Livro;
import br.edu.ifpr.biblioteca_spring.models.Usuario;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class EmprestimoService {

    private static final List<Emprestimo> emprestimos = new ArrayList<>();
    private static final AtomicLong idGenerator = new AtomicLong();

    public List<Emprestimo> listarTodos() {
        return new ArrayList<>(emprestimos);
    }

    public List<Emprestimo> Usuario(Usuario usuario) {
        List<Emprestimo> resultado = new ArrayList<>();
        for (Emprestimo e : emprestimos) {
            if (e.getUsuario().getId().equals(usuario.getId()) && e.getDataDevolucaoReal() == null) {
                resultado.add(e);
            }
        }
        return resultado;
    }

    public boolean podeEmprestar(Usuario usuario) {
        if (usuario.isBloqueado()) {
            return false;
        }

        int contador = 0;
        for (Emprestimo e : emprestimos) {
            if (e.getUsuario().getId().equals(usuario.getId()) && e.getDataDevolucaoReal() == null) {
                contador++;
            }
        }

        return contador < 3;
    }

    public LocalDate calcularDataDevolucao(LocalDate dataInicial) {
        LocalDate data = dataInicial.plusDays(7);
        while (data.getDayOfWeek() == DayOfWeek.SATURDAY || data.getDayOfWeek() == DayOfWeek.SUNDAY) {
            data = data.plusDays(1);
        }
        return data;
    }

    public Emprestimo emprestarLivro(Usuario usuario, Livro livro) {

        if (!podeEmprestar(usuario)) {
            throw new IllegalArgumentException("Usuário bloqueado ou com limite de livros atingido.");
        }

        if (!livro.isDisponivel()) {
            throw new IllegalArgumentException("Livro indisponível ou já emprestado.");
        }

        livro.setDisponivel(false);
        LocalDate hoje = LocalDate.now();
        LocalDate devolucao = calcularDataDevolucao(hoje);

        Emprestimo emp = new Emprestimo(
            idGenerator.incrementAndGet(), usuario, livro, hoje, devolucao
        );

        emprestimos.add(emp);
        return emp;
    }

    public Optional<String> devolverLivro(Long emprestimoId) {
        
        Emprestimo encontrado = null;
        
        for (Emprestimo e : emprestimos) {
            if (e.getId().equals(emprestimoId)) {
                encontrado = e;
                break;
            }
        }

        if (encontrado == null) {
            return Optional.of("Empréstimo não encontrado.");
        }

        if (encontrado.getDataDevolucaoReal() != null) {
            return Optional.of("Livro já devolvido.");
        }

        encontrado.setDataDevolucaoReal(LocalDate.now());
        encontrado.getLivro().setDisponivel(true);

        // Verifica atraso
        if (encontrado.getDataDevolucaoReal().isAfter(encontrado.getDataPrevistaDevolucao())) {
            long diasAtraso = encontrado.getDataPrevistaDevolucao()
                    .until(encontrado.getDataDevolucaoReal()).getDays();
            long diasBloqueio = 5 + diasAtraso;
            encontrado.getUsuario().bloquearAte(LocalDate.now().plusDays(diasBloqueio));
        }

        return Optional.empty();
    }

    public void limpar() {
        emprestimos.clear();
    }
}
