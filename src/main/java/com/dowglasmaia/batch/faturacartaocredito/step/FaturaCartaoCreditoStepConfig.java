package com.dowglasmaia.batch.faturacartaocredito.step;

import com.dowglasmaia.batch.faturacartaocredito.dominio.FaturaCartaoCredito;
import com.dowglasmaia.batch.faturacartaocredito.dominio.Transacao;
import com.dowglasmaia.batch.faturacartaocredito.reader.FaturaCartaoCreditoReader;
import com.dowglasmaia.batch.faturacartaocredito.writer.TotalTransacoesFooterCallback;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FaturaCartaoCreditoStepConfig {


    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    /**
     * Configura o passo de processamento de faturas de cartão de crédito.
     *
     * @param letTransacaoReader o leitor de transações
     * @param cartaoCreditoItemProcessor o processador de faturas de cartão de crédito
     * @param cartaoCreditoItemWriter o escritor de faturas de cartão de crédito
     * @param listener o callback de rodapé para calcular o total das transações
     * @return o passo configurado
     */
    @Bean
    public Step faturaCartaoCreditoStep(
          ItemStreamReader<Transacao> letTransacaoReader,
          ItemProcessor<FaturaCartaoCredito, FaturaCartaoCredito> cartaoCreditoItemProcessor,
          ItemWriter<FaturaCartaoCredito> cartaoCreditoItemWriter,
          TotalTransacoesFooterCallback listener
    ){
        return stepBuilderFactory
              .get("faturaCartaoCreditoStep")
              .<FaturaCartaoCredito, FaturaCartaoCredito>chunk(1)
              .reader(new FaturaCartaoCreditoReader(letTransacaoReader))  // Define o leitor customizado para ler as transações
              .processor(cartaoCreditoItemProcessor)  // Define o processador customizado para processar as faturas de cartão de crédito
              .writer(cartaoCreditoItemWriter) // Define o escritor customizado para escrever as faturas de cartão de crédito
              .listener(listener)  // Define o listener customizado para calcular o total das transações
              .build();
    }

}
