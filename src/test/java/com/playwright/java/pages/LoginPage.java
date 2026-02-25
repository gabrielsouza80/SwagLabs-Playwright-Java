package com.playwright.java.pages;

import com.microsoft.playwright.Page;
import com.playwright.java.config.TestData;
import io.qameta.allure.Step;

// Page Object for the login screen.
// This class contains only login-page actions and validations.
public class LoginPage {
    // Reference to the current browser tab.
    private final Page page;
    private final TestData testData;

    // Selectors (prefer data-test for better stability).
    private static final String USERNAME_INPUT = "[data-test='username']";
    private static final String PASSWORD_INPUT = "[data-test='password']";
    private static final String LOGIN_BUTTON = "[data-test='login-button']";
    private static final String LOGIN_CONTAINER = "[data-test='login-container']";
    private static final String LOGIN_LOGO = ".login_logo";
    private static final String LOGIN_CREDENTIALS_CONTAINER = "[data-test='login-credentials-container']";
    private static final String LOGIN_CREDENTIALS = "[data-test='login-credentials']";
    private static final String LOGIN_PASSWORD_HINT = "[data-test='login-password']";
    private static final String ERROR_MESSAGE = "[data-test='error']";

    // Receives the page instance used to perform interactions.
    public LoginPage(Page page) {
        this.page = page;
        this.testData = TestData.get();
    }

    // Navigates to the application base URL.
    @Step("Open application URL: {baseUrl}")
    public void open(String baseUrl) {
        page.navigate(baseUrl);
    }

    // Checks whether the login screen is visible.
    @Step("Validate that the login screen is loaded")
    public boolean isLoaded() {
        return page.locator(LOGIN_CONTAINER).isVisible();
    }

    @Step("Validate login button is visible")
    public boolean isLoginButtonVisible() {
        return page.locator(LOGIN_BUTTON).isVisible();
    }

    @Step("Validate username field is visible")
    public boolean isUsernameInputVisible() {
        return page.locator(USERNAME_INPUT).isVisible();
    }

    @Step("Validate password field is visible")
    public boolean isPasswordInputVisible() {
        return page.locator(PASSWORD_INPUT).isVisible();
    }

    // Fills in username/password and clicks login.
    @Step("Log in with user: {username}")
    public void login(String username, String password) {
        page.locator(USERNAME_INPUT).fill(username);
        page.locator(PASSWORD_INPUT).fill(password);
        page.locator(LOGIN_BUTTON).click();
    }

    @Step("Open application and log in with user: {username}")
    public void openAndLogin(String baseUrl, String username, String password) {
        open(baseUrl);
        login(username, password);
    }

    @Step("Fill username: {username}")
    public void fillUsername(String username) {
        page.locator(USERNAME_INPUT).fill(username);
    }

    @Step("Fill password")
    public void fillPassword(String password) {
        page.locator(PASSWORD_INPUT).fill(password);
    }

    @Step("Click the Login button")
    public void clickLogin() {
        page.locator(LOGIN_BUTTON).click();
    }

    @Step("Validate Swag Labs logo is visible")
    public boolean isLogoVisible() {
        return page.locator(LOGIN_LOGO).isVisible();
    }

    @Step("Validate credentials panel is visible")
    public boolean isCredentialsPanelVisible() {
        return page.locator(LOGIN_CREDENTIALS_CONTAINER).isVisible();
    }

    @Step("Get accepted usernames text")
    public String getAcceptedUsernamesText() {
        return page.locator(LOGIN_CREDENTIALS).innerText();
    }

    @Step("Validate all accepted usernames are displayed")
    public boolean hasAllAcceptedUsers() {
        String acceptedUsers = getAcceptedUsernamesText();
        return testData.allUsers().stream().allMatch(acceptedUsers::contains);
    }

    @Step("Get password hint text")
    public String getPasswordHintText() {
        return page.locator(LOGIN_PASSWORD_HINT).innerText();
    }

    @Step("Validate default password is shown on screen")
    public boolean hasDefaultPasswordHint() {
        return getPasswordHintText().contains(testData.password());
    }

    @Step("Validate error message is visible")
    public boolean isErrorVisible() {
        return page.locator(ERROR_MESSAGE).isVisible();
    }

    @Step("Get error message")
    public String getErrorMessage() {
        return page.locator(ERROR_MESSAGE).innerText().trim();
    }

    @Step("Validate error message contains: {expectedText}")
    public boolean hasErrorMessageContaining(String expectedText) {
        return isErrorVisible() && getErrorMessage().contains(expectedText);
    }

    @Step("Validate user was not redirected to inventory")
    public boolean isOnInventoryPage() {
        return page.url().contains(testData.route("inventory"));
    }

    @Step("Try logging in without username")
    public void tryLoginWithoutUsername(String password) {
        fillPassword(password);
        clickLogin();
    }

    @Step("Try logging in without username (password from JSON)")
    public void tryLoginWithoutUsername() {
        tryLoginWithoutUsername(testData.password());
    }

    @Step("Try logging in without password")
    public void tryLoginWithoutPassword(String username) {
        fillUsername(username);
        clickLogin();
    }

    @Step("Try logging in without password (default user from JSON)")
    public void tryLoginWithoutPassword() {
        tryLoginWithoutPassword(testData.user("standard"));
    }

    @Step("Try logging in with locked user")
    public void loginWithLockedOutUser(String password) {
        login(testData.user("lockedOut"), password);
    }

    @Step("Try logging in with locked user (data from JSON)")
    public void loginWithLockedOutUser() {
        loginWithLockedOutUser(testData.password());
    }

    @Step("Log in with performance_glitch_user")
    public void loginWithPerformanceGlitchUser(String password) {
        login(testData.user("performanceGlitch"), password);
    }

    @Step("Log in with performance_glitch_user (data from JSON)")
    public void loginWithPerformanceGlitchUser() {
        loginWithPerformanceGlitchUser(testData.password());
    }

    @Step("Log in with performance_glitch_user and measure duration")
    public long loginWithPerformanceGlitchUserAndMeasureDurationMs() {
        long start = System.nanoTime();
        loginWithPerformanceGlitchUser();
        page.waitForURL("**" + testData.route("inventory"));
        long end = System.nanoTime();
        return (end - start) / 1_000_000;
    }

    @Step("Log in with default user (data from JSON)")
    public void loginWithStandardUser() {
        login(testData.user("standard"), testData.password());
    }

    @Step("Log in with problem_user (data from JSON)")
    public void loginWithProblemUser() {
        login(testData.user("problem"), testData.password());
    }

    @Step("Log in with error_user (data from JSON)")
    public void loginWithErrorUser() {
        login(testData.user("error"), testData.password());
    }

    @Step("Log in with visual_user (data from JSON)")
    public void loginWithVisualUser() {
        login(testData.user("visual"), testData.password());
    }
}
