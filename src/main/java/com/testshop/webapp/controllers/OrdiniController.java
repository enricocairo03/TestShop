package com.testshop.webapp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.testshop.webapp.dtos.InfoMsg;
import com.testshop.webapp.dtos.OrdiniDto;
import com.testshop.webapp.entities.Clienti;
import com.testshop.webapp.entities.Ordini;
import com.testshop.webapp.exceptions.BindingException;
import com.testshop.webapp.exceptions.DuplicateException;
import com.testshop.webapp.exceptions.NotFoundException;
import com.testshop.webapp.services.ClientiService;
import com.testshop.webapp.services.OrdiniService;
import io.swagger.v3.oas.annotations.Operation;
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
@RequestMapping("api/ordini")
@Log
@Tag(name = "OrdiniController", description = "Controller Operazioni di Gestione Dati Ordini")
public class OrdiniController {

    @Autowired
    private OrdiniService ordiniService;

    @Autowired
    private ResourceBundleMessageSource errMessage;

    @GetMapping(value = "/cerca/codice/{IdOrdine}", produces = "application/json")
    @SneakyThrows
    public ResponseEntity<OrdiniDto> listOrdByIdOrdine(@PathVariable("IdOrdine") Integer IdOrdine) {
        log.info("****** Otteniamo l' ordine con id " + IdOrdine + "******" );
        OrdiniDto ordine = ordiniService.SelByIdOrdine(IdOrdine);

        if (ordine == null) {
            String ErrMsg = String.format("L' ordine con codice %s non trovato", IdOrdine);
            log.warning(ErrMsg);
            throw new NotFoundException(ErrMsg);
        }
        return new ResponseEntity<>(ordine, HttpStatus.OK);
    }

    @GetMapping(value = "/cerca/stato/{stato}", produces = "application/json")
    @SneakyThrows
    public ResponseEntity<List<OrdiniDto>> listOrdByStato(@PathVariable("stato") String stato) {
        log.info("****** Otteniamo gli ordini con stato " + stato + "******" );

        List<OrdiniDto> ordini = ordiniService.SelByStato(stato);

        if (ordini.isEmpty()) {
            String ErrMsg = String.format("Non è stato trovato nessun ordine con stato %s", stato);
            log.warning(ErrMsg);
            throw new NotFoundException(ErrMsg);
        }
        return new ResponseEntity<>(ordini, HttpStatus.OK);
    }

    //---------------------- INSERIMENTO ORDINE --------------------------------------
    @PostMapping(value = "/inserisci", consumes = {"application/json", "application/json;charset=UTF-8"}, produces = "application/json")
    @SneakyThrows
    public ResponseEntity<InfoMsg> createOrdine(@Valid @RequestBody Ordini ordine, BindingResult bindingResult) {
        log.info("Salviamo l'ordine con id " + ordine.getIdOrdine());

        if(bindingResult.hasErrors())
        {
            String MsgErr = errMessage.getMessage(bindingResult.getFieldError(), LocaleContextHolder.getLocale());
            log.warning(MsgErr);

            throw new BindingException(MsgErr);
        }

        OrdiniDto checkProd = ordiniService.SelByIdOrdine(ordine.getIdOrdine());

        if(checkProd != null)
        {
            String MsgErr = String.format("Ordine %s presente in anagrafica!" + "Impossibile utilizzare il metodo POST", ordine.getIdOrdine());
            log.warning(MsgErr);

            throw new DuplicateException(MsgErr);
        }

        ordiniService.InsOrdini(ordine);

        return new ResponseEntity<InfoMsg>(new InfoMsg(LocalDate.now(),
                "Inserimento Ordine Eseguita con successo!"), HttpStatus.CREATED);


    }

    //----------------------- MODIFICA ORDINE ------------------------------------
    @RequestMapping(value = "/modifica", method = RequestMethod.PUT)
    @SneakyThrows
    public ResponseEntity<InfoMsg> updateOrdine(@Valid @RequestBody Ordini ordine, BindingResult bindingResult) {
        log.info("Modifichiamo l'ordine con id " + ordine.getIdOrdine());

        if(bindingResult.hasErrors())
        {
            String MsgErr = errMessage.getMessage(bindingResult.getFieldError(), LocaleContextHolder.getLocale());
            log.warning(MsgErr);

            throw new BindingException(MsgErr);
        }

        OrdiniDto checkProd = ordiniService.SelByIdOrdine(ordine.getIdOrdine());

        if(checkProd == null)
        {
            String MsgErr = String.format("Ordine %s non trovato in anagrafica!" + "Impossibile utilizzare il metodo PUT", ordine.getIdOrdine());
            log.warning(MsgErr);

            throw new BindingException(MsgErr);
        }

        ordiniService.InsOrdini(ordine);

        return new ResponseEntity<InfoMsg>(new InfoMsg(LocalDate.now(), "Modifica Ordine Eseguita con successo!"), HttpStatus.OK);
    }

    //------------------------- ELIMINAZIONE ORDINE -------------------------------------
    @DeleteMapping(value = "/elimina/{IdOrdine}", produces = "application/json")
    @SneakyThrows
    public ResponseEntity<?> deleteOrdine(@PathVariable("IdOrdine") Integer IdOrdine) {
        log.info("Eliminiamo l'ordine con id " + IdOrdine);

        Ordini ordine = ordiniService.SelByIdOrdine2(IdOrdine);

        if (ordine == null) {
            String MsgErr = String.format("Ordine %s non presente in anagrafica!", IdOrdine);
            log.warning(MsgErr);
            throw new NotFoundException(MsgErr);
        }

        ordiniService.DelOrdini(ordine);

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode responseNode = mapper.createObjectNode();

        responseNode.put("code", HttpStatus.OK.toString());
        responseNode.put("message", "Ordine eliminato " + IdOrdine + " eseguito con successo!");

        return new ResponseEntity<>(responseNode, new HttpHeaders(), HttpStatus.OK);
    }
}
