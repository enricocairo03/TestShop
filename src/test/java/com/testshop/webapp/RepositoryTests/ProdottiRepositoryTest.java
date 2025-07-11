package com.testshop.webapp.RepositoryTests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import com.testshop.webapp.entities.Categorie;
import com.testshop.webapp.entities.Fornitori;
import com.testshop.webapp.entities.Prodotti;
import com.testshop.webapp.repository.ProdottiRepository;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;

@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
public class ProdottiRepositoryTest {

    @Autowired
    private ProdottiRepository prodottiRepository;

    @Test
    @Order(1)
    public void testInsertProdotto() {
        Prodotti prodotto = new Prodotti();
        prodotto.setIdProdotto(500123);
        prodotto.setNome("Anello Test Oro");
        prodotto.setDescrizione("Anello di test con oro 18kt");
        prodotto.setPrezzo(259.99);
        prodotto.setQuantitaInmagazzino(20);
        prodotto.setMateriale("Oro");
        prodotto.setTipoGemma("Zaffiro");
        prodotto.setPeso(3.25);
        prodotto.setCarati(18.0);

        // Relazioni
        Fornitori fornitore = new Fornitori();
        fornitore.setIdFornitore(200123456); // integer valido
        prodotto.setFornitore(fornitore);

        Categorie categoria = new Categorie();
        categoria.setIdCategoria(400123456); // integer valido
        prodotto.setCategoria(categoria);

        prodottiRepository.save(prodotto);

        Prodotti result = prodottiRepository.findByIdProdotto(500123);
        assertThat(result).isNotNull();
        assertEquals("Anello Test Oro", result.getNome());
    }

    @Test
    @Order(2)
    public void testFindByIdProdotto() {
        Prodotti result = prodottiRepository.findByIdProdotto(500123);
        assertThat(result).isNotNull();
        assertEquals("Anello di test con oro 18kt", result.getDescrizione());
    }

    @Test
    @Order(3)
    public void testFindByDescrizioneLikePaged() {
        List<Prodotti> prodotti = prodottiRepository.findByDescrizioneLike("Anello%", PageRequest.of(0, 5));
        assertThat(prodotti.size()).isGreaterThanOrEqualTo(1);
    }

    @Test
    @Order(4)
    public void testSelectByDescrizioneLike() {
        List<Prodotti> prodotti = prodottiRepository.SelByDescrizioneLike("Anello%");
        assertThat(prodotti.size()).isGreaterThanOrEqualTo(1);
    }

    @Test
    @Order(5)
    public void testSelByForn() {
        Prodotti prodotto = prodottiRepository.selByForn(200123456); // coerente con l'inserimento
        assertThat(prodotto).isNotNull();
        assertEquals(500123, prodotto.getIdProdotto());
    }

    @Test
    @Order(6)
    public void testDeleteProdotto() {
        Prodotti prodotto = prodottiRepository.findByIdProdotto(500123);
        prodottiRepository.delete(prodotto);

        Prodotti deleted = prodottiRepository.findByIdProdotto(500123);
        assertThat(deleted).isNull();
    }
}
