package br.edu.ifpr.biblioteca_spring;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;



import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class BibliotecaTest {

    @Autowired
    private MockMvc mockMvc;

   

    @Test
    void cadastrarUsuario() throws Exception {
        mockMvc.perform(post("/usuarios")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("nome", "João da Silva")
            .param("cpf", "123.456.789-00"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/usuarios"));
         }

    @Test
    void realizarEmprestimo() throws Exception {
       
        // Realizar empréstimo
        mockMvc.perform(post("/emprestimos/novo")
                .param("usuarioId", "1")
                .param("livroId", "1"));
    }

    @Test
    void realizarDevolucao() throws Exception {
        // Supondo que exista um empréstimo com ID 1
        mockMvc.perform(post("/devolucao/1"))
                .andExpect(status().isOk());
                
    }
}