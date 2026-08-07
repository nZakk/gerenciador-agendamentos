package com.barbearia.gerenciador;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class AgendamentoService {

    private static final Logger log = LoggerFactory.getLogger(AgendamentoService.class);

    private final AgendamentoRepository repository;

    public AgendamentoService(AgendamentoRepository repository) {
        this.repository = repository;
    }

    public Agendamento criarAgendamento(Agendamento novoAgendamento) {
        
        log.info("Iniciando a criação de agendamento para o cliente: {}", novoAgendamento.getNomeCliente());

        if (novoAgendamento.getNomeCliente() == null || novoAgendamento.getNomeCliente().trim().isEmpty()) {
            log.warn("Tentativa de agendamento bloqueada: Nome do cliente está vazio.");
            throw new IllegalArgumentException("O nome do cliente é obrigatório.");
        }

        if (novoAgendamento.getTipoServico() == null || novoAgendamento.getTipoServico().trim().isEmpty()) {
            log.warn("Tentativa de agendamento bloqueada: Serviço não informado.");
            throw new IllegalArgumentException("O tipo de serviço é obrigatório.");
        }

        if (novoAgendamento.getDataHora() != null && novoAgendamento.getDataHora().isBefore(LocalDateTime.now())) {
            log.warn("Tentativa de agendamento bloqueada: Data no passado ({}).", novoAgendamento.getDataHora());
            throw new IllegalArgumentException("Não é possível realizar um agendamento no passado.");
        }

        novoAgendamento.setStatus("PENDENTE");
        
        Agendamento agendamentoSalvo = repository.save(novoAgendamento);
        
        log.info("Agendamento criado com sucesso! ID gerado: {}", agendamentoSalvo.getId());

        return agendamentoSalvo;
    }

    // Regra de Negócio: Listar todos os agendamentos
    public List<Agendamento> listarTodos() {
        // O gerente pede para o bibliotecário trazer todas as fichas da gaveta
        return repository.findAll();
    }

    // Regra de Negócio: Cancelar um agendamento
    public Agendamento cancelarAgendamento(Long id) {
        // 1. O gerente pede para o bibliotecário procurar a ficha pelo ID
        Agendamento agendamento = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Agendamento não encontrado com o ID: " + id));

        // 2. Verifica se já está cancelado
        if ("CANCELADO".equals(agendamento.getStatus())) {
            throw new IllegalArgumentException("Este agendamento já está cancelado.");
        }

        // 3. Muda o status
        agendamento.setStatus("CANCELADO");

        // 4. Salva a ficha atualizada de volta na gaveta
        return repository.save(agendamento);
    }

    public List<String> buscarHorariosDisponiveis(LocalDate data) {
        
        // 1. A nossa lista "mãe" fixa
        List<String> todosHorarios = List.of(
            "08:00", "09:00", "10:00", "11:00", "12:00", 
            "13:00", "14:00", "15:00", "16:00", "17:00"
        );

        // O nosso "quadro branco" que podemos apagar
        List<String> horariosLivres = new ArrayList<>(todosHorarios);

        // 2. Definimos o que é o "começo" e o "fim" do dia escolhido
        LocalDateTime inicioDoDia = data.atStartOfDay(); // Fica: 26/05/2026 00:00:00
        LocalDateTime fimDoDia = data.atTime(23, 59, 59); // Fica: 26/05/2026 23:59:59

        // 3. Pedimos ao banco todos os agendamentos marcados dentro desse intervalo
        List<Agendamento> agendamentosDoDia = repository.findByDataHoraBetween(inicioDoDia, fimDoDia);

        // 4. A SUA LÓGICA: Passar horário por horário apagando os ocupados
        for (Agendamento agendamento : agendamentosDoDia) {
            
            // Pegamos a hora do agendamento (ex: 10:00) e transformamos em Texto (String)
            String horaOcupada = agendamento.getDataHora().toLocalTime().toString(); 
            
            // Apagamos do nosso quadro branco!
            horariosLivres.remove(horaOcupada);
        }

        // 5. Devolvemos a bandeja pronta, apenas com os horários que sobraram
        return horariosLivres;
    }
}