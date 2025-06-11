package br.edu.ifpr.biblioteca_spring.service;

import org.springframework.stereotype.Service;

import br.edu.ifpr.biblioteca_spring.models.Usuario;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class UsuariosService {

    private static final List<Usuario> usuarios = new ArrayList<>();
    private static final AtomicLong idGenerator = new AtomicLong();

    public List<Usuario> listarTodos() {
        return new ArrayList<>(usuarios);
    }

    public Optional<Usuario> buscarPorId(Long id) {
        for (Usuario u : usuarios) {
            if (u.getId().equals(id)) {
                return Optional.of(u);
            }
        }
        throw new IllegalArgumentException("Usuario inexistente.");
    }

    public Usuario adicionar(Usuario usuario) {

        usuario.setId(idGenerator.incrementAndGet());
        usuarios.add(usuario);
        return usuario;
    }

    public void limpar() {
        usuarios.clear();
    }
}
