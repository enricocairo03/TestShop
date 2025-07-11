package com.testshop.webapp.dtos;

import lombok.Data;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Data
public class CategorieDto implements Serializable
{
    private static final long serialVersionUID = 4L;

    private Integer idCategoria;
    private String nomeCategoria;
    private String descrizione;

    //private Set<ProdottiDto> prodotti = new HashSet<>();

}
