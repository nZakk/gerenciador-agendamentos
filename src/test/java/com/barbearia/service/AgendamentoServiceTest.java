package com.barbearia.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.barbearia.gerenciador.Agendamento;
import com.barbearia.gerenciador.AgendamentoRepository;
import com.barbearia.gerenciador.AgendamentoService;

import java.util.Optional;

@ExtendWith(MockitoExtension.class) // Prepara o terreno para os dublês (Mocks)
public class AgendamentoServiceTest {

    @Mock
    private AgendamentoRepository repository; // O dublê do banco de dados

    @InjectMocks
    private AgendamentoService service; // O Service real, mas usando o dublê acima

    @Test
    @DisplayName("Deve impedir agendamento em data retroativa")
    void deveDarErroAoAgendarNoPassado() {
        // 1. Preparação: Criamos um agendamento com data de ontem
        Agendamento agendamentoNoPassado = new Agendamento();
        agendamentoNoPassado.setDataHora(LocalDateTime.now().minusDays(1));
        agendamentoNoPassado.setNomeCliente("Cliente Antigo");

        // 2. Execução e Validação:
        // Nós esperamos (assert) que o sistema jogue (Throws) um erro!
        assertThrows(IllegalArgumentException.class, () -> {
            service.criarAgendamento(agendamentoNoPassado);
        });

        // 3. Verificação Extra: Garante que o sistema nem tentou falar com o banco
        verifyNoInteractions(repository);
    }

    @Test
    @DisplayName("Deve salvar agendamento com sucesso quando os dados forem válidos")
    void deveSalvarAgendamentoComSucesso() {
        // 1. Preparação: Criamos um agendamento para AMANHÃ às 10:00 (Data válida!)
        Agendamento agendamentoValido = new Agendamento();
        agendamentoValido.setDataHora(LocalDateTime.now().plusDays(1).withHour(10).withMinute(0));
        agendamentoValido.setNomeCliente("Goku da Silva");
        agendamentoValido.setTipoServico("Cabelo");

        // O TRUQUE DO DUBLÊ: Instruímos o dublê do banco de dados.
        // "Quando (when) o service mandar salvar qualquer agendamento, devolva (thenReturn) esse mesmo agendamento com sucesso"
        when(repository.save(any(Agendamento.class))).thenReturn(agendamentoValido);

        // 2. Execução: Chamamos o método real do cozinheiro
        Agendamento resultado = service.criarAgendamento(agendamentoValido);

        // 3. Validação: Garantimos que o resultado não é nulo e que o nome está correto
        assertNotNull(resultado);
        assertEquals("Goku da Silva", resultado.getNomeCliente());

        // Verificação de segurança: O cozinheiro REALMENTE chamou o banco de dados?
        verify(repository).save(agendamentoValido);
    }

    @Test
    @DisplayName("Deve dar erro ao tentar cancelar um agendamento que já está cancelado")
    void deveDarErroAoCancelarAgendamentoJaCancelado() {
        // 1. Preparação: Criamos um agendamento que JÁ NASCEU cancelado
        Agendamento agendamentoCancelado = new Agendamento();
        agendamentoCancelado.setId(1L);
        agendamentoCancelado.setStatus("CANCELADO");

        // Ensinamos o dublê do banco: "Quando o service procurar pelo ID 1, devolva esse agendamento cancelado"
        when(repository.findById(1L)).thenReturn(Optional.of(agendamentoCancelado));

        // 2. Execução e Validação: O sistema TEM QUE gritar um erro
        assertThrows(IllegalArgumentException.class, () -> {
            service.cancelarAgendamento(1L);
        });
    }

    @Test
    @DisplayName("Deve cancelar um agendamento pendente com sucesso")
    void deveCancelarAgendamentoComSucesso() {
        // 1. Preparação: Criamos um agendamento que está PENDENTE
        Agendamento agendamentoPendente = new Agendamento();
        agendamentoPendente.setId(2L);
        agendamentoPendente.setStatus("PENDENTE");

        when(repository.findById(2L)).thenReturn(Optional.of(agendamentoPendente));
        // Quando o service mandar salvar o agendamento modificado, o dublê aceita e devolve ele mesmo
        when(repository.save(agendamentoPendente)).thenReturn(agendamentoPendente);

        // 2. Execução: Chamamos o método de cancelar
        Agendamento resultado = service.cancelarAgendamento(2L);

        // 3. Validação: O status mudou para CANCELADO de verdade?
        assertEquals("CANCELADO", resultado.getStatus());
    }
}