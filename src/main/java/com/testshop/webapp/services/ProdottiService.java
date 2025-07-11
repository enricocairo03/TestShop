package com.testshop.webapp.services;

import com.testshop.webapp.dtos.ProdottiDto;
import com.testshop.webapp.entities.Prodotti;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProdottiService
{
    public List<ProdottiDto> SelByDescrizione(String descrizione);
    public List<ProdottiDto> SelByDescrizione(String descrizione, Pageable pageable);

    public ProdottiDto SelByIdProdotto(Integer idProdotto);

    public Prodotti SelByIdProdotto2(Integer idProdotto);

    public void DelProdotto(Prodotti prodotto);

    public void InsProdotto(Prodotti prodotto);

    public void CleanCaches();
}
