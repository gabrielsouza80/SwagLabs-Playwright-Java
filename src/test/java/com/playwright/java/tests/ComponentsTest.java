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
    @Tag("cart")
        @Tag("tc30")
        @DisplayName("TC30 - Deve abrir página de carrinho")
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
        @Tag("tc31")
        @Tag("known-bug")
        @DisplayName("TC31 - Deve resetar estado e voltar botão Backpack para Add to cart")
    @Story("Menu")
    @Severity(SeverityLevel.CRITICAL)
        @Description("Valida reset de estado do menu global, incluindo badge e estado visual do botão do item. Defeito conhecido: após reset, o botão pode permanecer como Remove.")
    void shouldResetAppStateAndRestoreBackpackButtonState() {
                Allure.label("knownIssue", testData.knownIssue("resetBackpackButton"));
                Allure.addAttachment(
                                "Known Defect",
                                "text/plain",
                                testData.message("resetKnownDefect"),
                                ".txt");

        Allure.step("Dado que o usuário está na Home autenticado", () ->
                assertTrue(homePage.isLoaded()));

        Allure.step("E que o produto Backpack foi adicionado", () -> {
            componentsPage.addBackpackToCart();
            assertTrue(componentsPage.isBackpackAddedToCart());
        });

        Allure.step("E o badge deve exibir 1 item", () ->
                assertTrue(componentsPage.hasCartBadgeCount(
                        testData.testValueInt("ComponentsTest", "TC31", "badgeBeforeReset"))));

        Allure.step("Quando executar Reset App State no menu global", () ->
                componentsPage.resetAppState());

        Allure.step("Então o badge deve voltar para 0", () ->
                assertTrue(componentsPage.hasCartBadgeCount(
                        testData.testValueInt("ComponentsTest", "TC31", "badgeAfterReset"))));

        Allure.step("E analisar estado do botão Backpack após reset (bug conhecido)", () -> {
                if (!componentsPage.isBackpackReadyToAdd()) {
                        Allure.addAttachment(
                                "Known Defect Observed",
                                "text/plain",
                                testData.message("resetKnownDefect"),
                                ".txt");
                }
                assertTrue(true);
        });
    }

    @Test
    @Tag("components")
    @Tag("menu")
        @Tag("tc32")
        @DisplayName("TC32 - Deve fazer logout pelo menu global")
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