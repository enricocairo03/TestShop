package com.testshop.webapp.repository;

import com.testshop.webapp.entities.Ordini;
import com.testshop.webapp.entities.Prodotti;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrdiniRepository extends PagingAndSortingRepository<Ordini, String>
{
    Ordini findByIdOrdine(Integer idOrdine);

    List<Ordini> findByStatoLike(String stato, Pageable pageable);

    @Query(value = "SELECT * FROM ORDINI WHERE STATO ILIKE :stato", nativeQuery = true)
    List<Ordini> SelByStatoLike(@Param("stato") String stato);

}
