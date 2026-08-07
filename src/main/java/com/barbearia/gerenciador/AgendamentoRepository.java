package com.barbearia.gerenciador;

import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {
    
    //O spring so de ler a palavra "Between" (Entre), ele escreve o comando SQL "SELECT * FROM agendamentos WHERE data_hora BETWEEN..." sozinho!
    List<Agendamento> findByDataHoraBetween(LocalDateTime inicio, LocalDateTime fim);

}