package com.playwright.java.tests;

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
@Owner("gabriel")
@TestMethodOrder(MethodOrderer.DisplayName.class)
public class ComponentsTest extends BaseTest {

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
    @DisplayName("TC19 - Deve resetar estado e voltar botão Backpack para Add to cart")
    @Story("Menu")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Valida reset de estado do menu global, incluindo badge e estado visual do botão do item.")
    void shouldResetAppStateAndRestoreBackpackButtonState() {
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
}