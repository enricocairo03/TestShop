package com.testshop.webapp.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class PagamentiDto implements Serializable
{
    private static final long serialVersionUID = 10L;

    private Integer idPagamento;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date dataPagamento;
    private String metodoPagamento;
    private Double importo;

    //private OrdiniDto ordini;
}
