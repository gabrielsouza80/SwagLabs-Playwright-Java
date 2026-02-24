package com.playwright.java.tests;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import com.playwright.java.base.BaseTest;
import com.playwright.java.config.TestData;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@Epic("Web Automation")
@Feature("Authentication")
@Owner("gabriel")
@TestMethodOrder(MethodOrderer.DisplayName.class)
public class LoginTest extends BaseTest {

        private final TestData testData = TestData.get();

    @Override
    protected boolean requiresAuthenticatedSession() {
        return false;
    }

    @Test
    @Tag("login")
    @Tag("smoke")
    @Tag("tc01")
    @DisplayName("TC01 - Deve realizar login com usuário padrão")
    @Story("Successful Login")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Valida que o usuário padrão autentica com sucesso e visualiza a home de inventário.")
    void shouldLoginWithStandardUser() {
        Allure.step("Dado que o usuário abriu a tela de login", () ->
                assertTrue(loginPage.isLoaded()));

        Allure.step("Quando realizar login com o usuário padrão", () ->
                loginPage.loginWithStandardUser());

        Allure.step("Então deve acessar a home de inventário", () ->
                assertTrue(homePage.isLoaded()));
    }

    @Test
    @Tag("login")
        @Tag("tc02")
        @DisplayName("TC02 - Deve exibir logo e campos obrigatórios na tela de login")
    @Story("Login Screen Layout")
    @Severity(SeverityLevel.NORMAL)
    @Description("Valida que a tela de login exibe logo, inputs de usuário/senha e botão de login.")
    void shouldDisplayLogoAndLoginFormElements() {
        Allure.step("Dado que o usuário abriu a página de login", () ->
                assertTrue(loginPage.isLoaded()));

        Allure.step("Quando validar logo, botão de login e painel de credenciais", () -> {
            assertTrue(loginPage.isLogoVisible());
                        assertTrue(loginPage.isUsernameInputVisible());
                        assertTrue(loginPage.isPasswordInputVisible());
            assertTrue(loginPage.isLoginButtonVisible());
            assertTrue(loginPage.isCredentialsPanelVisible());
        });

        Allure.step("Então a estrutura da tela de login deve estar visível", () ->
                assertTrue(loginPage.isLoaded()));
    }

    @Test
    @Tag("login")
        @Tag("tc03")
        @DisplayName("TC03 - Deve exibir usuários aceitos e senha padrão")
    @Story("Login Screen Content")
    @Severity(SeverityLevel.NORMAL)
    @Description("Valida o conteúdo textual de usuários aceitos e dica de senha da tela de login.")
    void shouldDisplayAcceptedUsersAndPasswordHint() {
        Allure.step("Dado que o usuário está na tela de login", () ->
                assertTrue(loginPage.isLoaded()));

        Allure.step("Quando consultar a lista de usuários aceitos", () ->
                assertTrue(loginPage.hasAllAcceptedUsers()));

        Allure.step("Então deve exibir a senha padrão secret_sauce", () ->
                assertTrue(loginPage.hasDefaultPasswordHint()));
    }

    @Test
    @Tag("login")
    @Tag("negative")
        @Tag("tc04")
        @DisplayName("TC04 - Deve exibir erro ao tentar login sem usuário")
    @Story("Login Validations")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Valida mensagem de erro ao tentar autenticar sem informar usuário.")
    void shouldShowErrorWhenUsernameIsEmpty() {
        Allure.step("Dado que o usuário está na tela de login", () ->
                assertTrue(loginPage.isLoaded()));

        Allure.step("Quando tentar login sem informar usuário", () ->
                loginPage.tryLoginWithoutUsername());

        Allure.step("Então deve exibir erro de usuário obrigatório", () ->
                assertTrue(loginPage.hasErrorMessageContaining(testData.error("usernameRequired"))));

                Allure.step("E não deve acessar a home de inventário", () -> {
                        assertTrue(loginPage.isLoaded());
                        assertFalse(loginPage.isOnInventoryPage());
                });
    }

    @Test
    @Tag("login")
    @Tag("negative")
        @Tag("tc05")
        @DisplayName("TC05 - Deve exibir erro ao tentar login sem senha")
    @Story("Login Validations")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Valida mensagem de erro ao tentar autenticar sem informar senha.")
    void shouldShowErrorWhenPasswordIsEmpty() {
        Allure.step("Dado que o usuário está na tela de login", () ->
                assertTrue(loginPage.isLoaded()));

        Allure.step("Quando tentar login sem informar senha", () ->
                loginPage.tryLoginWithoutPassword());

        Allure.step("Então deve exibir erro de senha obrigatória", () ->
                assertTrue(loginPage.hasErrorMessageContaining(testData.error("passwordRequired"))));

                Allure.step("E não deve acessar a home de inventário", () -> {
                        assertTrue(loginPage.isLoaded());
                        assertFalse(loginPage.isOnInventoryPage());
                });
    }

    @Test
    @Tag("login")
    @Tag("negative")
        @Tag("tc06")
        @DisplayName("TC06 - Deve exibir erro para usuário bloqueado")
    @Story("Login Validations")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Valida que usuário bloqueado não consegue autenticar e recebe mensagem adequada.")
    void shouldShowErrorForLockedOutUser() {
        Allure.step("Dado que o usuário está na tela de login", () ->
                assertTrue(loginPage.isLoaded()));

        Allure.step("Quando tentar login com usuário bloqueado", () ->
                loginPage.loginWithLockedOutUser());

        Allure.step("Então deve exibir erro de usuário bloqueado", () ->
                assertTrue(loginPage.hasErrorMessageContaining(testData.error("lockedOut"))));

                Allure.step("E não deve acessar a home de inventário", () -> {
                        assertTrue(loginPage.isLoaded());
                        assertFalse(loginPage.isOnInventoryPage());
                });
    }

    @Test
    @Tag("login")
        @Tag("tc07")
        @DisplayName("TC07 - Deve logar com performance_glitch_user e acessar inventário")
    @Story("Alternative Valid Users")
    @Severity(SeverityLevel.NORMAL)
    @Description("Valida login com usuário alternativo aceito e acesso à home de inventário.")
    void shouldLoginWithPerformanceGlitchUser() {
        Allure.step("Dado que o usuário está na tela de login", () ->
                assertTrue(loginPage.isLoaded()));

        Allure.step("Quando realizar login com performance_glitch_user", () ->
                loginPage.loginWithPerformanceGlitchUser());

        Allure.step("Então deve acessar a home de inventário", () ->
                assertTrue(homePage.isLoaded()));
    }

    @Test
    @Tag("login")
    @Tag("tc25")
    @DisplayName("TC25 - Deve logar com problem_user e acessar inventário")
    @Story("Alternative Valid Users")
    @Severity(SeverityLevel.NORMAL)
    @Description("Valida login com problem_user e acesso à home de inventário.")
    void shouldLoginWithProblemUser() {
        Allure.step("Dado que o usuário está na tela de login", () ->
                assertTrue(loginPage.isLoaded()));

        Allure.step("Quando realizar login com problem_user", () ->
                loginPage.loginWithProblemUser());

        Allure.step("Então deve acessar a home de inventário", () ->
                assertTrue(homePage.isLoaded()));
    }

    @Test
    @Tag("login")
    @Tag("tc26")
    @DisplayName("TC26 - Deve logar com error_user e acessar inventário")
    @Story("Alternative Valid Users")
    @Severity(SeverityLevel.NORMAL)
    @Description("Valida login com error_user e acesso à home de inventário.")
    void shouldLoginWithErrorUser() {
        Allure.step("Dado que o usuário está na tela de login", () ->
                assertTrue(loginPage.isLoaded()));

        Allure.step("Quando realizar login com error_user", () ->
                loginPage.loginWithErrorUser());

        Allure.step("Então deve acessar a home de inventário", () ->
                assertTrue(homePage.isLoaded()));
    }

    @Test
    @Tag("login")
    @Tag("tc27")
    @DisplayName("TC27 - Deve logar com visual_user e acessar inventário")
    @Story("Alternative Valid Users")
    @Severity(SeverityLevel.NORMAL)
    @Description("Valida login com visual_user e acesso à home de inventário.")
    void shouldLoginWithVisualUser() {
        Allure.step("Dado que o usuário está na tela de login", () ->
                assertTrue(loginPage.isLoaded()));

        Allure.step("Quando realizar login com visual_user", () ->
                loginPage.loginWithVisualUser());

        Allure.step("Então deve acessar a home de inventário", () ->
                assertTrue(homePage.isLoaded()));
    }
}