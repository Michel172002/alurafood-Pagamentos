package br.alurafood.pagamentos.dto;

import br.alurafood.pagamentos.model.Pagamento;
import br.alurafood.pagamentos.model.Status;

import java.math.BigDecimal;

public record PagamentoDto(
        Long id,
        BigDecimal valor,
        String nome,
        String numero,
        String expiracao,
        String codigo,
        Status status,
        Long formaDePagamentoId,
        Long pedidoId
) {
    public PagamentoDto(Pagamento pagamento) {
        this(pagamento.getId(), pagamento.getValor(), pagamento.getNome(), pagamento.getNumero(), pagamento.getExpiracao(),
                pagamento.getCodigo(), pagamento.getStatus(), pagamento.getFormaDePagamentoId(), pagamento.getPedidoId());
    }
}
