package com.barbearia.gerenciador;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/agendamentos") // Todos os pedidos que começarem com /agendamentos caem aqui
@CrossOrigin(origins = "http://localhost:5173")
public class AgendamentoController {

    @Autowired
    private AgendamentoService service;

    // O Garçom conhece o Gerente
    public AgendamentoController(AgendamentoService service) {
        this.service = service;
    }

    // Ação de Criar um novo agendamento
    @PostMapping
    public Agendamento criar(@RequestBody Agendamento novoAgendamento) {
        // O garçom pega o pedido do corpo da mensagem da internet e entrega pro gerente
        return service.criarAgendamento(novoAgendamento);
    }

    // Ação de Listar todos os agendamentos
    @GetMapping
    public List<Agendamento> listar() {
        // O garçom anota o pedido e pede a lista para o gerente
        return service.listarTodos();
    }

    // Ação de Cancelar um agendamento
    @PatchMapping("/{id}/cancelar")
    public Agendamento cancelar(@PathVariable Long id) {
        return service.cancelarAgendamento(id);
    }

    @GetMapping("/horarios-disponiveis")
    public ResponseEntity<List<String>> listarHorariosLivres(@RequestParam("data") LocalDate data) {
        
        // 1. O recepcionista pega a data e entrega para o cozinheiro (Service) trabalhar
        List<String> horarios = service.buscarHorariosDisponiveis(data);
        
        // 2. O recepcionista devolve a bandeja pronta com um carimbo de "OK" (Sucesso 200)
        return ResponseEntity.ok(horarios);
    }
}