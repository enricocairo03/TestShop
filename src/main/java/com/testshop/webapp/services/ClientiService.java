package com.testshop.webapp.services;

import com.testshop.webapp.dtos.ClientiDto;
import com.testshop.webapp.entities.Clienti;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ClientiService {
  public List<ClientiDto> SelByTel(String telefono);
  public List<ClientiDto> SelByTel(String telefono, Pageable pageable);

  public ClientiDto SelByIdCliente(Integer idCliente);

  public Clienti SelByIdCliente2(Integer idCliente);

  public void DelClienti(Clienti clienti);

  public void InsClienti(Clienti clienti);

  public void CleanCaches();
}

