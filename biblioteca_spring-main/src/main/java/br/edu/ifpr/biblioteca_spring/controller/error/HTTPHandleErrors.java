package br.edu.ifpr.biblioteca_spring.controller.error;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class HTTPHandleErrors implements ErrorController{
    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model){

        Object httpStatus = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Integer httpStatusCode = Integer.parseInt(httpStatus.toString());

        if(request != null){

            if (httpStatusCode == HttpStatus.NOT_FOUND.value()) {

                model.addAttribute("erro", "página não encontrada");
                model.addAttribute("descricao", "a página que você procurava não existe ou foi removida");
                
            }

            else if (httpStatusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {

                model.addAttribute("erro", "Um problema ocorreu");
                model.addAttribute("descricao", "tente novamente mais tarde");
                
            } else {

                model.addAttribute("erro", "Houve um problema inesperado");
                model.addAttribute("descricao", "estamos trabalhando para ajustá-lo");

            }
        }

        return "error/error";
    }

}