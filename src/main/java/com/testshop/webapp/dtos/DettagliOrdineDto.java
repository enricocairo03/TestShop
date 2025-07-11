package com.testshop.webapp.dtos;

import lombok.Data;

import java.io.Serializable;

@Data
public class DettagliOrdineDto implements Serializable
{
    private static final long serialVersionUID = 5L;

    private Integer idDettaglio;
    private Integer quantita;
    private Double prezzoUnitario;

    private ProdottiDto prodotti;
    private OrdiniDto ordini;

}
