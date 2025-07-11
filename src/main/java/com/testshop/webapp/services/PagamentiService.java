package com.testshop.webapp.services;

import com.testshop.webapp.entities.Pagamenti;
import java.util.Optional;

public interface PagamentiService {
    Optional<Pagamenti> findById(Integer idPagamento);
    // altri metodi se ti servono...
}
