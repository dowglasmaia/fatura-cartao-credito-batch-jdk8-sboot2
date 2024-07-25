package com.dowglasmaia.batch.faturacartaocredito.job;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuração do Job de Fatura de Cartão de Crédito.
 *
 * Esta classe configura o Job Spring Batch para processar as faturas de cartão de crédito.
 * Utiliza a anotação @EnableBatchProcessing para habilitar o processamento em lote do Spring Batch.
 */
@EnableBatchProcessing
@Configuration
public class FaturaCartaoCreditoJobConfig {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    /**
     * Cria e configura o Job de fatura de cartão de crédito.
     *
     * @param faturaCartaoCreditoStep o Step que será executado pelo Job.
     * @return o Job configurado.
     */
    @Bean
    public Job faturaCartaoCreditoJob(Step faturaCartaoCreditoStep){
        return jobBuilderFactory
              .get("faturaCartaoCreditoJob")
              .start(faturaCartaoCreditoStep)
              .incrementer(new RunIdIncrementer())
              .build();
    }

}
