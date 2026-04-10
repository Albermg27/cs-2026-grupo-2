package es.codeurjc.unit;

import es.codeurjc.model.Account;
import es.codeurjc.model.Notification;
import es.codeurjc.model.Transaction;
import es.codeurjc.model.User;
import es.codeurjc.repository.AccountRepository;
import es.codeurjc.repository.TransactionRepository;
import es.codeurjc.service.AccountService;
import es.codeurjc.service.RandomService;
import es.codeurjc.service.notifications.EmailNotificationService;
import es.codeurjc.service.notifications.SmsNotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    private AccountRepository accountRepository = mock(AccountRepository.class);
    private TransactionRepository transactionRepository = mock(TransactionRepository.class);
    private EmailNotificationService emailService = mock(EmailNotificationService.class);
    private SmsNotificationService smsService = mock(SmsNotificationService.class);
    private RandomService randomService = mock(RandomService.class);

    private AccountService accountService = new AccountService(accountRepository, transactionRepository, emailService,
            smsService, randomService);

    private User user;
    private Account account;
    private final String accountNumber = "ES0123456789";

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUsername("testuser");
        user.setEmail("test@test.com");

        account = new Account(accountNumber, Account.AccountType.SAVINGS, 1000.0);
        account.setUser(user);
    }

    // --- Tests for deposit(String accountNumber, double amount, String
    // description) ---

    @Test
    void depositWithDescription_amountZero_throwsException() {

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            accountService.deposit(accountNumber, 0.0, "Test deposit");
        });
        assertEquals("Amount must be positive", exception.getMessage());
    }

    @Test
    void depositWithDescription_amountNegative_throwsException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            accountService.deposit(accountNumber, -50.0, "Test deposit");
        });
        assertEquals("Amount must be positive", exception.getMessage());
    }

    @Test
    void depositWithDescription_amountGreaterThan10000_throwsException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            accountService.deposit(accountNumber, 15000.0, "Test deposit");
        });
        assertEquals("Amount exceeds maximum deposit limit", exception.getMessage());
    }

    @Test
    void depositWithDescription_amountGreaterThan50000_throwsException() {
        // This branch in the service is unreachable because > 10000 throws first
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            accountService.deposit(accountNumber, 60000.0, "Test deposit");
        });
        assertEquals("Amount exceeds maximum deposit limit", exception.getMessage());
    }

    @Test
    void depositWithDescription_accountNotFound_throwsException() {
        when(accountRepository.findByAccountNumber(accountNumber)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            accountService.deposit(accountNumber, 100.0, "Test deposit");
        });
        assertEquals("Account not found", exception.getMessage());
    }

    @Test
    void depositWithDescription_validAmountAndEmailNotification_success() {
        user.setNotificationType(User.NotificationType.EMAIL);
        when(accountRepository.findByAccountNumber(accountNumber)).thenReturn(Optional.of(account));
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        Account savedAccount = accountService.deposit(accountNumber, 100.0, "Test deposit");

        assertEquals(1100.0, savedAccount.getBalance());

        verify(transactionRepository).save(any(Transaction.class));
        verify(accountRepository).save(account);
        verify(emailService).sendNotification(
                eq(user),
                eq(Notification.NotificationType.DEPOSIT),
                eq("Deposit Confirmation"),
                eq("Deposit of 100,00 EUR. New balance: 1100,00 EUR"));
        verifyNoInteractions(smsService);
    }

    @Test
    void depositWithDescription_validAmountAndSmsNotification_success() {
        user.setNotificationType(User.NotificationType.SMS);
        when(accountRepository.findByAccountNumber(accountNumber)).thenReturn(Optional.of(account));
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        Account savedAccount = accountService.deposit(accountNumber, 100.0, "Test deposit");

        assertEquals(1100.0, savedAccount.getBalance());

        verify(transactionRepository).save(any(Transaction.class));
        verify(accountRepository).save(account);
        verify(smsService).sendNotification(
                eq(user),
                eq(Notification.NotificationType.DEPOSIT),
                eq("Deposit Confirmation"),
                eq("Deposit: 100,00 EUR. Balance: 1100,00 EUR"));
        verifyNoInteractions(emailService);
    }

    @Test
    void depositWithDescription_validAmountAndNoNotification_success() {
        user.setNotificationType(null);
        when(accountRepository.findByAccountNumber(accountNumber)).thenReturn(Optional.of(account));
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        Account savedAccount = accountService.deposit(accountNumber, 100.0, "Test deposit");

        assertEquals(1100.0, savedAccount.getBalance());

        verify(transactionRepository).save(any(Transaction.class));
        verify(accountRepository).save(account);
        verifyNoInteractions(emailService);
        verifyNoInteractions(smsService);
    }

    // --- Tests for deposit(String accountNumber, double amount) ---

    @Test
    void depositNoDescription_amountZero_throwsException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            accountService.deposit(accountNumber, 0.0);
        });
        assertEquals("Amount must be positive", exception.getMessage());
    }

    @Test
    void depositNoDescription_amountNegative_throwsException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            accountService.deposit(accountNumber, -50.0);
        });
        assertEquals("Amount must be positive", exception.getMessage());
    }

    @Test
    void depositNoDescription_amountGreaterThan10000_throwsException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            accountService.deposit(accountNumber, 15000.0);
        });
        assertEquals("Amount exceeds maximum deposit limit", exception.getMessage());
    }

    @Test
    void depositNoDescription_amountGreaterThan50000_throwsException() {
        // This branch in the service is unreachable because > 10000 throws first
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            accountService.deposit(accountNumber, 60000.0);
        });
        assertEquals("Amount exceeds maximum deposit limit", exception.getMessage());
    }

    @Test
    void depositNoDescription_accountNotFound_throwsException() {
        when(accountRepository.findByAccountNumber(accountNumber)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            accountService.deposit(accountNumber, 100.0);
        });
        assertEquals("Account not found", exception.getMessage());
    }

    @Test
    void depositNoDescription_validAmountAndEmailNotification_success() {
        user.setNotificationType(User.NotificationType.EMAIL);
        when(accountRepository.findByAccountNumber(accountNumber)).thenReturn(Optional.of(account));
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        Account savedAccount = accountService.deposit(accountNumber, 100.0);

        assertEquals(1100.0, savedAccount.getBalance());

        verify(transactionRepository).save(any(Transaction.class));
        verify(accountRepository).save(account);
        verify(emailService).sendNotification(
                eq(user),
                eq(Notification.NotificationType.DEPOSIT),
                eq("Deposit Confirmation"),
                eq("Deposit of 100,00 EUR. New balance: 1100,00 EUR"));
        verifyNoInteractions(smsService);
    }

    // --- Tests for getAccount(String accountNumber) and getUserAccounts(User user) ---

    @Test
    void getAccount_accountNotFound_throwsException() {
        // GIVEN
        when(accountRepository.findByAccountNumber("ES000")).thenReturn(Optional.empty());

        // WHEN & THEN
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            accountService.getAccount("ES000");
        });
        assertEquals("Account not found", exception.getMessage());
    }

    @Test
    void getAccount_validAccountNumber_success() {
        // GIVEN
        Account account = new Account("ES123", Account.AccountType.CHECKING, 1000.0);
        when(accountRepository.findByAccountNumber("ES123")).thenReturn(Optional.of(account));

        // WHEN
        Account result = accountService.getAccount("ES123");

        // THEN
        assertEquals("ES123", result.getAccountNumber());
        assertEquals(1000.0, result.getBalance());
    }

    @Test
    void getUserAccounts_noAccounts_returnsEmptyList() {
        // GIVEN
        User user = new User();
        when(accountRepository.findByUser(user)).thenReturn(Collections.emptyList());

        // WHEN
        List<Account> result = accountService.getUserAccounts(user);

        // THEN
        assertEquals(0, result.size());
    }

    @Test
    void getUserAccounts_withAccounts_returnsList() {
        // GIVEN
        User user = new User();
        List<Account> accounts = Arrays.asList(
            new Account("ES1", Account.AccountType.CHECKING, 100.0),
            new Account("ES2", Account.AccountType.SAVINGS, 200.0)
        );
        when(accountRepository.findByUser(user)).thenReturn(accounts);

        // WHEN
        List<Account> result = accountService.getUserAccounts(user);

        // THEN
        assertEquals(2, result.size());
        assertEquals("ES1", result.get(0).getAccountNumber());
        assertEquals("ES2", result.get(1).getAccountNumber());
    }
}