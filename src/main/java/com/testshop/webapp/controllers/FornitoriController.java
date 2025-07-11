package com.testshop.webapp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.testshop.webapp.dtos.FornitoriDto;
import com.testshop.webapp.dtos.InfoMsg;
import com.testshop.webapp.entities.Fornitori;
import com.testshop.webapp.entities.Ordini;
import com.testshop.webapp.exceptions.BindingException;
import com.testshop.webapp.exceptions.DuplicateException;
import com.testshop.webapp.exceptions.NotFoundException;
import com.testshop.webapp.services.FornitoriService;
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
@RequestMapping("api/fornitori")
@Log
@Tag(name = "FornitoriController" , description = "Controller Opererazioni di Gestione Dati Ordini")
public class FornitoriController
{

    @Autowired
    private FornitoriService fornitoriService;

    @Autowired
    private ResourceBundleMessageSource errMessage;

    @GetMapping(value = "/cerca/codice/{IdFornitore}", produces = "application/json")
    @SneakyThrows
    public ResponseEntity<FornitoriDto> listFornByIdFornitore(@PathVariable("IdFornitore") Integer IdFornitore)
    {
        log.info("****** Otteniamo il fornitore con id " + IdFornitore + "******");
        FornitoriDto fornitore = fornitoriService.SelByIdFornitore(IdFornitore);

        if(fornitore == null)
        {
            String ErrMsg = String.format("Il fornitore con codice %s non trovato", IdFornitore);
            log.warning(ErrMsg);

            throw new NotFoundException(ErrMsg);

        }
        return new ResponseEntity<>(fornitore, HttpStatus.OK);
    }

    @GetMapping(value = "/cerca/email/{email}", produces = "application/json")
    @SneakyThrows
    public ResponseEntity<List<FornitoriDto>> listFornByEmail(@PathVariable("email") String email)
    {
        log.info("****** Otteniamo il fornitore con email " + email + "******");

        List<FornitoriDto> fornitori = fornitoriService.SelByEmail(email);

        if(fornitori.isEmpty())
        {
            String ErrMsg = String.format("Non è stato trovato nessun fornitore con email: %s", email);
            log.warning(ErrMsg);

            throw new NotFoundException(ErrMsg);
        }
        return new ResponseEntity<>(fornitori, HttpStatus.OK);
    }

    @GetMapping(value = "/cerca/telefono/{telefono}", produces = "application/json")
    @SneakyThrows
    public ResponseEntity<List<FornitoriDto>> listFornByTelefono(@PathVariable("telefono") String telefono)
    {
        log.info("****** Otteniamo il fornitore con numero di telefono: " + telefono + "******");

        List<FornitoriDto> fornitori = fornitoriService.SelByTelefono(telefono);

        if(fornitori.isEmpty())
        {
            String ErrMsg = String.format("Non è stato trovato nessun fornitore con il numero di telefono: %s", telefono);
            log.warning(ErrMsg);

            throw new NotFoundException(ErrMsg);
        }
        return new ResponseEntity<>(fornitori, HttpStatus.OK);
    }

    //----------------INSERIMENTO FORNITORE-----------------------------------------
    @PostMapping(value = "/inserisci", consumes = {"application/json", "application/json;charset=UTF-8"}, produces = "application/json")
    @SneakyThrows
    public ResponseEntity<InfoMsg> createFornitore(@Valid @RequestBody Fornitori fornitore, BindingResult bindingResult)
    {
        log.info("Salviamo il fornitore con id" + fornitore.getIdFornitore());

        if(bindingResult.hasErrors())
        {
            String MsgErr = errMessage.getMessage(bindingResult.getFieldError(), LocaleContextHolder.getLocale());
            log.warning(MsgErr);

            throw new BindingException(MsgErr);
        }

        FornitoriDto checkForn = fornitoriService.SelByIdFornitore(fornitore.getIdFornitore());

        if(checkForn != null)
        {
            String MsgErr = String.format("Fornitore %s presente in anagrafica! " + "Impossibile utilizzare il metodo POST", fornitore.getIdFornitore());
            log.warning(MsgErr);

            throw new DuplicateException(MsgErr);
        }

        fornitoriService.InsFornitore(fornitore);

        return new ResponseEntity<InfoMsg>(new InfoMsg(LocalDate.now(),
                "Inserimento Fornitore Eseguita con successo!"), HttpStatus.CREATED);
    }

    //-------------MODIFICA FORNITORE------------------------------
    @RequestMapping(value = "/modifica", method = RequestMethod.PUT)
    @SneakyThrows
    public ResponseEntity<InfoMsg> updateFornitore(@Valid @RequestBody Fornitori fornitore, BindingResult bindingResult)
    {
        log.info("Modifichiamo il fornitore con id " + fornitore.getIdFornitore());

        if(bindingResult.hasErrors())
        {
            String MsgErr = errMessage.getMessage(bindingResult.getFieldError(), LocaleContextHolder.getLocale());
            log.warning(MsgErr);

            throw new BindingException(MsgErr);
        }

        FornitoriDto checkForn = fornitoriService.SelByIdFornitore(fornitore.getIdFornitore());

        if(checkForn == null)
        {
            String MsgErr = String.format("Fornitore %s non trovato in anagrafica!" + "Impossibile utilizzare il metodo PUT", fornitore.getIdFornitore());
            log.warning(MsgErr);

            throw new BindingException(MsgErr);
        }

        fornitoriService.InsFornitore(fornitore);

        return new ResponseEntity<InfoMsg>(new InfoMsg(LocalDate.now(), "Modifica Fornitore Eseguita con successo!"), HttpStatus.OK);
    }
//------------------ELIMINAZIONE FORNITORE----------------------------
    @DeleteMapping(value = "/elimina/{IdFornitore}", produces = "application/json")
    @SneakyThrows
    public ResponseEntity<?> deleteFornitore(@PathVariable("IdFornitore") Integer IdFornitore) {
        log.info("Eliminiamo il fornitore con id " + IdFornitore);

        Fornitori fornitori = fornitoriService.SelByIdFornitore2(IdFornitore);

        if (fornitori == null) {
            String MsgErr = String.format("Ordine %s non presente in anagrafica!", IdFornitore);
            log.warning(MsgErr);
            throw new NotFoundException(MsgErr);
        }

        fornitoriService.DelFornitore(fornitori);

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode responseNode = mapper.createObjectNode();

        responseNode.put("code", HttpStatus.OK.toString());
        responseNode.put("message", "Fornitore eliminato " + IdFornitore + " eseguito con successo!");

        return new ResponseEntity<>(responseNode, new HttpHeaders(), HttpStatus.OK);
    }


}
