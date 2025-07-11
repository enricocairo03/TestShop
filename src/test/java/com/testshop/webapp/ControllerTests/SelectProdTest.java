package com.testshop.webapp.ControllerTests;

import com.testshop.webapp.Application;
import org.json.JSONException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ContextConfiguration(classes = Application.class)
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SelectProdTest
{
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    @BeforeEach
    public void setup() throws JSONException, IOException
    {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    String JsonData =
            "{\n" +
                    "  \"id_prodotto\": \"002000301\",\n" +
                    "  \"nome\": \"Anello Diamanti\",\n" +
                    "  \"descrizione\": \"Anello diamanti artiginale\",\n" +
                    "  \"prezzo\": 4.50,\n" +
                    "  \"quantita_in_magazzino\": 120,\n" +
                    "  \"materiale\": \"Diamante\",\n" +
                    "  \"tipo_gemma\": \"\",\n" +  // Non applicabile, lo lasciamo vuoto
                    "  \"peso\": 1.5,\n" +
                    "  \"carati\": 0.0,\n" +
                    "  \"categoria\": {\n" +
                    "    \"id_categoria\": \"020012\",\n" +
                    "    \"nome_categoria\": \"Gioielli preziosi\",\n" +
                    "    \"descrizione\": \"Annello confezione deluxe\"\n" +
                    "  },\n" +
                    "  \"fornitore\": {\n" +
                    "    \"id_fornitore\": \"040012\",\n" +
                    "    \"nome_azienda\": \"Miluna Spa\",\n" +
                    "    \"contatto_nome\": \"Luigi Rossi\",\n" +
                    "    \"telefono\": \"3209876543\",\n" +
                    "    \"email\": \"info@uliveto.it\",\n" +
                    "    \"indirizzo\": \"Via delle Fonti 45, Firenze\"\n" +
                    "  }\n" +
                    "}";
    @Test
    @Order(1)
    public void listProdByForn() throws Exception
    {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/prodotti/cerca/fornitore/Miluna Spa")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))

                // prodotto
                .andExpect(jsonPath("$.id_prodotto").exists())
                .andExpect(jsonPath("$.id_prodotto").value(002000301))
                .andExpect(jsonPath("$.nome").exists())
                .andExpect(jsonPath("$.nome").value("Anello Diamanti"))
                .andExpect(jsonPath("$.descrizione").exists())
                .andExpect(jsonPath("$.descrizione").value("Anello diamanti artigianale"))
                .andExpect(jsonPath("$.prezzo").exists())
                .andExpect(jsonPath("$.prezzo").value(4.50))
                .andExpect(jsonPath("$.quantita_in_magazzino").exists())
                .andExpect(jsonPath("$.quantita_in_magazzino").value(120))
                .andExpect(jsonPath("$.materiale").exists())
                .andExpect(jsonPath("$.materiale").value("Diamante"))
                .andExpect(jsonPath("$.tipo_gemma").exists())
                .andExpect(jsonPath("$.tipo_gemma").value(""))
                .andExpect(jsonPath("$.peso").exists())
                .andExpect(jsonPath("$.peso").value(1.5))
                .andExpect(jsonPath("$.carati").exists())
                .andExpect(jsonPath("$.carati").value(0.0))

                // categoria
                .andExpect(jsonPath("$.categoria.id_categoria").exists())
                .andExpect(jsonPath("$.categoria.id_categoria").value(020012))
                .andExpect(jsonPath("$.categoria.nome_categoria").exists())
                .andExpect(jsonPath("$.categoria.nome_categoria").value("Gioielli preziosi"))
                .andExpect(jsonPath("$.categoria.descrizione").exists())
                .andExpect(jsonPath("$.categoria.descrizione").value("Annello confezione deluxe"))

                // fornitore
                .andExpect(jsonPath("$.fornitore.id_fornitore").exists())
                .andExpect(jsonPath("$.fornitore.id_fornitore").value(040012))
                .andExpect(jsonPath("$.fornitore.nome_azienda").exists())
                .andExpect(jsonPath("$.fornitore.nome_azienda").value("Miluna Spa"))
                .andExpect(jsonPath("$.fornitore.contatto_nome").exists())
                .andExpect(jsonPath("$.fornitore.contatto_nome").value("Luigi Rossi"))
                .andExpect(jsonPath("$.fornitore.telefono").exists())
                .andExpect(jsonPath("$.fornitore.telefono").value("3209876543"))
                .andExpect(jsonPath("$.fornitore.email").exists())
                .andExpect(jsonPath("$.fornitore.email").value("info@miluna.it"))
                .andExpect(jsonPath("$.fornitore.indirizzo").exists())
                .andExpect(jsonPath("$.fornitore.indirizzo").value("Via delle Fonti 45, Firenze"))

                .andDo(print());
    }
    private String Fornitore = "Miluna Spa";

    @Test
    @Order(2)
    public void ErrlistProdByForn() throws Exception
    {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/prodotti/cerca/fornitore/" + Fornitore)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonData)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.message").value("Il fornitore " + Fornitore + " non e' stato trovato!"))
                .andDo(print());
    }

    @Test
    @Order(3)
    public void listProdByNome() throws Exception
    {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/prodotti/cerca/nome/Anelllo Diamanti")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(JsonData))
                .andReturn();
    }

    private String Nome = "Anello Diamanti";

    @Test
    @Order(4)
    public void errlistProdByNome() throws Exception
    {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/prodotti/cerca/nome/" + Nome)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonData)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.message").value("Il prodotto con nome " + Nome + " non e stato trovato!"))
                .andDo(print());
    }

    private String JsonData2 = "[" + JsonData + "]";

    @Test
    @Order(5)
    public void listProdByDesc() throws Exception
    {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/prodotti/cerca/descrizione/Anello diamanti artigianale")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(JsonData2))
                .andReturn();
    }

    @Test
    @Order(6)
    public void errlistProdbyDesc() throws Exception
    {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/prodotti/cerca/descrizione/Anello diamanti artigianale")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonData)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.message").value("Non e' stato trovato alcun prodotto avente descrizione Anello diamanti artigianale"))
                .andDo(print());
    }
}
