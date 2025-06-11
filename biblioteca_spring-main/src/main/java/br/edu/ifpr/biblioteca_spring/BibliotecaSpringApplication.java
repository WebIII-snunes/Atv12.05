package br.edu.ifpr.biblioteca_spring;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import br.edu.ifpr.biblioteca_spring.models.Usuario;
import br.edu.ifpr.biblioteca_spring.service.EmprestimoService;
import br.edu.ifpr.biblioteca_spring.service.LivroService;
import br.edu.ifpr.biblioteca_spring.service.UsuariosService;

@SpringBootApplication
public class BibliotecaSpringApplication {

		public static void main(String[] args) {
			SpringApplication.run(BibliotecaSpringApplication.class, args);
		}

		@Bean
		CommandLineRunner initDatabase(LivroService livroService, UsuariosService usuarioService, EmprestimoService emprestimoService) {
			return args -> {
				// Criando livros
				livroService.adicionar("Hamlet", "William Shakespeare");
				livroService.adicionar("Os Miseráveis", "Victor Hugo");
				livroService.adicionar("1984", "George Orwell");
				livroService.adicionar("Cem Anos de Solidão", "Gabriel García Márquez");
				livroService.adicionar("Ensaio sobre a Cegueira", "José Saramago");

				// Criando usuários
				usuarioService.adicionar(new Usuario(null, "John Doe", "00000000000"));
				usuarioService.adicionar(new Usuario(null, "Joana Doe", "99999999999"));

				//Criando empréstimos
				emprestimoService.emprestarLivro(usuarioService.buscarPorId(1L).get(), livroService.buscarPorId(1L).get());
				emprestimoService.emprestarLivro(usuarioService.buscarPorId(2L).get(), livroService.buscarPorId(2L).get());
			};
		}

}
