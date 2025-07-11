package com.testshop.webapp.repository;

import com.testshop.webapp.entities.Fornitori;
import com.testshop.webapp.entities.Ordini;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FornitoriRepository extends PagingAndSortingRepository<Fornitori, String>
{
    Fornitori findByIdFornitore(Integer idFornitore);

    List<Fornitori> findByEmailLike(String email , Pageable pageable);

    List<Fornitori> findByTelefonoLike(String telefono , Pageable pageable);

    @Query(value = "SELECT * FROM FORNITORI WHERE EMAIL ILIKE :email", nativeQuery = true)
    List<Fornitori> SelByEmailLike(@Param("email") String email);

    @Query(value = "SELECT * FROM FORNITORI WHERE TELEFONO ILIKE :telefono", nativeQuery = true)
    List<Fornitori> SelByTelefonoLike(@Param("telefono") String telefono);
}
