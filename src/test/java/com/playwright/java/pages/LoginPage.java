package com.playwright.java.pages;

import com.microsoft.playwright.Page;
import com.playwright.java.config.TestData;
import io.qameta.allure.Step;

// Page Object da tela de login.
// Aqui ficam somente ações/validações da página de login.
public class LoginPage {
    // Referência da aba atual do navegador.
    private final Page page;
    private final TestData testData;

    // Seletores (preferência por data-test para maior estabilidade).
    private static final String USERNAME_INPUT = "[data-test='username']";
    private static final String PASSWORD_INPUT = "[data-test='password']";
    private static final String LOGIN_BUTTON = "[data-test='login-button']";
    private static final String LOGIN_CONTAINER = "[data-test='login-container']";
    private static final String LOGIN_LOGO = ".login_logo";
    private static final String LOGIN_CREDENTIALS_CONTAINER = "[data-test='login-credentials-container']";
    private static final String LOGIN_CREDENTIALS = "[data-test='login-credentials']";
    private static final String LOGIN_PASSWORD_HINT = "[data-test='login-password']";
    private static final String ERROR_MESSAGE = "[data-test='error']";

    // Recebe a página para executar ações nela.
    public LoginPage(Page page) {
        this.page = page;
        this.testData = TestData.get();
    }

    // Navega para a URL base da aplicação.
    @Step("Abrir URL da aplicação: {baseUrl}")
    public void open(String baseUrl) {
        page.navigate(baseUrl);
    }

    // Verifica se a tela de login está visível.
    @Step("Validar se tela de login está carregada")
    public boolean isLoaded() {
        return page.locator(LOGIN_CONTAINER).isVisible();
    }

    @Step("Validar botão de login visível")
    public boolean isLoginButtonVisible() {
        return page.locator(LOGIN_BUTTON).isVisible();
    }

    @Step("Validar campo de usuário visível")
    public boolean isUsernameInputVisible() {
        return page.locator(USERNAME_INPUT).isVisible();
    }

    @Step("Validar campo de senha visível")
    public boolean isPasswordInputVisible() {
        return page.locator(PASSWORD_INPUT).isVisible();
    }

    // Preenche usuário/senha e clica em login.
    @Step("Realizar login com usuário: {username}")
    public void login(String username, String password) {
        page.locator(USERNAME_INPUT).fill(username);
        page.locator(PASSWORD_INPUT).fill(password);
        page.locator(LOGIN_BUTTON).click();
    }

    @Step("Abrir aplicação e realizar login com usuário: {username}")
    public void openAndLogin(String baseUrl, String username, String password) {
        open(baseUrl);
        login(username, password);
    }

    @Step("Preencher usuário: {username}")
    public void fillUsername(String username) {
        page.locator(USERNAME_INPUT).fill(username);
    }

    @Step("Preencher senha")
    public void fillPassword(String password) {
        page.locator(PASSWORD_INPUT).fill(password);
    }

    @Step("Clicar no botão Login")
    public void clickLogin() {
        page.locator(LOGIN_BUTTON).click();
    }

    @Step("Validar logo Swag Labs visível")
    public boolean isLogoVisible() {
        return page.locator(LOGIN_LOGO).isVisible();
    }

    @Step("Validar painel de credenciais visível")
    public boolean isCredentialsPanelVisible() {
        return page.locator(LOGIN_CREDENTIALS_CONTAINER).isVisible();
    }

    @Step("Obter texto de usuários aceitos")
    public String getAcceptedUsernamesText() {
        return page.locator(LOGIN_CREDENTIALS).innerText();
    }

    @Step("Validar lista completa de usuários aceitos")
    public boolean hasAllAcceptedUsers() {
        String acceptedUsers = getAcceptedUsernamesText();
        return testData.allUsers().stream().allMatch(acceptedUsers::contains);
    }

    @Step("Obter texto de dica de senha")
    public String getPasswordHintText() {
        return page.locator(LOGIN_PASSWORD_HINT).innerText();
    }

    @Step("Validar senha padrão exibida na tela")
    public boolean hasDefaultPasswordHint() {
        return getPasswordHintText().contains(testData.password());
    }

    @Step("Validar mensagem de erro visível")
    public boolean isErrorVisible() {
        return page.locator(ERROR_MESSAGE).isVisible();
    }

    @Step("Obter mensagem de erro")
    public String getErrorMessage() {
        return page.locator(ERROR_MESSAGE).innerText().trim();
    }

    @Step("Validar mensagem de erro contém: {expectedText}")
    public boolean hasErrorMessageContaining(String expectedText) {
        return isErrorVisible() && getErrorMessage().contains(expectedText);
    }

    @Step("Validar que usuário não foi redirecionado para inventário")
    public boolean isOnInventoryPage() {
        return page.url().contains(testData.route("inventory"));
    }

    @Step("Tentar login sem usuário")
    public void tryLoginWithoutUsername(String password) {
        fillPassword(password);
        clickLogin();
    }

    @Step("Tentar login sem usuário (senha do JSON)")
    public void tryLoginWithoutUsername() {
        tryLoginWithoutUsername(testData.password());
    }

    @Step("Tentar login sem senha")
    public void tryLoginWithoutPassword(String username) {
        fillUsername(username);
        clickLogin();
    }

    @Step("Tentar login sem senha (usuário padrão do JSON)")
    public void tryLoginWithoutPassword() {
        tryLoginWithoutPassword(testData.user("standard"));
    }

    @Step("Tentar login com usuário bloqueado")
    public void loginWithLockedOutUser(String password) {
        login(testData.user("lockedOut"), password);
    }

    @Step("Tentar login com usuário bloqueado (dados do JSON)")
    public void loginWithLockedOutUser() {
        loginWithLockedOutUser(testData.password());
    }

    @Step("Login com usuário performance_glitch_user")
    public void loginWithPerformanceGlitchUser(String password) {
        login(testData.user("performanceGlitch"), password);
    }

    @Step("Login com usuário performance_glitch_user (dados do JSON)")
    public void loginWithPerformanceGlitchUser() {
        loginWithPerformanceGlitchUser(testData.password());
    }

    @Step("Login com performance_glitch_user medindo duração")
    public long loginWithPerformanceGlitchUserAndMeasureDurationMs() {
        long start = System.nanoTime();
        loginWithPerformanceGlitchUser();
        page.waitForURL("**" + testData.route("inventory"));
        long end = System.nanoTime();
        return (end - start) / 1_000_000;
    }

    @Step("Login com usuário padrão (dados do JSON)")
    public void loginWithStandardUser() {
        login(testData.user("standard"), testData.password());
    }

    @Step("Login com usuário problem_user (dados do JSON)")
    public void loginWithProblemUser() {
        login(testData.user("problem"), testData.password());
    }

    @Step("Login com usuário error_user (dados do JSON)")
    public void loginWithErrorUser() {
        login(testData.user("error"), testData.password());
    }

    @Step("Login com usuário visual_user (dados do JSON)")
    public void loginWithVisualUser() {
        login(testData.user("visual"), testData.password());
    }
}
