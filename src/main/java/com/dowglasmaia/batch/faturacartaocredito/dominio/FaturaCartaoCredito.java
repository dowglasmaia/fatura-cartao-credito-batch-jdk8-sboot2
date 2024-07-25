package com.dowglasmaia.batch.faturacartaocredito.dominio;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FaturaCartaoCredito {
    private Cliente cliente;
    private CartaoCredito cartaoCredito;
    private List<Transacao> transacoes = new ArrayList<>();

    public Double getTotal(){
        return transacoes
              .stream()
              .mapToDouble(Transacao::getValor)
              .reduce(0.0, Double::sum);
    }
}
