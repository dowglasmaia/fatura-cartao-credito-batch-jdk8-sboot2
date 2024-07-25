package com.dowglasmaia.batch.faturacartaocredito.dominio;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transacao {

    private int id;
    private CartaoCredito cartaoCredito;
    private String descricao;
    private Double valor;
    private Date date;

}
