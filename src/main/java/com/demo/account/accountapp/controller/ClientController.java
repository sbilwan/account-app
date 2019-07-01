package com.demo.account.accountapp.controller;

import com.demo.account.accountapp.model.Client;
import com.demo.account.accountapp.service.ClientHandlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

/**
 * The client controller that intercepts the request related to client operations.
 *
 */
@RestController
@RequestMapping(path = ClientController.PATH_MAPPING )
public class ClientController {

    public static final String PATH_MAPPING = "demoaccountapp/v1";

    private final ClientHandlerService clientHandlerService;

    @Autowired
    ClientController(final ClientHandlerService clientHandlerService){
        this.clientHandlerService = clientHandlerService;
    }

    @PostMapping(value = "clients", consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Client> createClient(@RequestBody final Client requestClientObject) {
        return new ResponseEntity<>(clientHandlerService.createClient(requestClientObject), HttpStatus.CREATED);
    }

    @GetMapping(value = "clients", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Set<Client>> listClients() {
        return new ResponseEntity<>(clientHandlerService.listClients(),
                HttpStatus.OK);
    }

    @GetMapping(value = "clients/{id}", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Client> listClientWith(@PathVariable("id") Long clientId) {
        return new ResponseEntity<>(clientHandlerService.getClient(clientId),
                HttpStatus.OK);
    }

}
