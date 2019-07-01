package com.demo.account.accountapp.controller;

import com.demo.account.accountapp.model.Account;
import com.demo.account.accountapp.model.Transfer;
import com.demo.account.accountapp.service.AccountHandlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

/**
 * The Account Controller will intercept all the requests related to the account, transfer and transaction.
 *
 */
@RestController
@RequestMapping(path = AccountController.PATH_MAPPING)
public class AccountController {

    public static final String PATH_MAPPING = "demoaccountapp/v1";

    private static final String SUCCESS_TRANSFER_MESSAGE = "{\n" +
            "\t\"message\": \"Amount transferred\"\n" +
            "}";

    private final AccountHandlerService accountHandlerService;

    @Autowired
    AccountController(final AccountHandlerService accountHandlerService) {
        this.accountHandlerService = accountHandlerService;
    }

    @PostMapping(value = "accounts/clients/{client_id}", consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Account> createAccount(@PathVariable("client_id") final Long clientId, @RequestBody final Account account) {
        return new ResponseEntity<>(accountHandlerService.createAccount(clientId, account), HttpStatus.CREATED);
    }

    @GetMapping(value = "accounts/clients/{client_id}",  produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Set<Account>> createAccount(@PathVariable("client_id") final Long clientId) {
        return new ResponseEntity<>(accountHandlerService.listAccounts(clientId), HttpStatus.OK);
    }

    @PostMapping(value = "accounts/clients/{client_id}/transfer",  consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<String> transfer(@PathVariable("client_id") final Long clientId, @RequestBody final Transfer transferObject) {
        accountHandlerService.transfer(clientId, transferObject);
        return new ResponseEntity<>(SUCCESS_TRANSFER_MESSAGE, HttpStatus.CREATED);
    }

    @GetMapping(value = "accounts/{account_id}/transactions",  produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Set<Transfer>> getTransactionsForAccount(@PathVariable("account_id") final Long accountId) {
        return new ResponseEntity<>(accountHandlerService.listTransactions(accountId), HttpStatus.OK);

    }


}
