package com.testshop.webapp.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "PRODOTTI")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Prodotti implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "ID_PRODOTTO")
    @Max(value = 999999)
    //@NotNull(message = "{NotNull.ProdottiComponent.id_prodotto.Validation}")
    @JsonProperty("id_prodotto")
    private Integer idProdotto;

    @Column(name = "nome")
    @Size(min = 6, max = 80, message = "{Size.ProdottiComponent.nome.Validation}")
    private String nome;

    @Column(name = "descrizione")
    @Size(min = 6, max = 80, message = "{Size.ProdottiComponent.descrizione.Validation}")
    private String descrizione;

    @Column(name = "prezzo")
    private Double prezzo;

    @Column(name = "quantita_in_magazzino")
    @JsonProperty("quantita_in_magazzino")
    private Integer quantitaInmagazzino;

    @Column(name = "materiale")
    private String materiale;

    @Column(name = "tipo_gemma")
    @JsonProperty("tipo_gemma")
    private String tipoGemma;

    @Column(name = "peso")
    private Double peso;

    @Column(name = "carati")
    private Double carati;

    @ManyToOne
    @JoinColumn(name = "ID_FORNITORE", referencedColumnName = "id_fornitore")
   // @JsonManagedReference("fornitoreProdotti") // questo se uso JsonIgnore non serve
    private Fornitori fornitore;

    @ManyToOne
    @JoinColumn(name = "ID_CATEGORIA", referencedColumnName = "id_categoria")
   // @JsonBackReference("categorieProdotti") // questo se uso JsonIgnore non serve
    private Categorie categoria;

    @OneToMany(mappedBy = "prodotto", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference("dettagliOrdineProdotti")
    private Set<Dettagli_Ordine> dettagliOrdine = new HashSet<>();
}
