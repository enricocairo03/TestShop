package com.testshop.webapp.services;

import com.testshop.webapp.entities.Pagamenti;
import com.testshop.webapp.repository.PagamentiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PagamentiServiceImpl implements PagamentiService {

    @Autowired
    private PagamentiRepository pagamentiRepository;

    @Override
    public Optional<Pagamenti> findById(Integer idPagamento) {
        return pagamentiRepository.findById(idPagamento);
    }

    // aggiungi altri metodi se ti servono
}
