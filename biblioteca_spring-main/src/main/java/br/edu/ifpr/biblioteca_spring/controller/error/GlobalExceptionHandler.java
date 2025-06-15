package br.edu.ifpr.biblioteca_spring.controller.error;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    
@ExceptionHandler(Exception.class)
    public String handleException(Exception ex, Model model) { 
        model.addAttribute("erro", "Um problema ocorreu");
        model.addAttribute("descricao", ex.getMessage());

        return "error/error"; 
    }

}