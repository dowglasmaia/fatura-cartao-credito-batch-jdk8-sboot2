package com.dowglasmaia.batch.faturacartaocredito.writer;

import com.dowglasmaia.batch.faturacartaocredito.dominio.FaturaCartaoCredito;
import org.springframework.batch.core.annotation.AfterChunk;
import org.springframework.batch.core.annotation.BeforeWrite;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.item.file.FlatFileFooterCallback;

import java.io.IOException;
import java.io.Writer;
import java.text.NumberFormat;
import java.util.List;

public class TotalTransacoesFooterCallback implements FlatFileFooterCallback {

    private Double total = 0.0;

    /**
     * Método chamado para escrever o rodapé no arquivo de saída.
     * @param writer Writer para o arquivo de saída.
     * @throws IOException Se ocorrer um erro de escrita.
     */
    @Override
    public void writeFooter(Writer writer) throws IOException{
        writer.write(String.format("\n%121s", "Total: " + NumberFormat.getCurrencyInstance().format(total)));

    }

    /**
     * Método chamado antes de escrever um chunk para calcular o total das transações.
     * @param faturas Lista de faturas de cartão de crédito.
     */
    @BeforeWrite
    public void beforeWrite(List<FaturaCartaoCredito> faturas){
        for (FaturaCartaoCredito faturaCartaoCredito : faturas)
            total += faturaCartaoCredito.getTotal();
    }

    /**
     * Método chamado após a escrita de um chunk para resetar o total.
     * @param chunkContext Contexto do chunk.
     */
    @AfterChunk
    public void afterChunk(ChunkContext chunkContext){
        total = 0.0;
    }
}
