package com.barbearia.gerenciador;

import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class AgendamentoService {

    private final AgendamentoRepository repository;

    public AgendamentoService(AgendamentoRepository repository) {
        this.repository = repository;
    }

    public Agendamento criarAgendamento(Agendamento novoAgendamento) {
        
        // 1. Validação: O nome não pode estar vazio
        if (novoAgendamento.getNomeCliente() == null || novoAgendamento.getNomeCliente().trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do cliente é obrigatório.");
        }

        // 2. Validação: O serviço não pode estar vazio
        if (novoAgendamento.getTipoServico() == null || novoAgendamento.getTipoServico().trim().isEmpty()) {
            throw new IllegalArgumentException("O tipo de serviço é obrigatório.");
        }

        // 3. Validação: Não pode agendar no passado
        if (novoAgendamento.getDataHora() != null && novoAgendamento.getDataHora().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Não é possível realizar um agendamento no passado.");
        }

        novoAgendamento.setStatus("PENDENTE");
        
        return repository.save(novoAgendamento); 
    }
}