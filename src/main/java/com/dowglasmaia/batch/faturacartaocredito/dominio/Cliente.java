package com.dowglasmaia.batch.faturacartaocredito.dominio;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {

    private int id;
    private String nome;
    private  String endereco;

}
