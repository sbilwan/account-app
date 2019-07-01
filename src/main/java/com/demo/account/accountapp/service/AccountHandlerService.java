package com.demo.account.accountapp.service;

import com.demo.account.accountapp.dao.entity.AccountEntity;
import com.demo.account.accountapp.dao.entity.AccountStatus;
import com.demo.account.accountapp.dao.entity.ClientEntity;
import com.demo.account.accountapp.dao.entity.TransactionEntity;
import com.demo.account.accountapp.error.BusinessException;
import com.demo.account.accountapp.mapper.create.AccountAndAccountEntityMapper;
import com.demo.account.accountapp.mapper.create.TransferAndTransactionEntityMapper;
import com.demo.account.accountapp.model.Account;
import com.demo.account.accountapp.model.Transfer;
import com.demo.account.accountapp.repository.AccountRepository;
import com.demo.account.accountapp.repository.ClientRepository;
import com.demo.account.accountapp.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The account handler service that gets called by AccountController and connects to repositories anmappers
 * to provide response to the clients.
 *
 */
@Service
public class AccountHandlerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountHandlerService.class);

    private final AccountAndAccountEntityMapper accountAndAccountEntityMapper;

    private final TransferAndTransactionEntityMapper transferAndTransactionEntityMapper;

    private final AccountRepository accountRepository;

    private final ClientRepository clientRepository;

    private final TransactionRepository transactionRepository;

    @Autowired
    public AccountHandlerService(final AccountAndAccountEntityMapper mapper,
                                 final TransferAndTransactionEntityMapper transferAndTransactionEntityMapper,
                                 final AccountRepository accountRepository,
                                 final ClientRepository clientRepository,
                                 final TransactionRepository transactionRepository) {
        this.accountAndAccountEntityMapper = mapper;
        this.transferAndTransactionEntityMapper = transferAndTransactionEntityMapper;
        this.accountRepository = accountRepository;
        this.clientRepository = clientRepository;
        this.transactionRepository = transactionRepository;
    }

    public Account createAccount(final Long clientId, final Account account) {
        Optional<ClientEntity> clientEntity = clientRepository.findById(clientId);
        AccountEntity accountEntity = clientEntity.map(entity -> accountAndAccountEntityMapper.accountToAccountEntity(account, entity))
                .orElseThrow(() -> new BusinessException("No client exists with " + clientId));
        AccountEntity savedAccountEntity = accountRepository.save(accountEntity);
        return accountAndAccountEntityMapper.accountEntityToAccount(savedAccountEntity);
    }

    public Set<Account> listAccounts(final Long clientId) {
        List<AccountEntity> accountEntities = accountRepository.findByClientId(clientId);
        return accountEntities.stream()
                .map(accountAndAccountEntityMapper::accountEntityToAccount)
                .collect(Collectors.toSet());

    }

    public Set<Transfer> listTransactions(final Long accountId) {
        final List<TransactionEntity> transactionEntities = transactionRepository.findAccountTransactionByAccountId(accountId);
        return transactionEntities.stream()
                .map(transferAndTransactionEntityMapper::transactionEntityToTransfer)
                .collect(Collectors.toSet());
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = BusinessException.class )
    public void transfer(final Long clientId, final Transfer transferObject) {
        LOGGER.debug("Source Client details are now being fetched based on the client id {}", clientId);
        // Fetch the source client details including accounts.
        Optional<ClientEntity> sourceClientEntity = clientRepository.findById(clientId);
        LOGGER.debug("Destination account details are now being fetched {}", transferObject.getDestinationAccountId());
        // Fetch the destination account details.
        Optional<AccountEntity> destinationAccountEntity = accountRepository.findById(transferObject.getDestinationAccountId());
        // Check whether the client exists, whether the source account passes in the request is one of the client's accounts
        // and whether the destination account passed in the request actually exists.
        if (sourceClientEntity.isPresent() &&
                isSourceAccountValid(transferObject, sourceClientEntity)&&
                destinationAccountEntity.isPresent()) {
            Optional<AccountEntity> sourceAccountEntity = sourceClientEntity.get().getAccounts()
                    .stream()
                    .filter(ae -> ae.getId().equals(transferObject.getSourceAccountId()))
                    .findFirst();
            transferBalance(sourceAccountEntity.get(), destinationAccountEntity.get(), transferObject.getTransferAmount());
            saveTransaction(transferObject);
        } else {
            throw new BusinessException("Either or both clients are not registered.  " + clientId + " , " + transferObject.getDestinationAccountId());
        }

    }

    /**
     * The method will transfer the amount from source to destination account.
     *
     * @param source the source entity.
     * @param destination the destination entity.
     * @param transferAmount the amount to be transferred.
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public void transferBalance(AccountEntity source, AccountEntity destination, BigDecimal transferAmount) {
        // Case where money is not withdrawn from the overdraw balance.
        if (AccountStatus.valueOf(source.getAccountStatus()) == AccountStatus.CR) {
            // It means transfer amount is less than or equal to the available balance without using the overdraw balance.
            if (source.getBalance().compareTo(transferAmount) >= 0 ) {
                updateBalanceOfTheDestination(destination, transferAmount);
                // Simply deduct the balance from the source and add to the destination.
                final BigDecimal newBalanceInSource = source.getBalance().subtract(transferAmount);
                source.setBalance(newBalanceInSource);
            } else {
                // It means transfer amount is larger than the available balance and will require the overdraw balance.
                // Now check whether transfer is still large even after applying the overdraw limit
                if (transferAmount.compareTo(source.getBalance().add(source.getOverDrawLimit()) ) > 0) {
                    // This is not a valid transfer.
                    throw new BusinessException("Not enough balance in the account to transfer. Aborting the operation");
                }
                // It means source has to borrow from overdrawn balance and the account is now in DR status.
                updateBalanceOfTheDestination(destination, transferAmount);
                source.setBalance(transferAmount.subtract(source.getBalance()));
                source.setAccountStatus(AccountStatus.DR.name());
            }
        } else {
            // The source balance is already on DR balance.
            // if transferAmount is larger than the available overdrawn balance then it is not a valid transfer
            if (transferAmount.compareTo(source.getOverDrawLimit().subtract(source.getBalance())) > 0) {
                throw new BusinessException("Not enough balance in the account to transfer. Aborting the operation");
            }
            updateBalanceOfTheDestination(destination, transferAmount);
            // Add the debit balance to the transfer amount to increase the debit balance of the account.
            source.setBalance(source.getBalance().add(transferAmount));
        }
    }

    /**
     * The method will update the balance and account status of the destination account.
     * @param destination
     * @param transferAmount
     */
    private void updateBalanceOfTheDestination(AccountEntity destination, BigDecimal transferAmount) {
        // check the destination account balance state.
        if (AccountStatus.valueOf(destination.getAccountStatus()) == AccountStatus.DR) {
            // The destination account is in Debit balance this credit operation will decrease the debit balance.
            // If the transfer amount is larger than current debit balance then it means the account balance will be in CR.
            if (transferAmount.compareTo(destination.getBalance()) >=0) {
                //  Set the account balance in CR
                destination.setAccountStatus(AccountStatus.CR.name());
                destination.setBalance(transferAmount.subtract(destination.getBalance()));
            }else {
                // Destination balance will continue to be in DR but will decrease with the credit operation
                destination.setBalance(destination.getBalance().subtract(transferAmount));
            }
        } else {
            destination.setBalance(destination.getBalance().add(transferAmount));
        }
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void saveTransaction(final Transfer transferObject){
        transactionRepository.save(transferAndTransactionEntityMapper.transferToTransactionEntity(transferObject));
    }



    private boolean isSourceAccountValid(Transfer transferObject, Optional<ClientEntity> sourceClientEntity) {
        LOGGER.debug("Fetching all the accounts details of the client {}", sourceClientEntity.get().getId());
        return sourceClientEntity.get().getAccounts()
                .stream().
                anyMatch( ae -> ae.getId().equals(transferObject.getSourceAccountId()));
    }


}
