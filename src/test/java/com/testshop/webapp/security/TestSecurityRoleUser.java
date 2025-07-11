package com.testshop.webapp.security;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.testshop.webapp.Application;
import com.testshop.webapp.utility.JsonData;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest()
@ContextConfiguration(classes = Application.class)
@TestMethodOrder(OrderAnnotation.class)
public class TestSecurityRoleUser
{
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    @BeforeEach
    public void setup()
    {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(wac)
                .defaultRequest(get("/")
                        .with(user("Enrico").roles("USER"))) //Attivato Ruolo User
                .apply(springSecurity()) //Attiva la sicurezza
                .build();

    }

    private final String ApiBaseUrl = "/api/prodotti";



    @Order(1)
    @Test
    public void testListArtByCodArt() throws Exception
    {
        mockMvc.perform(MockMvcRequestBuilders.get(ApiBaseUrl + "/cerca/codice/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(JsonData.GetProdData()))
                .andReturn();
    }

    @Order(2)
    @Test
    public void testErrRoleInsArticolo() throws Exception
    {
        mockMvc.perform(MockMvcRequestBuilders.post(ApiBaseUrl + "/inserisci")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonData.GetTestProdData())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden()) //Non sei autorizzato ad usare l'endpoint
                .andDo(print());
    }


}

