package com.testshop.webapp.repository;

import com.testshop.webapp.entities.Pagamenti;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PagamentiRepository extends JpaRepository<Pagamenti, Integer> {
    // Puoi aggiungere query custom se ti servono
}
