package com.playwright.java.tests;

import static org.junit.jupiter.api.Assertions.assertTrue;

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
@Feature("Shared Components")
@Owner("Gabriel Souza")
@TestMethodOrder(MethodOrderer.DisplayName.class)
public class ComponentsTest extends BaseTest {
        private final TestData testData = TestData.get();

    @Test
    @Tag("components")
    @Tag("header")
        @Tag("tc30")
        @DisplayName("TC30 - Deve exibir componentes globais do header")
    @Story("Header")
    @Severity(SeverityLevel.NORMAL)
    @Description("Valida presença dos componentes globais do header: primary header, botão de menu e ícone do carrinho.")
    void shouldDisplayGlobalHeaderComponents() {
        Allure.step("Dado que o usuário está na Home autenticado", () ->
                assertTrue(homePage.isLoaded()));

        Allure.step("Então os componentes globais do header devem estar visíveis", () ->
                assertTrue(componentsPage.hasVisibleHeaderComponents()));
    }

    @Test
    @Tag("components")
    @Tag("menu")
        @Tag("tc31")
        @DisplayName("TC31 - Deve abrir menu lateral e exibir opções esperadas")
    @Story("Menu")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Valida abertura do menu lateral e presença das opções All Items, About, Logout e Reset App State.")
    void shouldOpenMenuAndDisplayExpectedOptions() {
        Allure.step("Dado que o usuário está na Home autenticado", () ->
                assertTrue(homePage.isLoaded()));

        Allure.step("Então o menu deve estar aberto com opções esperadas", () ->
                assertTrue(componentsPage.openMenuAndValidateExpectedOptions()));

        Allure.step("E a opção About deve apontar para o site da Sauce Labs", () -> {
            assertTrue(componentsPage.hasValidAboutMenuLink());
        });
    }

    @Test
    @Tag("components")
    @Tag("menu")
        @Tag("tc32")
        @DisplayName("TC32 - Deve fechar menu lateral no botão Close Menu")
    @Story("Menu")
    @Severity(SeverityLevel.NORMAL)
    @Description("Valida fechamento do menu lateral pelo botão Close Menu e ocultação das opções.")
    void shouldCloseMenuUsingCloseButton() {
        Allure.step("Dado que o usuário está na Home autenticado", () ->
                assertTrue(homePage.isLoaded()));

        Allure.step("E que o menu lateral foi aberto", () -> {
                        assertTrue(componentsPage.openMenuAndValidateOpen());
        });

        Allure.step("Quando fechar o menu no botão Close Menu", () ->
                componentsPage.closeMenu());

        Allure.step("Então as opções do menu devem ficar ocultas", () ->
                assertTrue(componentsPage.hasMenuOptionsHidden()));
    }

    @Test
    @Tag("components")
    @Tag("cart")
        @Tag("tc33")
        @DisplayName("TC33 - Deve abrir página de carrinho")
    @Story("Cart")
    @Severity(SeverityLevel.NORMAL)
    @Description("Navega para a página de carrinho usando componente global do header.")
    void shouldOpenCartPageFromHeaderComponent() {
        Allure.step("Dado que o usuário está na Home autenticado", () ->
                assertTrue(homePage.isLoaded()));

        Allure.step("Então a página de carrinho deve ser exibida", () ->
                assertTrue(componentsPage.openCartAndValidateLoaded()));
    }

    @Test
    @Tag("components")
    @Tag("menu")
        @Tag("tc34")
        @Tag("known-bug")
        @DisplayName("TC34 - Deve resetar estado e voltar botão Backpack para Add to cart")
    @Story("Menu")
    @Severity(SeverityLevel.CRITICAL)
        @Description("Valida reset de estado do menu global, incluindo badge e estado visual do botão do item. Defeito conhecido: após reset, o botão pode permanecer como Remove.")
    void shouldResetAppStateAndRestoreBackpackButtonState() {
                Allure.label("knownIssue", testData.expected("knownIssueResetCode"));
                Allure.addAttachment(
                                "Known Defect",
                                "text/plain",
                                testData.expected("knownIssueResetMessage"),
                                ".txt");

        Allure.step("Dado que o usuário está na Home autenticado", () ->
                assertTrue(homePage.isLoaded()));

        Allure.step("E que o produto Backpack foi adicionado", () -> {
                        assertTrue(componentsPage.addBackpackAndValidateAdded());
        });

        Allure.step("E o badge deve exibir 1 item", () ->
                assertTrue(componentsPage.hasCartBadgeCount(testData.expectedInt("cartBadgeOne"))));

        Allure.step("Quando executar Reset App State no menu global", () ->
                componentsPage.resetAppState());

        Allure.step("Então o badge deve voltar para 0", () ->
                assertTrue(componentsPage.hasCartBadgeCount(testData.expectedInt("cartBadgeZero"))));

        Allure.step("E analisar estado do botão Backpack após reset (bug conhecido)", () ->
                assertTrue(componentsPage.validateBackpackStateAfterResetKnownBug()));
    }

    @Test
    @Tag("components")
    @Tag("menu")
        @Tag("tc35")
        @DisplayName("TC35 - Deve fazer logout pelo menu global")
    @Story("Menu")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Realiza logout via menu global e valida retorno para tela de login.")
    void shouldLogoutFromGlobalMenuComponent() {
        Allure.step("Dado que o usuário está na Home autenticado", () ->
                assertTrue(homePage.isLoaded()));

        Allure.step("Quando realizar logout pelo menu global", () ->
                componentsPage.logout());

        Allure.step("Então deve retornar para a tela de login", () ->
                assertTrue(loginPage.isLoaded()));
    }

        @Test
        @Tag("components")
        @Tag("menu")
        @Tag("tc36")
        @DisplayName("TC36 - Deve voltar para inventário usando All Items")
        @Story("Menu")
        @Severity(SeverityLevel.NORMAL)
        @Description("Valida navegação para o inventário a partir da opção All Items no menu lateral.")
        void shouldNavigateToInventoryByAllItemsOption() {
                Allure.step("Dado que o usuário abriu a página de carrinho", () -> {
                        assertTrue(homePage.isLoaded());
                        assertTrue(componentsPage.openCartAndValidateLoaded());
                });

                Allure.step("Quando selecionar All Items no menu lateral", () ->
                                componentsPage.openAllItemsFromMenu());

                Allure.step("Então deve retornar para a Home de inventário", () ->
                                assertTrue(homePage.isLoaded()));
        }
}