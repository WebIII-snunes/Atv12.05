
package br.edu.ifpr.biblioteca_spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.edu.ifpr.biblioteca_spring.models.Livro;
import br.edu.ifpr.biblioteca_spring.models.Usuario;
import br.edu.ifpr.biblioteca_spring.service.EmprestimoService;
import br.edu.ifpr.biblioteca_spring.service.LivroService;
import br.edu.ifpr.biblioteca_spring.service.UsuariosService;

import java.util.Optional;

@Controller
@RequestMapping("/emprestimos")
public class EmprestimoController {

    @Autowired
    private EmprestimoService emprestimoService;

    @Autowired
    private LivroService livrosService;

    @Autowired
    private UsuariosService usuarioService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("emprestimos", emprestimoService.listarTodos());
        return "emprestimos/lista";
    }

    @GetMapping("/novo")
    public String formularioNovoEmprestimo(Model model) {
        model.addAttribute("livros", livrosService.listarTodos());
        model.addAttribute("usuarios", usuarioService.listarTodos());
        return "emprestimos/form";
    }

    @PostMapping
    public String emprestar(@RequestParam Long usuarioId, @RequestParam Long livroId, Model model, RedirectAttributes redirectAttrs) {
        
        Optional<Usuario> usuario = usuarioService.buscarPorId(usuarioId);
        Optional<Livro> livro = livrosService.buscarPorId(livroId);

        if (usuario.isPresent() && livro.isPresent()) {
            
            try {
                
                emprestimoService.emprestarLivro(usuario.get(), livro.get());
                return "redirect:/emprestimos";

            } catch (Exception e) {
                redirectAttrs.addFlashAttribute("erro", e.getMessage());

            }

        } else {
            redirectAttrs.addFlashAttribute("erro", "Usuário ou Livro não encontrados.");
        }

        return "redirect:emprestimos/novo";
    }

    @GetMapping("/devolucao")
    public String devolver(@RequestParam Long emprestimoId, Model model) {
        Optional<String> resultado = emprestimoService.devolverLivro(emprestimoId);
        if (resultado.isEmpty()) {
            return "redirect:/emprestimos";
        } else {
            model.addAttribute("erro", resultado.get());
            return "redirect:/emprestimos";
        }
    }
}
