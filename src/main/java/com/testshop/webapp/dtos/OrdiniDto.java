package com.testshop.webapp.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
public class OrdiniDto implements Serializable
{
    private static final long serialVersionUID = 2L;
    private Integer idOrdine;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date dataOrdine;
    private String stato;
    private Double totale;


    // private Set<DettagliOrdineDto> dettagliOrdine = new HashSet<>();
    private ClientiDto clienti;
    private PagamentiDto pagamenti;

}
