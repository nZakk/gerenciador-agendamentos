package com.barbearia.gerenciador;

import org.springframework.stereotype.Service;

@Service
public class AgendamentoService {

    private final AgendamentoRepository repository;

    // O Gerente contrata o Bibliotecário para trabalhar com ele
    public AgendamentoService(AgendamentoRepository repository) {
        this.repository = repository;
    }

    // Regra de Negócio: Como salvar um agendamento novo
    public Agendamento criarAgendamento(Agendamento novoAgendamento) {
        
        // Clean Code: Usamos nomes claros (novoAgendamento) para saber exatamente o que a variável faz.
        novoAgendamento.setStatus("PENDENTE"); // Todo agendamento nasce como pendente!
        
        // O gerente pede ao bibliotecário para salvar no banco
        return repository.save(novoAgendamento); 
    }
}