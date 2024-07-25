package com.dowglasmaia.batch.faturacartaocredito.writer;

import com.dowglasmaia.batch.faturacartaocredito.dominio.FaturaCartaoCredito;
import com.dowglasmaia.batch.faturacartaocredito.dominio.Transacao;
import org.springframework.batch.item.file.*;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.builder.MultiResourceItemWriterBuilder;
import org.springframework.batch.item.file.transform.LineAggregator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import java.io.IOException;
import java.io.Writer;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;

import static java.lang.String.format;

@Configuration
public class ArquivoFaturaCartaoCreditoWriterConfig {

    /**
     * Cria um MultiResourceItemWriter que escreve múltiplos arquivos de faturas de cartão de crédito,
     * dividindo-os em arquivos separados com base no limite de itens por arquivo.
     *
     * @return um MultiResourceItemWriter configurado
     */
    @Bean
    public MultiResourceItemWriter<FaturaCartaoCredito> arquivosFaturaCartaoCredito(){
        return new MultiResourceItemWriterBuilder<FaturaCartaoCredito>()
              .name("arquivosFaturaCartaoCredito")
              .resource(new FileSystemResource("files/fatura"))
              .itemCountLimitPerResource(1)
              .resourceSuffixCreator(suffixCreator())
              .delegate(arquivoFaturaCartaoCredito())
              .build();
    }

    /**
     * Cria um FlatFileItemWriter que escreve faturas de cartão de crédito em um arquivo de texto plano.
     *
     * @return um FlatFileItemWriter configurado
     */
    private FlatFileItemWriter<FaturaCartaoCredito> arquivoFaturaCartaoCredito(){
        return new FlatFileItemWriterBuilder<FaturaCartaoCredito>()
              .name("arquivoFaturaCartaoCredito")
              .resource(new FileSystemResource("files/fatura.txt"))
              .lineAggregator(lineAggregator())
              .headerCallback(headerCallback())
              .footerCallback(footerCallback())
              .build();
    }

    /**
     * Cria um callback para escrever o rodapé no arquivo.
     *
     * @return um FlatFileFooterCallback configurado
     */
    @Bean
    public FlatFileFooterCallback footerCallback(){
        return new TotalTransacoesFooterCallback();
    }

    /**
     * Cria um callback para escrever o cabeçalho no arquivo.
     *
     * @return um FlatFileHeaderCallback configurado
     */
    private FlatFileHeaderCallback headerCallback(){
        return new FlatFileHeaderCallback() {
            @Override
            public void writeHeader(Writer writer) throws IOException{
                writer.append(format("%121s\n", "Cartão XPTO"));
                writer.append(format("%121s\n\n", "Rua Verg, 021"));
            }
        };
    }

    /**
     * Cria um agregador de linhas que formata cada fatura de cartão de crédito como uma string.
     *
     * @return um LineAggregator configurado
     */
    private LineAggregator<FaturaCartaoCredito> lineAggregator(){
        return new LineAggregator<FaturaCartaoCredito>() {
            @Override
            public String aggregate(FaturaCartaoCredito faturaCartaoCredito){
                StringBuilder writer = new StringBuilder();
                writer.append(format("Nome: %s\n", faturaCartaoCredito.getCliente().getNome()));
                writer.append(format("Endereço: %s\n\n\n", faturaCartaoCredito.getCliente().getEndereco()));
                writer.append(format("Fatura completa do cartão: %d\n", faturaCartaoCredito.getCartaoCredito().getNumeroCartaoCredito()));
                writer.append("-----------------------------------------------------------------------------------------------------\n");
                writer.append("DATA          DESCRIÇÃO                                        VALOR\n");
                writer.append("-----------------------------------------------------------------------------------------------------\n");

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                NumberFormat numberFormat = NumberFormat.getCurrencyInstance();

                for (Transacao transacao : faturaCartaoCredito.getTransacoes()) {
                    writer.append(format("[%10s] %-80s %s\n",
                          dateFormat.format(transacao.getDate()),
                          transacao.getDescricao(),
                          numberFormat.format(transacao.getValor())));
                }
                return writer.toString();
            }
        };
    }

    /**
     * Cria um sufixo para os arquivos gerados, incrementando o índice de cada arquivo.
     *
     * @return um ResourceSuffixCreator configurado
     */
    private ResourceSuffixCreator suffixCreator(){
        return new ResourceSuffixCreator() {
            @Override
            public String getSuffix(int index){
                return index + ".txt";
            }
        };
    }
}
