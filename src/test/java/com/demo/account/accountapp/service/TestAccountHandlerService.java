package com.demo.account.accountapp.service;

import com.demo.account.accountapp.dao.entity.AccountEntity;
import com.demo.account.accountapp.dao.entity.AccountStatus;
import com.demo.account.accountapp.error.BusinessException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(SpringRunner.class)
public class TestAccountHandlerService {


    @InjectMocks
    AccountHandlerService accountHandlerServiceMock;

    @Test
    public void testTransferWhenTransferAmountIsMoreThanCreditBalance() {
        AccountEntity source = new AccountEntity();
        source.setBalance(new BigDecimal("1000"));
        source.setAccountStatus("CR");
        source.setOverDrawLimit(new BigDecimal("300"));

        AccountEntity destination = new AccountEntity();
        destination.setBalance(new BigDecimal("500"));
        destination.setAccountStatus("CR");
        destination.setOverDrawLimit(new BigDecimal("400"));

        BigDecimal transferAmount = new BigDecimal("1100");

        accountHandlerServiceMock.transferBalance(source, destination, transferAmount);

        assertThat("Source account balance is in DR status",
                source.getAccountStatus(), is(AccountStatus.DR.name()));
        assertThat("Destination account balance is in CR status",
                destination.getAccountStatus(), is(AccountStatus.CR.name()));

        assertThat("Source account balance after transfer is",
                source.getBalance().toString(), is("100"));
        assertThat("Destination account balance after transfer is",
                destination.getBalance().toString(), is("1600"));
    }

    @Test
    public void testTransferWhenSourceAccountIsInDebitBalance() {
        AccountEntity source = new AccountEntity();
        source.setBalance(new BigDecimal("200"));
        source.setAccountStatus("DR");
        source.setOverDrawLimit(new BigDecimal("400"));

        AccountEntity destination = new AccountEntity();
        destination.setBalance(new BigDecimal("500"));
        destination.setAccountStatus("CR");
        destination.setOverDrawLimit(new BigDecimal("400"));

        BigDecimal transferAmount = new BigDecimal("150");

        accountHandlerServiceMock.transferBalance(source, destination, transferAmount);

        assertThat("Source account balance is still in DR status",
                source.getAccountStatus(), is(AccountStatus.DR.name()));
        assertThat("Destination account balance is still in CR status",
                destination.getAccountStatus(), is(AccountStatus.CR.name()));

        assertThat("Source account balance after transfer is",
                source.getBalance().toString(), is("350"));
        assertThat("Destination account balance after transfer is",
                destination.getBalance().toString(), is("650"));
    }


    @Test
    public void testTransferWhenDestAccountIsInDRBalanceAndAfterBigTransferIsInCRBalance() {
        AccountEntity source = new AccountEntity();
        source.setBalance(new BigDecimal("1200"));
        source.setAccountStatus("CR");
        source.setOverDrawLimit(new BigDecimal("200"));

        AccountEntity destination = new AccountEntity();
        destination.setBalance(new BigDecimal("100"));
        destination.setAccountStatus("DR");
        destination.setOverDrawLimit(new BigDecimal("300"));

        BigDecimal transferAmount = new BigDecimal("150");

        accountHandlerServiceMock.transferBalance(source, destination, transferAmount);

        assertThat("Source account balance is still in CR status",
                source.getAccountStatus(), is(AccountStatus.CR.name()));
        assertThat("Destination account balance is now in CR status",
                destination.getAccountStatus(), is(AccountStatus.CR.name()));

        assertThat("Source account balance after transfer is",
                source.getBalance().toString(), is("1050"));
        assertThat("Destination account balance after transfer is",
                destination.getBalance().toString(), is("50"));
    }

    @Test
    public void testTransferWhenDestAccountIsInDRBalanceAndAfterSmallTransferDRBalanceDecreases() {
        AccountEntity source = new AccountEntity();
        source.setBalance(new BigDecimal("1200"));
        source.setAccountStatus("CR");
        source.setOverDrawLimit(new BigDecimal("200"));

        AccountEntity destination = new AccountEntity();
        destination.setBalance(new BigDecimal("100"));
        destination.setAccountStatus("DR");
        destination.setOverDrawLimit(new BigDecimal("300"));

        BigDecimal transferAmount = new BigDecimal("50");

        accountHandlerServiceMock.transferBalance(source, destination, transferAmount);

        assertThat("Source account balance is still in CR status",
                source.getAccountStatus(), is(AccountStatus.CR.name()));
        assertThat("Destination account balance is still in DR status",
                destination.getAccountStatus(), is(AccountStatus.DR.name()));

        assertThat("Source account balance after transfer is",
                source.getBalance().toString(), is("1150"));
        assertThat("Destination account balance after transfer is",
                destination.getBalance().toString(), is("50"));
    }

    @Test(expected = BusinessException.class)
    public void testAfterMaxOverDrawnLimitReachedFurtherTransferRequestIsRejected() {
        AccountEntity source = new AccountEntity();
        source.setBalance(new BigDecimal("100"));
        source.setAccountStatus("DR");
        source.setOverDrawLimit(new BigDecimal("100"));

        AccountEntity destination = new AccountEntity();
        destination.setBalance(new BigDecimal("100"));
        destination.setAccountStatus("DR");
        destination.setOverDrawLimit(new BigDecimal("300"));

        BigDecimal transferAmount = new BigDecimal("50");

        accountHandlerServiceMock.transferBalance(source, destination, transferAmount);
    }

}
