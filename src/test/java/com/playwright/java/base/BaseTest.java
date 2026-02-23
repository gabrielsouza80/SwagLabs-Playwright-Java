package com.playwright.java.base;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Browser.NewContextOptions;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserContext.StorageStateOptions;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import io.qameta.allure.Allure;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.TestInstance;
import com.playwright.java.config.TestConfig;
import com.playwright.java.pages.HomePage;
import com.playwright.java.pages.InventoryPage;
import com.playwright.java.pages.LoginPage;

// Classe base de todos os testes.
// Tudo que é comum (abrir browser, login, fechar browser) fica aqui.
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class BaseTest {
    // Objetos principais do Playwright.
    protected Playwright playwright;
    protected Browser browser;
    protected BrowserContext context;
    protected Page page;
    protected Path authStorageStatePath;

    // Configurações carregadas de src/test/resources/config.properties.
    protected TestConfig config;

    // Page Objects reutilizados em qualquer teste.
    protected LoginPage loginPage;
    protected InventoryPage inventoryPage;
    protected HomePage homePage;

    // Executa uma vez por classe de teste.
    // Faz login uma única vez e salva o estado autenticado.
    @BeforeAll
    void setUpSuite() {
        config = TestConfig.load();

        writeAllureEnvironment();

        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(config.headless()));

        try {
            authStorageStatePath = Files.createTempFile("playwright-auth-", ".json");
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to create auth storage state file", exception);
        }

        BrowserContext authContext = browser.newContext();
        Page authPage = authContext.newPage();

        LoginPage authLoginPage = new LoginPage(authPage);
        HomePage authHomePage = new HomePage(authPage);

        authLoginPage.open(config.baseUrl());
        assertTrue(authLoginPage.isLoaded());
        authLoginPage.login(config.username(), config.password());
        assertTrue(authHomePage.isLoaded());

        authContext.storageState(new StorageStateOptions().setPath(authStorageStatePath));
        authContext.close();
    }

    // Executa antes de cada teste: cria contexto isolado e já autenticado.
    // Assim os testes ficam independentes entre si.
    @BeforeEach
    void setUpTest() {
        context = browser.newContext(new NewContextOptions().setStorageStatePath(authStorageStatePath));
        page = context.newPage();

        loginPage = new LoginPage(page);
        inventoryPage = new InventoryPage(page);
        homePage = new HomePage(page);

        String inventoryUrl = config.baseUrl().endsWith("/")
                ? config.baseUrl() + "inventory.html"
                : config.baseUrl() + "/inventory.html";

        page.navigate(inventoryUrl);
        assertTrue(homePage.isLoaded());
    }

    // Executa após cada teste para garantir isolamento.
    @AfterEach
    void tearDownTest(TestInfo testInfo) {
        saveScreenshot(testInfo);

        if (context != null) {
            context.close();
        }
    }

    // Tira screenshot ao final de cada teste.
    // Arquivos ficam em target/reports/screenshots e também são anexados ao Allure.
    private void saveScreenshot(TestInfo testInfo) {
        if (page == null) {
            return;
        }

        try {
            Path screenshotsDir = Path.of("target", "reports", "screenshots");
            Files.createDirectories(screenshotsDir);

            String className = testInfo.getTestClass().map(Class::getSimpleName).orElse("UnknownClass");
            String methodName = testInfo.getTestMethod().map(method -> method.getName()).orElse("UnknownMethod");
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss-SSS"));

            String fileName = sanitizeForFileName(className)
                    + "_"
                    + sanitizeForFileName(methodName)
                    + "_"
                    + timestamp
                    + ".png";

            byte[] screenshotBytes = page.screenshot(new Page.ScreenshotOptions().setFullPage(true));

            Files.write(screenshotsDir.resolve(fileName), screenshotBytes);

            Allure.addAttachment(
                    className + "." + methodName,
                    "image/png",
                    new ByteArrayInputStream(screenshotBytes),
                    ".png");
        } catch (Exception exception) {
            System.err.println("Failed to capture screenshot in tearDown: " + exception.getMessage());
        }
    }

    private String sanitizeForFileName(String value) {
        return value.replaceAll("[^a-zA-Z0-9._-]", "_");
    }

    // Escreve informações de ambiente para aparecer na aba Environment do Allure.
    private void writeAllureEnvironment() {
        try {
            Path allureResultsDir = Path.of("target", "allure-results");
            Files.createDirectories(allureResultsDir);

            Properties environment = new Properties();
            environment.setProperty("Application URL", config.baseUrl());
            environment.setProperty("Browser", "Chromium");
            environment.setProperty("Headless", String.valueOf(config.headless()));
            environment.setProperty("Java Version", System.getProperty("java.version"));
            environment.setProperty("OS", System.getProperty("os.name") + " " + System.getProperty("os.version"));

            try (OutputStream output = Files.newOutputStream(allureResultsDir.resolve("environment.properties"))) {
                environment.store(output, "Allure Environment");
            }

            String categoriesJson = "[\n"
                    + "  {\n"
                    + "    \"name\": \"Product defects\",\n"
                    + "    \"matchedStatuses\": [\"failed\"]\n"
                    + "  },\n"
                    + "  {\n"
                    + "    \"name\": \"Test defects\",\n"
                    + "    \"matchedStatuses\": [\"broken\"]\n"
                    + "  }\n"
                    + "]";

            Files.writeString(
                    allureResultsDir.resolve("categories.json"),
                    categoriesJson,
                    StandardCharsets.UTF_8);
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to write Allure environment.properties", exception);
        }
    }

    // Executa uma vez por classe de teste.
    @AfterAll
    void tearDownSuite() {
        if (browser != null) {
            browser.close();
        }
        if (playwright != null) {
            playwright.close();
        }
        if (authStorageStatePath != null) {
            try {
                Files.deleteIfExists(authStorageStatePath);
            } catch (IOException exception) {
                throw new IllegalStateException("Failed to delete auth storage state file", exception);
            }
        }
    }
}
