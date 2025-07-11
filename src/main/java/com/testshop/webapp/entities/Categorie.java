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
@Table(name = "Categorie")
@Data
public class Categorie implements Serializable
{
    private static final long serialVersionUID = 4L;

    @Id
    @Column(name = "id_categoria")
    @JsonProperty("id_categoria")
    private Integer idCategoria;

    @Column(name = "nome_categoria")
    @JsonProperty("nome_categoria")
    private String nomeCategoria;

    @Column(name = "descrizione")
    private String descrizione;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "categoria")
    //@JsonManagedReference("categorieProdotti")
    @JsonIgnore //posso usare anche JsonIgnore ma bisogna rimuovere JsonManagedReference
    private Set<Prodotti> prodotti = new HashSet<Prodotti>();

}
