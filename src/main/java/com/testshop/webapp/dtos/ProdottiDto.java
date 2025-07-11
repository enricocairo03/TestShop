package com.testshop.webapp.dtos;

import lombok.Data;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Data
public class ProdottiDto implements Serializable
{
    private static final long serialVersionUID = 1L;
    private Integer idProdotto;
    private String nome;
    private String descrizione;
    private Double prezzo = 0.0;
    private Integer quantitaInmagazzino;
    private String materiale;
    private String tipoGemma;
    private Double peso;
    private Double carati;

   // private Set<DettagliOrdineDto> dettagliOrdine = new HashSet<>();
    private FornitoriDto fornitore;
    private CategorieDto categoria;

}
