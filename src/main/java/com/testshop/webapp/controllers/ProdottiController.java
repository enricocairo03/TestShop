package com.testshop.webapp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.testshop.webapp.dtos.InfoMsg;
import com.testshop.webapp.dtos.ProdottiDto;
import com.testshop.webapp.entities.Prodotti;
import com.testshop.webapp.exceptions.BindingException;
import com.testshop.webapp.exceptions.DuplicateException;
import com.testshop.webapp.exceptions.NotFoundException;
import com.testshop.webapp.services.ProdottiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.SneakyThrows;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("api/prodotti")
@Log
@Tag(name= "ProdottiController", description = "Controller Operazioni di Gestione Dati Articoli")
public class ProdottiController
{
    @Autowired
    private ProdottiService prodottiService;

    @Autowired
    private ResourceBundleMessageSource errMessage;

    @Operation(summary = "Ricerca il prodotto per ID", description = "Restituisce i dati del prodotto in formato JSON", tags = { "ProdottiDto" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Il prodotto cercato è stato trovato!", content = @Content(schema = @Schema(implementation = ProdottiDto.class))),
            @ApiResponse(responseCode = "401", description = "Utente non AUTENTICATO", content = @Content),
            @ApiResponse(responseCode = "403", description = "Utente Non AUTORIZZATO ad accedere alle informazioni", content = @Content),
            @ApiResponse(responseCode = "404", description = "Il prodotto cercato NON è stato trovato!", content = @Content) })
    @GetMapping(value = "/cerca/codice/{IdProdotto}", produces = "application/json")
    @SneakyThrows
    public ResponseEntity<ProdottiDto> listProdByIdProdotto(@PathVariable("IdProdotto") Integer IdProdotto)  {
        log.info("****** Ottteniamo il prodotto con codice" + IdProdotto + "******");
        ProdottiDto prodotto = prodottiService.SelByIdProdotto(IdProdotto);

        if(prodotto == null){
            String ErrMsg = String.format("Il prodotto con codice %s non trovato", IdProdotto);
            log.warning(ErrMsg);

            throw new NotFoundException(ErrMsg);
        }
        return new ResponseEntity<ProdottiDto>(prodotto, HttpStatus.OK);
    }

    @Operation(summary = "Ricerca uno o più prodotti per descrizione o parte", description = "Restituisce i dati del prodotto in formato JSON", tags = { "ProdottiDto" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Il prodotto/i cercato/i sono stati trovati!", content = @Content(schema = @Schema(implementation = ProdottiDto.class))),
            @ApiResponse(responseCode = "401", description = "Utente non AUTENTICATO", content = @Content),
            @ApiResponse(responseCode = "403", description = "Utente Non AUTORIZZATO ad accedere alle informazioni", content = @Content),
            @ApiResponse(responseCode = "404", description = "Il prodotto/i cercato/i NON sono stati trovati!", content = @Content) })
    @GetMapping(value = "/cerca/descrizione/{Filter}", produces = "application/json")
    @SneakyThrows
    public ResponseEntity<List<ProdottiDto>> listProdByDescrizione(@PathVariable("Filter") String Filter) {
        log.info("****** Otteniamo il prodotto con descrizione " + Filter + "******");

        List<ProdottiDto> prodotti = prodottiService.SelByDescrizione(Filter);

        if(prodotti.isEmpty())
        {
            String ErrMsg = String.format("Non e' stato trovatato alcun prodotto avente descrizione %s", Filter);
            log.warning(ErrMsg);

            throw new NotFoundException(ErrMsg);
        }
        return new ResponseEntity<List<ProdottiDto>>(prodotti, HttpStatus.OK);


    }
//------------------- INSERIMENTO PRODOTTO ----------------------------------------------
@PostMapping(value = "/inserisci", consumes = {"application/json", "application/json;charset=UTF-8"}, produces = "application/json")
@SneakyThrows
    public ResponseEntity<InfoMsg> createProdotto(@Valid @RequestBody Prodotti prodotto, BindingResult bindingResult)
    {
        log.info("Salviamo il prodotto con id " + prodotto.getIdProdotto());

        //controllo validità dati prodotto
        if(bindingResult.hasErrors())
        {
            String MsgErr = errMessage.getMessage(bindingResult.getFieldError(), LocaleContextHolder.getLocale());

            log.warning(MsgErr);

            throw new BindingException(MsgErr);
        }

        //Disabilitare se si vuole gestrie anche la mdifica
        ProdottiDto ckeckProd = prodottiService.SelByIdProdotto(prodotto.getIdProdotto());

        if(ckeckProd != null)
        {
            String MsgErr = String.format("Prodotto %s presente in anagrafica!" + "Impossibile utilizzare il metodo POST", prodotto.getIdProdotto());

            log.warning(MsgErr);

            throw new DuplicateException(MsgErr);
        }

        prodottiService.InsProdotto(prodotto);

        return new ResponseEntity<InfoMsg>(new InfoMsg(LocalDate.now(),
                "Inserimento Prodotto Eseguita con successo!"), HttpStatus.CREATED);
    }

    //-------------------------- MODIFICA PRODOTTO----------------------------------

    @RequestMapping(value = "/modifica", method = RequestMethod.PUT)
    @SneakyThrows
    public ResponseEntity<InfoMsg> updateProdotto(@Valid @RequestBody Prodotti prodotto, BindingResult bindingResult)
    {
        log.info("Modifichiamo il prodotto con id " + prodotto.getIdProdotto());

        if(bindingResult.hasErrors())
        {
            String MsgErr = errMessage.getMessage(bindingResult.getFieldError(), LocaleContextHolder.getLocale());
            log.warning(MsgErr);

            throw new BindingException(MsgErr);
        }

        ProdottiDto checkProd = prodottiService.SelByIdProdotto(prodotto.getIdProdotto());

        if(checkProd == null)
        {
            String MsgErr = String.format("Prodotto %s non presente in anagrafica!" + "Impossibile utilizzare il metodo PUT", prodotto.getIdProdotto());
            log.warning(MsgErr);

            throw new BindingException(MsgErr);
        }

        prodottiService.InsProdotto(prodotto);

        return new ResponseEntity<InfoMsg>(new InfoMsg(LocalDate.now(),
                "Modifica Prodotto Eseguita con successo!"), HttpStatus.OK);
    }

    //------------------ ELIMINAZIONE PRODOTTO--------------------------------------
    @DeleteMapping(value = "/elimina/{IdProdotto}", produces = "application/json")
    @SneakyThrows
    public ResponseEntity<?> deleteProdotto(@PathVariable("IdProdotto") Integer IdProdotto)
    {
        log.info("Eliminamo il prodotto con id " + IdProdotto);

        Prodotti prodotto = prodottiService.SelByIdProdotto2(IdProdotto);

        if(prodotto == null)
        {
            String MsgErr = String.format("Prodotto %s non trovato in anagrafica!", IdProdotto);
            log.warning(MsgErr);

            throw new NotFoundException(MsgErr);
        }

        prodottiService.DelProdotto(prodotto);

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode responseNode = mapper.createObjectNode();

        responseNode.put("code", HttpStatus.OK.toString());
        responseNode.put("message", "Eliminazione Prodotto" + IdProdotto + "Eseguita con Successo");

        return new ResponseEntity<>(responseNode, new HttpHeaders(),  HttpStatus.OK);

    }

}
