package com.testshop.webapp.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "Dettagli_ordine")
@Data
public class Dettagli_Ordine {
    @Id
    @Column(name = "ID_DETTAGLIO")
    @JsonProperty("id_dettaglio")
    private Integer idDettaglio;

    @Column(name = "QUANTITA")
    private Integer quantita;

    @Column(name = "PREZZO_UNITARIO")
    @JsonProperty("prezzo_unitario")
    private Double prezzoUnitario;

    @ManyToOne
    @JoinColumn(name = "ID_PRODOTTO", referencedColumnName = "id_prodotto")
    @JsonIgnore
    private Prodotti prodotto;

    @ManyToOne
    @JoinColumn(name = "ID_ORDINE")
    @JsonIgnore
    private Ordini ordine;

}
