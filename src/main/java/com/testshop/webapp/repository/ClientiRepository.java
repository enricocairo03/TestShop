package com.testshop.webapp.repository;

import com.testshop.webapp.entities.Clienti;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientiRepository extends PagingAndSortingRepository<Clienti, String>
{
    Clienti findByIdCliente( Integer idCliente );

    List<Clienti> findByTelefonoLike(String telefono, Pageable pageable);

    @Query(value = "SELECT * FROM CLIENTI WHERE TELEFONO LIKE :telefono", nativeQuery = true)
    List<Clienti> SelByTelefonoLike(@Param("telefono") String telefono);

}
