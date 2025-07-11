package com.testshop.webapp.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "CLIENTI")
@Data
public class Clienti implements Serializable
{
    private static final long serialVersionUID = 6L;

    @Id
    @Column(name = "ID_CLIENTE")
    @JsonProperty("id_cliente")
    private Integer idCliente;

    @Column(name = "NOME")
    @NotNull(message = "{NotNull.Clienti.nome.Validation}")
    private String nome;

    @Column(name = "COGNOME")
    @NotNull(message = "{NotNull.Clienti.cognome.Validation}")
    private String cognome;

    @Column(name = "TELEFONO")
    @NotNull( message = "{NotNull.Clienti.telefono.Validation}")
    private String telefono;

    @Column(name = "EMAIL")
    @NotNull( message = "{NotNull.Clienti.email.Validation}")
    private String email;

    @Column(name = "INDIRIZZO")
    @NotNull( message = "{NotNull.Clienti.indirizzo.Validation}")
    private String indirizzo;

    @OneToMany(mappedBy = "clienti", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Ordini> ordini = new HashSet<>();
}
