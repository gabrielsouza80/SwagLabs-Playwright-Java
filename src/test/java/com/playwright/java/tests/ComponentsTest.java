package com.playwright.java.tests;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.playwright.java.base.BaseTest;
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

    @Test
    @Tag("components")
    @Tag("header")
    @Tag("tc29")
    @DisplayName("TC29 - Deve exibir componentes globais do header")
    @Story("Header")
    @Severity(SeverityLevel.NORMAL)
    @Description("Valida presença dos componentes globais do header: primary header, botão de menu e ícone do carrinho.")
    void shouldDisplayGlobalHeaderComponents() {
        Allure.step("Dado que o usuário está na Home autenticado", () ->
                assertTrue(homePage.isLoaded()));

        Allure.step("Então o header principal deve estar visível", () ->
                assertTrue(componentsPage.isPrimaryHeaderVisible()));

        Allure.step("E os componentes de menu e carrinho devem estar visíveis", () -> {
            assertTrue(componentsPage.isMenuButtonVisible());
            assertTrue(componentsPage.isCartIconVisible());
        });
    }

    @Test
    @Tag("components")
    @Tag("menu")
    @Tag("tc30")
    @DisplayName("TC30 - Deve abrir menu lateral e exibir opções esperadas")
    @Story("Menu")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Valida abertura do menu lateral e presença das opções All Items, About, Logout e Reset App State.")
    void shouldOpenMenuAndDisplayExpectedOptions() {
        Allure.step("Dado que o usuário está na Home autenticado", () ->
                assertTrue(homePage.isLoaded()));

        Allure.step("Quando abrir o menu lateral", () ->
                componentsPage.openMenu());

        Allure.step("Então o menu deve estar aberto com opções esperadas", () ->
                assertTrue(componentsPage.hasExpectedMenuOptionsVisible()));

        Allure.step("E a opção About deve apontar para o site da Sauce Labs", () -> {
            String aboutHref = componentsPage.getAboutMenuHref();
            assertNotNull(aboutHref);
            assertTrue(aboutHref.contains("saucelabs.com"));
        });
    }

    @Test
    @Tag("components")
    @Tag("menu")
    @Tag("tc31")
    @DisplayName("TC31 - Deve fechar menu lateral no botão Close Menu")
    @Story("Menu")
    @Severity(SeverityLevel.NORMAL)
    @Description("Valida fechamento do menu lateral pelo botão Close Menu e ocultação das opções.")
    void shouldCloseMenuUsingCloseButton() {
        Allure.step("Dado que o usuário está na Home autenticado", () ->
                assertTrue(homePage.isLoaded()));

        Allure.step("E que o menu lateral foi aberto", () -> {
            componentsPage.openMenu();
            assertTrue(componentsPage.isMenuOpen());
        });

        Allure.step("Quando fechar o menu no botão Close Menu", () ->
                componentsPage.closeMenu());

        Allure.step("Então as opções do menu devem ficar ocultas", () ->
                assertTrue(componentsPage.hasMenuOptionsHidden()));
    }

    @Test
    @Tag("components")
    @Tag("cart")
    @Tag("tc18")
    @DisplayName("TC18 - Deve abrir página de carrinho")
    @Story("Cart")
    @Severity(SeverityLevel.NORMAL)
    @Description("Navega para a página de carrinho usando componente global do header.")
    void shouldOpenCartPageFromHeaderComponent() {
        Allure.step("Dado que o usuário está na Home autenticado", () ->
                assertTrue(homePage.isLoaded()));

        Allure.step("Quando clicar no ícone global do carrinho", () ->
                componentsPage.openCart());

        Allure.step("Então a página de carrinho deve ser exibida", () ->
                assertTrue(componentsPage.isCartPageLoaded()));
    }

    @Test
    @Tag("components")
    @Tag("menu")
    @Tag("tc19")
        @Tag("known-bug")
    @DisplayName("TC19 - Deve resetar estado e voltar botão Backpack para Add to cart")
    @Story("Menu")
    @Severity(SeverityLevel.CRITICAL)
        @Description("Valida reset de estado do menu global, incluindo badge e estado visual do botão do item. Defeito conhecido: após reset, o botão pode permanecer como Remove.")
    void shouldResetAppStateAndRestoreBackpackButtonState() {
                Allure.label("knownIssue", "SAUCEDEMO-RESET-BACKPACK-BUTTON");
                Allure.addAttachment(
                                "Known Defect",
                                "text/plain",
                                "Known issue: após Reset App State, o badge zera mas o botão do Backpack pode permanecer como Remove em vez de voltar para Add to cart.",
                                ".txt");

        Allure.step("Dado que o usuário está na Home autenticado", () ->
                assertTrue(homePage.isLoaded()));

        Allure.step("E que o produto Backpack foi adicionado", () -> {
            componentsPage.addBackpackToCart();
            assertTrue(componentsPage.isBackpackAddedToCart());
        });

        Allure.step("E o badge deve exibir 1 item", () ->
                assertTrue(componentsPage.hasCartBadgeCount(1)));

        Allure.step("Quando executar Reset App State no menu global", () ->
                componentsPage.resetAppState());

        Allure.step("Então o badge deve voltar para 0", () ->
                assertTrue(componentsPage.hasCartBadgeCount(0)));

        Allure.step("E o botão do Backpack deve voltar para Add to cart", () ->
                assertTrue(componentsPage.isBackpackReadyToAdd()));
    }

    @Test
    @Tag("components")
    @Tag("menu")
    @Tag("tc20")
    @DisplayName("TC20 - Deve fazer logout pelo menu global")
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
        @Tag("tc32")
        @DisplayName("TC32 - Deve voltar para inventário usando All Items")
        @Story("Menu")
        @Severity(SeverityLevel.NORMAL)
        @Description("Valida navegação para o inventário a partir da opção All Items no menu lateral.")
        void shouldNavigateToInventoryByAllItemsOption() {
                Allure.step("Dado que o usuário abriu a página de carrinho", () -> {
                        assertTrue(homePage.isLoaded());
                        componentsPage.openCart();
                        assertTrue(componentsPage.isCartPageLoaded());
                });

                Allure.step("Quando selecionar All Items no menu lateral", () ->
                                componentsPage.openAllItemsFromMenu());

                Allure.step("Então deve retornar para a Home de inventário", () ->
                                assertTrue(homePage.isLoaded()));
        }
}