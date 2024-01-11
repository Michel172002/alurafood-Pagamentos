package br.alurafood.pagamentos.service;

import br.alurafood.pagamentos.dto.PagamentoDto;
import br.alurafood.pagamentos.http.PedidoClient;
import br.alurafood.pagamentos.model.Pagamento;
import br.alurafood.pagamentos.model.Status;
import br.alurafood.pagamentos.repository.PagamentoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PagamentoService {
    @Autowired
    private PagamentoRepository pagamentoRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PedidoClient pedidoClient;

    public Page<PagamentoDto> obterTodos(Pageable pageable){
        return pagamentoRepository.findAll(pageable).map(p -> modelMapper.map(p, PagamentoDto.class));
    }

    public PagamentoDto obterPorId(Long id){
        Pagamento pagamento = pagamentoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException());

        return modelMapper.map(pagamento, PagamentoDto.class);
    }

    public PagamentoDto criarPagamento(PagamentoDto dto){
        Pagamento pagamento = modelMapper.map(dto, Pagamento.class);
        pagamento.setStatus(Status.CRIADO);
        pagamentoRepository.save(pagamento);

        return modelMapper.map(pagamento, PagamentoDto.class);
    }

    public PagamentoDto atualizarPagamento(Long id, PagamentoDto dto){
        Pagamento pagamento = modelMapper.map(dto, Pagamento.class);
        pagamento.setId(id);
        pagamento = pagamentoRepository.save(pagamento);

        return modelMapper.map(pagamento, PagamentoDto.class);
    }

    public void excluirPagamento(Long id){
        pagamentoRepository.deleteById(id);
    }

    public void confirmarPagamento(Long id){
        Optional<Pagamento> pagamento = pagamentoRepository.findById(id);

        if(!pagamento.isPresent()){
            throw new EntityNotFoundException();
        }

        pagamento.get().setStatus(Status.CONFIRMADO);
        pagamentoRepository.save(pagamento.get());
        pedidoClient.atualizarPagamento(pagamento.get().getPedidoId());
    }

    public void alteraStatus(Long id) {
        Optional<Pagamento> pagamento = pagamentoRepository.findById(id);

        if(!pagamento.isPresent()){
            throw new EntityNotFoundException();
        }

        pagamento.get().setStatus(Status.CONFIRMADO_SEM_INTEGRACAO);
        pagamentoRepository.save(pagamento.get());
    }
}
