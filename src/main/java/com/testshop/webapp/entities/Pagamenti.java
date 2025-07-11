package com.testshop.webapp.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "PAGAMENTI")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pagamenti
{
    @Id
    @Column(name = "ID_PAGAMENTO")
    @JsonProperty("id_pagamento")
    private Integer idPagamento;

    @Temporal(TemporalType.DATE)
    @JsonProperty("data_pagamento")
    @Column(name = "DATA_PAGAMENTO")
    private Date dataPagamento;

    @Column(name = "METODO_PAGAMENTO")
    @NotNull( message = "{NotNull.Pagamenti.metodo_pagamento.Validation}")
    @JsonProperty("metodo_pagamento")
    private String metodoPagamento;

    @Column(name = "IMPORTO")
    @NotNull( message = "{NotNull.Pagamenti.importo.Validation}")
    private Double importo;

    @OneToOne
    @JoinColumn(name = "ID_ORDINE")
    @JsonIgnore
    private Ordini ordini;

}
