package com.dowglasmaia.batch.faturacartaocredito.reader;

import com.dowglasmaia.batch.faturacartaocredito.dominio.FaturaCartaoCredito;
import com.dowglasmaia.batch.faturacartaocredito.dominio.Transacao;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamReader;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class FaturaCartaoCreditoReader implements ItemStreamReader<FaturaCartaoCredito> {
    private ItemStreamReader<Transacao> delegate;
    private Transacao transacaoAtual;

    /**
     * Construtor para inicializar o leitor com o delegado de Transação.
     * @param delegate ItemStreamReader para Transacao.
     */
    public FaturaCartaoCreditoReader(ItemStreamReader<Transacao> delegate){
        this.delegate = delegate;
    }


    /**
     * Lê uma FaturaCartaoCredito, agregando transações relacionadas.
     * @return FaturaCartaoCredito ou null se não houver mais transações.
     * @throws Exception em caso de erro durante a leitura.
     */
    @Override
    public FaturaCartaoCredito read() throws Exception{
        if (isNull(transacaoAtual))
            transacaoAtual = delegate.read();

        FaturaCartaoCredito faturaCartaoCredito = null;
        Transacao transacao = transacaoAtual;

        if (nonNull(transacao)) {
            faturaCartaoCredito = new FaturaCartaoCredito();
            faturaCartaoCredito.setCartaoCredito(transacao.getCartaoCredito());
            faturaCartaoCredito.setCliente(transacao.getCartaoCredito().getCliente());
            faturaCartaoCredito.getTransacoes().add(transacao);

            while (isTransacaoRelacionada(transacao))
                faturaCartaoCredito.getTransacoes().add(transacao);
        }

        return faturaCartaoCredito;
    }

    /**
     * Verifica se a transação atual está relacionada com a transação lida.
     * @param transacao Transação atual.
     * @return true se as transações estão relacionadas, false caso contrário.
     * @throws Exception em caso de erro durante a leitura.
     */
    private boolean isTransacaoRelacionada(Transacao transacao) throws Exception{
        return peek() != null && transacao.getCartaoCredito().getNumeroCartaoCredito() == transacaoAtual.getCartaoCredito().getNumeroCartaoCredito();
    }


    /**
     * Lê a próxima transação sem avançar o ponteiro.
     * @return A próxima transação.
     * @throws Exception em caso de erro durante a leitura.
     */
    private Transacao peek() throws Exception{
        transacaoAtual = delegate.read();
        return transacaoAtual;
    }

    /**
     * Abre o leitor com o contexto de execução.
     * @param executionContext Contexto de execução.
     * @throws ItemStreamException em caso de erro ao abrir o leitor.
     */
    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException{
        delegate.open(executionContext);

    }

    /**
     * Atualiza o contexto de execução.
     * @param executionContext Contexto de execução.
     * @throws ItemStreamException em caso de erro ao atualizar o contexto.
     */
    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException{
        delegate.update(executionContext);
    }

    /**
     * Fecha o leitor.
     * @throws ItemStreamException em caso de erro ao fechar o leitor.
     */
    @Override
    public void close() throws ItemStreamException{
        delegate.close();
    }
}
