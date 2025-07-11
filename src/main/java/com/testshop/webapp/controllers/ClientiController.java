package com.testshop.webapp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.testshop.webapp.dtos.ClientiDto;
import com.testshop.webapp.dtos.InfoMsg;
import com.testshop.webapp.dtos.OrdiniDto;
import com.testshop.webapp.entities.Clienti;
import com.testshop.webapp.exceptions.BindingException;
import com.testshop.webapp.exceptions.DuplicateException;
import com.testshop.webapp.exceptions.NotFoundException;
import com.testshop.webapp.services.ClientiService;
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
@RequestMapping("api/clienti")
@Log
@Tag(name = "ClientiController", description = "Controller Operazioni di Gestione Dati Clienti")
public class ClientiController
{
    @Autowired
    private ClientiService clientiService;

    @Autowired
    private ResourceBundleMessageSource errMessage;

    @GetMapping(value = "/cerca/codice/{IdCliente}" , produces = "application/json")
    @SneakyThrows
    public ResponseEntity<ClientiDto> listCliByIdCliente(@PathVariable("IdCliente") Integer IdCliente)
    {
        log.info("****** Otteniamo il cliente con id: " + IdCliente + "******");
        ClientiDto cliente = clientiService.SelByIdCliente(IdCliente);


        if(cliente == null){
            String ErrMsg = String.format("Il cliente con codice %s non trovato", IdCliente);
            log.severe(ErrMsg);

            throw new NotFoundException(ErrMsg);
        }

        return new  ResponseEntity<>(cliente, HttpStatus.OK );
    }

    @GetMapping(value = "/cerca/telefono/{telefono}", produces = "application/json")
    @SneakyThrows
    public ResponseEntity<List<ClientiDto>> listCliByTelefono(@PathVariable("telefono") String telefono) {
        log.info("****** Otteniamo il cliente  con numero di telefono " + telefono + "******" );

        List<ClientiDto> clienti = clientiService.SelByTel(telefono);

        if (clienti.isEmpty()) {
            String ErrMsg = String.format("Non è stato trovato nessun cliente con il numero %s", telefono);
            log.warning(ErrMsg);
            throw new NotFoundException(ErrMsg);
        }
        return new ResponseEntity<>(clienti, HttpStatus.OK);
    }

    //-----------------------INSERIMENTO CLIENTE-------------------------------
    @PostMapping(value = "/inserisci", consumes = {"application/json", "application/json;charset=UTF-8"}, produces = "application/json")
    @SneakyThrows
    public ResponseEntity<InfoMsg> createClienti(@Valid @RequestBody Clienti cliente, BindingResult bindingResult)
    {
        log.info("Salviamo il cliente: " + cliente.getIdCliente());

        if(bindingResult.hasErrors())
        {
            String MsgErr = errMessage.getMessage(bindingResult.getFieldError(), LocaleContextHolder.getLocale());
            log.warning(MsgErr);

            throw new BindingException(MsgErr);
        }

        ClientiDto checkProd = clientiService.SelByIdCliente(cliente.getIdCliente());

        if(checkProd != null)
        {
            String MsgErr = String.format("Cliente %s presente in anagrafica!" + "Impossibile utilizzare il metodo POST" , cliente.getIdCliente());
            log.warning(MsgErr);

            throw new DuplicateException(MsgErr);
        }

        clientiService.InsClienti(cliente);

        return new ResponseEntity<InfoMsg>(new InfoMsg(LocalDate.now(),
                "Inserimento Cliente Eseguito con successo!"), HttpStatus.CREATED);
    }

    //----------------------MODIFICA CLIENTE------------------------------
    @RequestMapping(value = "/modifica", method = RequestMethod.PUT)
    @SneakyThrows
    public ResponseEntity<InfoMsg> updateCliente(@Valid @RequestBody Clienti cliente, BindingResult bindingResult)
    {
        log.info("Modifichiamo il cliente con id: " + cliente.getIdCliente());

        if(bindingResult.hasErrors())
        {
            String MsgErr = errMessage.getMessage(bindingResult.getFieldError(), LocaleContextHolder.getLocale());
            log.warning(MsgErr);

            throw new BindingException(MsgErr);
        }

        ClientiDto checkProd = clientiService.SelByIdCliente(cliente.getIdCliente());

        if(checkProd == null)
        {
            String MsgErr = String.format("Cliente %s non trovato in anagrafica! " + "Impossibile utilizzare il metodo PUT ", cliente.getIdCliente());
            log.warning(MsgErr);

            throw new BindingException(MsgErr);
        }

        clientiService.InsClienti(cliente);

        return new ResponseEntity<InfoMsg>(new InfoMsg(LocalDate.now(), "Modifica CLiente Eseguita con successo! "), HttpStatus.OK);
    }

    //-------------------------ELIMINAZIONE CLIENTE------------------------------------
    @DeleteMapping(value = "/elimina/{IdCliente}", produces = "application/json")
    @SneakyThrows
    public ResponseEntity<?>deleteCliente(@PathVariable("IdCliente") Integer IdCliente)
    {
        log.info("Eliminamo il cliente con id: " + IdCliente);

        Clienti cliente = clientiService.SelByIdCliente2(IdCliente);

        if(cliente == null)
        {
            String MsgErr = String.format("Cliente %s non presente in anagrafica! ", IdCliente);
            log.warning(MsgErr);

            throw new NotFoundException(MsgErr);
        }

        clientiService.InsClienti(cliente);

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode responseNode = mapper.createObjectNode();

        responseNode.put("code", HttpStatus.OK.toString());
        responseNode.put("message", "Cliente eliminato " + IdCliente + " eseguito con successo! ");

        return new ResponseEntity<>(responseNode, new HttpHeaders(), HttpStatus.OK);
    }
}
