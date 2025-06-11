
package br.edu.ifpr.biblioteca_spring.service;

import org.springframework.stereotype.Service;

import br.edu.ifpr.biblioteca_spring.models.Livro;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class LivroService {

    private static final List<Livro> livros = new ArrayList<>();
    private static final AtomicLong idGenerator = new AtomicLong();

    public List<Livro> listarTodos() {
        return new ArrayList<>(livros);
    }

    public Optional<Livro> buscarPorId(Long id) {
        for (Livro l : livros) {
            if (l.getId().equals(id)) {
                return Optional.of(l);
            }
        }
        return Optional.empty();
    }

    public Livro adicionar(String titulo, String autor) {
        Livro l = new Livro(idGenerator.incrementAndGet(), titulo, autor);
        livros.add(l);
        return l;
    }

    public void limpar() {
        livros.clear();
    }
}
