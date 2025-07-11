package com.testshop.webapp.ControllerTests;

import com.testshop.webapp.entities.Prodotti;
import com.testshop.webapp.repository.ProdottiRepository;
import com.testshop.webapp.Application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@ContextConfiguration(classes = Application.class)
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class InsertProdTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    ProdottiRepository prodottiRepository;

    @BeforeEach
    public void setup() throws IOException {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    private final String JsonData =
            "{\n" +
                    "  \"id_prodotto\": 500123,\n" +
                    "  \"nome\": \"Anello Test Oro\",\n" +
                    "  \"descrizione\": \"Anello di test con oro 18kt\",\n" +
                    "  \"prezzo\": 259.99,\n" +
                    "  \"quantita_in_magazzino\": 20,\n" +
                    "  \"materiale\": \"Oro\",\n" +
                    "  \"tipo_gemma\": \"Zaffiro\",\n" +
                    "  \"peso\": 3.25,\n" +
                    "  \"carati\": 18.0,\n" +
                    "  \"fornitore\": {\n" +
                    "    \"id_fornitore\": 200123456,\n" +
                    "    \"nome_azienda\": \"Forniture Gioielli Spa\",\n" +
                    "    \"contatto_nome\": \"Mario Bianchi\",\n" +
                    "    \"telefono\": \"3456789012\",\n" +
                    "    \"email\": \"fornitore@gioielli.com\",\n" +
                    "    \"indirizzo\": \"Via Roma 10, Milano\"\n" +
                    "  },\n" +
                    "  \"categoria\": {\n" +
                    "    \"id_categoria\": 40012,\n" +
                    "    \"nome_categoria\": \"Anelli\",\n" +
                    "    \"descrizione\": \"Anelli in oro e pietre preziose\"\n" +
                    "  }\n" +
                    "}";

    @Test
    @Order(1)
    public void testInsProdotto() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/prodotti/inserisci")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonData)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Inserimento Prodotto Eseguito con successo!"))
                .andDo(print());

        assertThat(prodottiRepository.findByIdProdotto(500123))
                .extracting(Prodotti::getDescrizione)
                .isEqualTo("Anello di test con oro 18kt");
    }

    @Test
    @Order(2)
    public void testErrInsProdotto1() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/prodotti/inserisci")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonData)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotAcceptable())
                .andExpect(jsonPath("$.code").value(406))
                .andExpect(jsonPath("$.message").value("Articolo 500123 presente in anagrafica! Impossibile utilizzare il metodo POST"))
                .andDo(print());
    }

    private final String ErrJsonData =
            "{\n" +
                    "  \"id_prodotto\": 500123,\n" +
                    "  \"nome\": \"Anello Diamante\",\n" +
                    "  \"descrizione\": \"\",\n" +
                    "  \"prezzo\": 1499.99,\n" +
                    "  \"quantita_in_magazzino\": 10,\n" +
                    "  \"materiale\": \"Oro bianco\",\n" +
                    "  \"tipo_gemma\": \"Diamante\",\n" +
                    "  \"peso\": 0.5,\n" +
                    "  \"carati\": 1.2,\n" +
                    "  \"fornitore\": { \"id_fornitore\": 20012 },\n" +
                    "  \"categoria\": { \"id_categoria\": 40012 }\n" +
                    "}";

    @Test
    @Order(3)
    public void testErrInsProdotto2() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/prodotti/inserisci")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ErrJsonData)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value("Il campo Descrizione deve avere un numero di caratteri compreso tra 6 e 80"))
                .andDo(print());
    }

    private final String JsonDataMod =
            "{\n" +
                    "  \"id_prodotto\": 500123,\n" +
                    "  \"nome\": \"Anello Diamante\",\n" +
                    "  \"descrizione\": \"Anello Unit Test Modifica\",\n" +
                    "  \"prezzo\": 1499.99,\n" +
                    "  \"quantita_in_magazzino\": 10,\n" +
                    "  \"materiale\": \"Oro bianco\",\n" +
                    "  \"tipo_gemma\": \"Diamante\",\n" +
                    "  \"peso\": 0.5,\n" +
                    "  \"carati\": 1.2,\n" +
                    "  \"fornitore\": { \"id_fornitore\": 20012 },\n" +
                    "  \"categoria\": { \"id_categoria\": 40012 }\n" +
                    "}";

    @Test
    @Order(4)
    public void testUpProdotto() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/api/prodotti/modifica")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonDataMod)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Modifica ProdottiComponent Eseguita con successo!"))
                .andDo(print());

        assertThat(prodottiRepository.findByIdProdotto(500123))
                .extracting(Prodotti::getDescrizione)
                .isEqualTo("Anello Unit Test Modifica");
    }

    private final String JsonDataMod2 =
            "{\n" +
                    "  \"id_prodotto\": 9999,\n" + // Codice prodotto inesistente
                    "  \"nome\": \"Anello Corallo\",\n" +
                    "  \"descrizione\": \"Anello Unit Test Modifica\",\n" +
                    "  \"prezzo\": 99.99,\n" +
                    "  \"quantita_in_magazzino\": 10,\n" +
                    "  \"materiale\": \"Oro\",\n" +
                    "  \"tipo_gemma\": \"Zaffiro\",\n" +
                    "  \"peso\": 1.75,\n" +
                    "  \"carati\": 0.5,\n" +
                    "  \"fornitore\": { \"id_fornitore\": 20012 },\n" +
                    "  \"categoria\": { \"id_categoria\": 40012 }\n" +
                    "}";

    @Test
    @Order(5)
    public void testErrUpProdotto() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/api/prodotti/modifica")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonDataMod2)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Articolo 9999 non presente in anagrafica! Impossibile utilizzare il metodo PUT"))
                .andDo(print());

        assertThat(prodottiRepository.findByIdProdotto(500123))
                .extracting(Prodotti::getDescrizione)
                .isEqualTo("Anello Unit Test Modifica");
    }

    @Test
    @Order(6)
    public void testDelProdotto() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/prodotti/elimina/500123")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200 OK"))
                .andExpect(jsonPath("$.message").value("Eliminazione Articolo 500123 Eseguita Con Successo"))
                .andDo(print());
    }

    @Test
    @Order(7)
    public void testErrDelProdotto() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/prodotti/elimina/500123")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("ProdottiComponent 500123 non presente in anagrafica! "))
                .andDo(print());
    }
}
