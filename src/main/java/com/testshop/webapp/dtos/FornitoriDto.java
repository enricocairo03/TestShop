package com.testshop.webapp.dtos;

import lombok.Data;

import java.io.Serializable;
import java.util.Set;

@Data
public class FornitoriDto implements Serializable
{
    private static final long serialVersionUID = 7L;
    private Integer idFornitore;
    private String nomeAzienda;
    private String contattoNome;
    private String telefono;
    private String email;
    private String indirizzo;

   // private Set<ProdottiDto> prodotti;

}
