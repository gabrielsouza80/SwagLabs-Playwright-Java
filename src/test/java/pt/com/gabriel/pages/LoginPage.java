package pt.com.gabriel.pages;

import com.microsoft.playwright.Page;
import io.qameta.allure.Step;

// Page Object da tela de login.
// Aqui ficam somente ações/validações da página de login.
public class LoginPage {
    // Referência da aba atual do navegador.
    private final Page page;

    // Seletores (preferência por data-test para maior estabilidade).
    private static final String USERNAME_INPUT = "[data-test='username']";
    private static final String PASSWORD_INPUT = "[data-test='password']";
    private static final String LOGIN_BUTTON = "[data-test='login-button']";
    private static final String LOGIN_CONTAINER = "[data-test='login-container']";

    // Recebe a página para executar ações nela.
    public LoginPage(Page page) {
        this.page = page;
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

    // Preenche usuário/senha e clica em login.
    @Step("Realizar login com usuário: {username}")
    public void login(String username, String password) {
        page.locator(USERNAME_INPUT).fill(username);
        page.locator(PASSWORD_INPUT).fill(password);
        page.locator(LOGIN_BUTTON).click();
    }
}
