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
import com.playwright.java.config.TestData;
import com.playwright.java.pages.ComponentsPage;
import com.playwright.java.pages.HomePage;
import com.playwright.java.pages.LoginPage;

// Base class for all tests.
// Shared setup and teardown (open browser, login, close browser) lives here.
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class BaseTest {
    // Main Playwright objects.
    protected Playwright playwright;
    protected Browser browser;
    protected BrowserContext context;
    protected Page page;
    protected Path authStorageStatePath;

    // Configuration loaded from src/test/resources/config.properties.
    protected TestConfig config;
    protected TestData testData;

    // Page Objects reused in every test.
    protected LoginPage loginPage;
    protected HomePage homePage;
    protected ComponentsPage componentsPage;

    protected boolean requiresAuthenticatedSession() {
        return true;
    }

    // Runs once per test class.
    // Performs login once and saves authenticated storage state.
    @BeforeAll
    void setUpSuite() {
        config = TestConfig.load();
        testData = TestData.get();

        writeAllureEnvironment();

        playwright = Playwright.create();
        BrowserType.LaunchOptions launchOptions = new BrowserType.LaunchOptions().setHeadless(config.headless());
        if (config.slowMoMs() > 0) {
            launchOptions.setSlowMo((double) config.slowMoMs());
        }
        browser = playwright.chromium().launch(launchOptions);

        if (!requiresAuthenticatedSession()) {
            return;
        }

        try {
            authStorageStatePath = Files.createTempFile("playwright-auth-", ".json");
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to create auth storage state file", exception);
        }

        BrowserContext authContext = browser.newContext(new NewContextOptions()
            .setViewportSize(config.viewportWidth(), config.viewportHeight()));
        Page authPage = authContext.newPage();
        configurePage(authPage);

        LoginPage authLoginPage = new LoginPage(authPage);
        HomePage authHomePage = new HomePage(authPage);

        authLoginPage.open(config.baseUrl());
        authLoginPage.loginWithStandardUser();
        assertTrue(authHomePage.isLoaded());

        authContext.storageState(new StorageStateOptions().setPath(authStorageStatePath));
        authContext.close();
    }

    // Runs before each test: creates an isolated and already authenticated context.
    // This keeps tests independent from one another.
    @BeforeEach
    void setUpTest() {
        Allure.label("owner", "Gabriel Souza");

        NewContextOptions contextOptions = new NewContextOptions()
                .setViewportSize(config.viewportWidth(), config.viewportHeight());

        if (requiresAuthenticatedSession()) {
            context = browser.newContext(contextOptions.setStorageStatePath(authStorageStatePath));
        } else {
            context = browser.newContext(contextOptions);
        }

        page = context.newPage();
        configurePage(page);

        loginPage = new LoginPage(page);
        homePage = new HomePage(page);
        componentsPage = new ComponentsPage(page);

        if (requiresAuthenticatedSession()) {
            String inventoryRoute = testData.route("inventory");
            String normalizedRoute = inventoryRoute.startsWith("/") ? inventoryRoute.substring(1) : inventoryRoute;
            String inventoryUrl = config.baseUrl().endsWith("/")
                ? config.baseUrl() + normalizedRoute
                : config.baseUrl() + inventoryRoute;

            page.navigate(inventoryUrl);
            assertTrue(homePage.isLoaded());
        } else {
            loginPage.open(config.baseUrl());
            assertTrue(loginPage.isLoaded());
        }
    }

    //Runs after each test to ensure isolation.
    @AfterEach
    void tearDownTest(TestInfo testInfo) {
        saveScreenshot(testInfo);

        if (context != null) {
            context.close();
        }
    }

    // Takes a screenshot at the end of each test.
    // Files are stored in target/reports/screenshots and attached to Allure.
    private void saveScreenshot(TestInfo testInfo) {
        if (page == null || !config.screenshotOnTeardown()) {
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

    private void configurePage(Page targetPage) {
        targetPage.setDefaultTimeout(config.defaultTimeoutMs());
        targetPage.setDefaultNavigationTimeout(config.navigationTimeoutMs());
    }

    // Writes environment information to the Allure Environment tab.
    private void writeAllureEnvironment() {
        try {
            Path allureResultsDir = Path.of("target", "allure-results");
            Files.createDirectories(allureResultsDir);

            Properties environment = new Properties();
            environment.setProperty("Application URL", config.baseUrl());
            environment.setProperty("Browser", "Chromium");
            environment.setProperty("Headless", String.valueOf(config.headless()));
            environment.setProperty("Viewport", config.viewportWidth() + "x" + config.viewportHeight());
            environment.setProperty("Default Timeout (ms)", String.valueOf(config.defaultTimeoutMs()));
            environment.setProperty("Navigation Timeout (ms)", String.valueOf(config.navigationTimeoutMs()));
            environment.setProperty("Slow Mo (ms)", String.valueOf(config.slowMoMs()));
            environment.setProperty("Screenshot On Teardown", String.valueOf(config.screenshotOnTeardown()));
            environment.setProperty("Java Version", System.getProperty("java.version"));
            environment.setProperty("OS", System.getProperty("os.name") + " " + System.getProperty("os.version"));

            try (OutputStream output = Files.newOutputStream(allureResultsDir.resolve("environment.properties"))) {
                environment.store(output, "Allure Environment");
            } catch (IOException e) {
                throw new IllegalStateException("Failed to write environment.properties", e);
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

    // Runs once per test class.
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
