package com.testshop.webapp.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "FORNITORI")
@Data
public class Fornitori implements Serializable
{
    private static final long serialVersionUID = 3L;

    @Id
    @Column(name = "ID_FORNITORE")
    @JsonProperty("id_fornitore")
    private Integer idFornitore;

    @Column(name = "NOME_AZIENDA")
    @JsonProperty("nome_azienda")
    private String nomeAzienda;

    @Column(name = "CONTATTO_NOME")
    @JsonProperty("contatto_nome")
    private String contattoNome;

    @Column(name = "TELEFONO")
    private String telefono;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "INDIRIZZO")
    private String indirizzo;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "fornitore")
    //@JsonManagedReference("fornitoreProdotti")
     @JsonIgnore //posso usare anche JsonIgnore ma bisogna rimuovere JsonManagedReference
    private Set<Prodotti> prodotti = new HashSet<>();
}
