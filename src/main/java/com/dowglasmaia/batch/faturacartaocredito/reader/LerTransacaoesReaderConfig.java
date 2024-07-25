package com.dowglasmaia.batch.faturacartaocredito.reader;

import com.dowglasmaia.batch.faturacartaocredito.dominio.CartaoCredito;
import com.dowglasmaia.batch.faturacartaocredito.dominio.Cliente;
import com.dowglasmaia.batch.faturacartaocredito.dominio.Transacao;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

@Configuration
public class LerTransacaoesReaderConfig {


    /**
     * Configura o leitor de transações a partir de uma fonte de dados JDBC.
     *
     * @param dataSource a fonte de dados configurada no aplicativo
     * @return um leitor de transações configurado
     */
    @Bean
    public JdbcCursorItemReader<Transacao> letTransacaoReader(
          @Qualifier("appDataSource") DataSource dataSource
    ){
        return new JdbcCursorItemReaderBuilder<Transacao>()
              .name("letTransacaoReader")
              .dataSource(dataSource)
              .sql(sqlCartaoBuilder())
              .rowMapper(rowMapperTransacao())
              .build();
    }

    /**
     * Constrói a consulta SQL para recuperar transações e unir com cartões de crédito.
     *
     * @return a string SQL
     */
    private static String sqlCartaoBuilder(){
        StringBuilder sql = new StringBuilder();
        sql.append("select * from transacao ");
        sql.append("join cartao_credito using (numero_cartao_credito) ");
        sql.append("order by numero_cartao_credito");
        return sql.toString();
    }

    /**
     * Mapeia as linhas do resultado da consulta SQL para objetos Transacao.
     *
     * @return um RowMapper configurado para Transacao
     */
    private RowMapper<Transacao> rowMapperTransacao(){
        return new RowMapper<Transacao>() {
            @Override
            public Transacao mapRow(ResultSet rs, int rowNum) throws SQLException{
                Cliente cliente = Cliente.builder()
                      .id(rs.getInt("cliente"))
                      .build();

                CartaoCredito cartaoCredito = CartaoCredito.builder()
                      .numeroCartaoCredito(rs.getInt("numero_cartao_credito"))
                      .cliente(cliente)
                      .build();

                Transacao transacao = Transacao.builder()
                      .id(rs.getInt("id"))
                      .valor(rs.getDouble("valor"))
                      .date(rs.getDate("data"))
                      .cartaoCredito(cartaoCredito)
                      .descricao(rs.getString("descricao"))
                      .build();

                return transacao;
            }
        };
    }
}
