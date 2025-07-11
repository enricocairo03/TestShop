package com.testshop.webapp.services;

import com.testshop.webapp.dtos.FornitoriDto;
import com.testshop.webapp.entities.Fornitori;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FornitoriService
{
    public List<FornitoriDto> SelByEmail(String email);
    public List<FornitoriDto> SelByEmail(String email , Pageable pageable);

    public List<FornitoriDto> SelByTelefono(String telefono);
    public List<FornitoriDto> SelByTelefono(String telefono , Pageable pageable);

    public FornitoriDto SelByIdFornitore(Integer idFornitore);

    public Fornitori SelByIdFornitore2(Integer idFornitore);

    public void DelFornitore(Fornitori fornitori);

    public void InsFornitore(Fornitori fornitori);

    public void CleanCaches();
}
