package es.codeurjc.e2e;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;
import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;

import es.codeurjc.BankingApplication;
import es.codeurjc.model.Account;
import es.codeurjc.model.User;
import es.codeurjc.repository.AccountRepository;
import es.codeurjc.repository.UserRepository;
import es.codeurjc.repository.NotificationRepository;

import java.time.Duration;
import java.time.LocalDate;

@ActiveProfiles("test")
@SpringBootTest(classes = BankingApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TransferE2ETest {

    @LocalServerPort
    int port;

    private WebDriver driver;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private NotificationRepository notificationRepository; // AGREGADO POR TI

    @Autowired
    private PasswordEncoder passwordEncoder;

    private String testUsername = "test_user";
    private String testPassword = "testingPass123";

    private String userBName = "user_b";
    private String userBPass = "passB123";

    @BeforeEach
    public void setupTest() {
        notificationRepository.deleteAll();
        accountRepository.deleteAll();
        userRepository.deleteAll();

        driver = new ChromeDriver();

        // Create test user
        User testUser = new User(
                testUsername,
                passwordEncoder.encode(testPassword),
                "CUSTOMER");
        testUser.setFirstName("Usuario");
        testUser.setLastName("De Prueba");
        testUser.setDni("99999999Z");
        testUser.setEmail("test@email.com");
        testUser.setPhone("+34600112233");
        testUser.setRegistrationDate(LocalDate.now());
        testUser = userRepository.save(testUser);

        // Create first account
        Account account1 = new Account("ES9999999991", Account.AccountType.CHECKING, 1000.0);
        account1.setUser(testUser);
        accountRepository.save(account1);

        // Create second account
        Account account2 = new Account("ES9999999992", Account.AccountType.SAVINGS, 0.0);
        account2.setUser(testUser);
        accountRepository.save(account2);

        // Create test user B
        User userB = new User(userBName, passwordEncoder.encode(userBPass), "CUSTOMER");
        userB.setDni("88888888X");
        userB.setEmail("userB@test.com");
        userB = userRepository.save(userB);

        // Create account for user B
        Account accountB = new Account("ES0000000000", Account.AccountType.CHECKING, 500.0);
        accountB.setUser(userB);
        accountRepository.save(accountB);
    }

    @AfterEach
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testTransferOwnAccountsSuccess() throws InterruptedException {
        // En lugar de ir directamente a localhost, iniciamos sesión primero:
        driver.get("http://localhost:" + port + "/login");

        // Log in
        driver.findElement(By.id("username")).sendKeys(testUsername);
        driver.findElement(By.id("password")).sendKeys(testPassword);
        driver.findElement(By.id("loginButton")).click();

        // Go to transfer page
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        WebElement text = wait.until(presenceOfElementLocated(By.cssSelector("a[href='/transfer']")));
        text.click();

        // Select "from account" from drop-down
        WebElement fromAccountElement = wait.until(presenceOfElementLocated(By.id("fromAccount")));
        Select fromAccountDropdown = new Select(fromAccountElement);
        fromAccountDropdown.selectByValue("ES9999999991");

        // Write "to account" number
        driver.findElement(By.id("toAccount")).sendKeys("ES9999999992");

        // Enter amount
        driver.findElement(By.id("amount")).sendKeys("100");

        // Submit transfer
        driver.findElement(By.id("transferButton")).click();

        // Verify success message
        WebElement successMessage = wait.until(
                presenceOfElementLocated(By.cssSelector(".alert.alert-success")));
        assertTrue(successMessage.getText().contains("Transfer completed successfully"));

        // Verify new balance
        Account account1 = accountRepository.findByAccountNumber("ES9999999991").get();
        Account account2 = accountRepository.findByAccountNumber("ES9999999992").get();
        assertEquals(900.0, account1.getBalance());
        assertEquals(100.0, account2.getBalance());

    }

    @Test
    public void testTransferInsufficientFunds() {
        // Log in to the application
        driver.get("http://localhost:" + port + "/login");
        driver.findElement(By.id("username")).sendKeys(testUsername);
        driver.findElement(By.id("password")).sendKeys(testPassword);
        driver.findElement(By.id("loginButton")).click();

        // Navigate to the transfer page
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement transferLink = wait.until(presenceOfElementLocated(By.cssSelector("a[href='/transfer']")));
        transferLink.click();

        // Fill the transfer form with an amount greater than the balance
        WebElement fromAccountElement = wait.until(presenceOfElementLocated(By.id("fromAccount")));
        Select fromAccountDropdown = new Select(fromAccountElement);
        fromAccountDropdown.selectByValue("ES9999999991");

        driver.findElement(By.id("toAccount")).sendKeys("ES9999999992");
        driver.findElement(By.id("amount")).sendKeys("1500");

        // Submit the transfer
        driver.findElement(By.id("transferButton")).click();

        // Verify error message is displayed
        WebElement errorMessage = wait.until(
                presenceOfElementLocated(By.cssSelector(".alert.alert-danger")));

        assertTrue(errorMessage.getText().contains("Insufficient funds"),
                "The error message should indicate insufficient funds");

        // Verify balances have NOT changed in the database
        Account accountSource = accountRepository.findByAccountNumber("ES9999999991").get();
        Account accountDest = accountRepository.findByAccountNumber("ES9999999992").get();

        assertEquals(1000.0, accountSource.getBalance(), "Source balance should remain unchanged");
        assertEquals(0.0, accountDest.getBalance(), "Destination balance should remain unchanged");
    }

    @Test
    public void testTransferNegativeAmount() {

        // Log in to the application
        driver.get("http://localhost:" + port + "/login");
        driver.findElement(By.id("username")).sendKeys(testUsername);
        driver.findElement(By.id("password")).sendKeys(testPassword);
        driver.findElement(By.id("loginButton")).click();

        // Navigate to the transfer page
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement transferLink = wait.until(
                presenceOfElementLocated(By.cssSelector("a[href='/transfer']")));
        transferLink.click();

        // Fill the transfer form with a NEGATIVE amount
        WebElement fromAccountElement = wait.until(
                presenceOfElementLocated(By.id("fromAccount")));

        Select fromAccountDropdown = new Select(fromAccountElement);
        fromAccountDropdown.selectByValue("ES9999999991");

        driver.findElement(By.id("toAccount")).sendKeys("ES9999999992");
        driver.findElement(By.id("amount")).sendKeys("-100");

        // Submit the transfer
        driver.findElement(By.id("transferButton")).click();

        // Verify error message is displayed
        WebElement errorMessage = wait.until(
                presenceOfElementLocated(By.cssSelector(".alert.alert-danger")));

        assertTrue(errorMessage.getText().contains("Amount must be positive"),
                "The error message should indicate Amount must be positive");


        // Verify balances have NOT changed in the database
        Account accountSource =
                accountRepository.findByAccountNumber("ES9999999991").get();

        Account accountDest =
                accountRepository.findByAccountNumber("ES9999999992").get();

        assertEquals(1000.0, accountSource.getBalance(),
                "Source balance should remain unchanged");

        assertEquals(0.0, accountDest.getBalance(),
                "Destination balance should remain unchanged");
    }

    @Test
    public void testTransferAmountGreaterThan20000() {
        // Log in to the application
        driver.get("http://localhost:" + port + "/login");
        driver.findElement(By.id("username")).sendKeys(testUsername);
        driver.findElement(By.id("password")).sendKeys(testPassword);
        driver.findElement(By.id("loginButton")).click();

        // Navigate to the transfer page
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement transferLink = wait.until(
                presenceOfElementLocated(By.cssSelector("a[href='/transfer']")));
        transferLink.click();

        // Fill the transfer form with an amount greater than 20,000
        WebElement fromAccountElement = wait.until(presenceOfElementLocated(By.id("fromAccount")));

        Select fromAccountDropdown = new Select(fromAccountElement);
        fromAccountDropdown.selectByValue("ES9999999991");

        driver.findElement(By.id("toAccount")).sendKeys("ES9999999992");
        driver.findElement(By.id("amount")).sendKeys("20001");

        // Submit the transfer
        driver.findElement(By.id("transferButton")).click();

        // Verify error message is displayed
        WebElement errorMessage = wait.until(
                presenceOfElementLocated(By.cssSelector(".alert.alert-danger")));

        assertTrue(errorMessage.getText().contains("Amount exceeds maximum limit of 20,000"),
                "The error message should indicate Amount exceeds maximum limit of 20,000");

        // Verify balances have NOT changed in the database
        Account accountSource = accountRepository.findByAccountNumber("ES9999999991").get();
        Account accountDest = accountRepository.findByAccountNumber("ES9999999992").get();

        assertEquals(1000.0, accountSource.getBalance(), "Source balance should remain unchanged");
        assertEquals(0.0, accountDest.getBalance(), "Destination balance should remain unchanged");
    }

    @Test
    public void testTransferToDifferentUserSuccess() {
        // Log in
        driver.get("http://localhost:" + port + "/login");
        driver.findElement(By.id("username")).sendKeys(testUsername);
        driver.findElement(By.id("password")).sendKeys(testPassword);
        driver.findElement(By.id("loginButton")).click();

        // Navigate to the transfer page
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(presenceOfElementLocated(By.cssSelector("a[href='/transfer']"))).click();

        // Source: User A Account | Destination: User B Account
        WebElement fromAccountElement = wait.until(presenceOfElementLocated(By.id("fromAccount")));
        new Select(fromAccountElement).selectByValue("ES9999999991");

        driver.findElement(By.id("toAccount")).sendKeys("ES0000000000"); 
        driver.findElement(By.id("amount")).sendKeys("200");
        driver.findElement(By.id("transferButton")).click();

        // Verify success message
        WebElement successMessage = wait.until(presenceOfElementLocated(By.cssSelector(".alert.alert-success")));
        assertTrue(successMessage.getText().contains("Transfer completed successfully"));

        // Verify balances in the database (Both are required by Task 5)
        Account sourceAcc = accountRepository.findByAccountNumber("ES9999999991").get();
        Account targetAcc = accountRepository.findByAccountNumber("ES0000000000").get();

        assertEquals(800.0, sourceAcc.getBalance());
        assertEquals(700.0, targetAcc.getBalance());
    }

    @Test
    public void testTransferToSameAccountFails() {
        // Log in 
        driver.get("http://localhost:" + port + "/login");
        driver.findElement(By.id("username")).sendKeys(testUsername);
        driver.findElement(By.id("password")).sendKeys(testPassword);
        driver.findElement(By.id("loginButton")).click();

        // Navigate to the transfer page
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement transferLink = wait.until(presenceOfElementLocated(By.cssSelector("a[href='/transfer']")));
        transferLink.click();

        // Attempt transfer to the same account
        String sameAccountNumber = "ES9999999991";
        WebElement fromAccountElement = wait.until(presenceOfElementLocated(By.id("fromAccount")));
        new Select(fromAccountElement).selectByValue(sameAccountNumber);

        driver.findElement(By.id("toAccount")).sendKeys(sameAccountNumber);
        driver.findElement(By.id("amount")).sendKeys("100");

        // Submit transfer
        driver.findElement(By.id("transferButton")).click();

        // Verify an error message is displayed
        WebElement errorBox = wait.until(presenceOfElementLocated(By.cssSelector(".alert.alert-danger")));
        assertFalse(errorBox.getText().isEmpty(), "An error message should be displayed for same account transfer");

        // Verify database integrity: balance must remain unchanged
        Account account = accountRepository.findByAccountNumber(sameAccountNumber).get();
        assertEquals(1000.0, account.getBalance(), "Source balance should remain unchanged");
    }

}