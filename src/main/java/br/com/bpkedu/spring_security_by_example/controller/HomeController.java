package br.com.bpkedu.spring_security_by_example.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/home")
    public String home(Model model, Authentication authentication) {
        // Adiciona informações do usuário ao modelo se necessário
        model.addAttribute("username", authentication.getName());
        model.addAttribute("roles", authentication.getAuthorities());
        return "home";
    }

    @GetMapping("/")
    public String index() {
        // Redireciona a raiz para a página home
        return "redirect:/home";
    }
} 