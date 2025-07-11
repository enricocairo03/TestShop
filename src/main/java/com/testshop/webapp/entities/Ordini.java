package com.testshop.webapp.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "ORDINI")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ordini implements Serializable
{
    private static final long serialVersionUID = 2L;

    @Id
    @Column(name = "ID_ORDINE")
    @JsonProperty("id_ordine")
    private Integer idOrdine;

    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_ORDINE")
    @JsonProperty("data_ordine")
    private Date dataOrdine;

    @Column(name = "STATO")
    private String stato;

    @Column(name = "TOTALE")
    private double totale;

    @ManyToOne
    @JoinColumn(name = "ID_CLIENTE", referencedColumnName = "id_cliente")
    @JsonIgnore
    private Clienti clienti;

    @OneToOne(mappedBy = "ordini", cascade = CascadeType.ALL)
    @JsonIgnore
    private Pagamenti pagamenti;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "ordine")
    @JsonIgnore
    private Set<Dettagli_Ordine> dettagli_ordine = new HashSet<>();
}
