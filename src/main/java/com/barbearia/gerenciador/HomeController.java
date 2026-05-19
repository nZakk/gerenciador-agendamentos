package com.barbearia.gerenciador;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/home")
    public String boasVindas() {
        return "Olá! O servidor da Barbearia está rodando com sucesso!";
    }
}