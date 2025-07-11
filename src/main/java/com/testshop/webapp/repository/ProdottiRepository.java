package com.testshop.webapp.repository;

import com.testshop.webapp.entities.Prodotti;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProdottiRepository  extends PagingAndSortingRepository<Prodotti, String>
{
    Prodotti findByIdProdotto(Integer idProdotto);

    List<Prodotti> findByDescrizioneLike(String descrizione, Pageable pageable);

    @Query(value = "SELECT * FROM Prodotti WHERE   descrizione ILIKE :descrizione", nativeQuery = true)
    List<Prodotti> SelByDescrizioneLike(@Param("descrizione") String descrizione);

    @Query(value="SELECT a FROM Prodotti a JOIN a.fornitore b WHERE b.idFornitore IN (:fornitore)")
    Prodotti selByForn(@Param("fornitore") Integer fornitore);


}
