package com.testshop.webapp.services;

import com.testshop.webapp.dtos.OrdiniDto;
import com.testshop.webapp.entities.Ordini;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrdiniService
{
    public List<OrdiniDto> SelByStato(String stato);
    public List<OrdiniDto> SelByStato(String stato, Pageable pageable);

    public OrdiniDto SelByIdOrdine(Integer idOrdine);

    public Ordini SelByIdOrdine2(Integer idOrdine);

    public void DelOrdini(Ordini ordini);

    public void InsOrdini(Ordini ordini);

    public void CleanCaches();
}
