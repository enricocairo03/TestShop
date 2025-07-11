package com.testshop.webapp.dtos;

import lombok.Data;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Data
public class ClientiDto implements Serializable {
    private static final long serialVersionUID = 3L;

    private Integer idCliente;
    private String nome;
    private String cognome;
    private String email;
    private String telefono;
    private String indirizzo;



}
