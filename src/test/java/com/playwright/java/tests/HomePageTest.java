package com.playwright.java.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import com.playwright.java.base.BaseTest;

// Suíte de testes da HomePage com foco em funcionalidades principais.
@Epic("Web Automation")
@Feature("Homepage")
@Owner("gabriel")
public class HomePageTest extends BaseTest {

    private static final String PRODUCTS_TITLE = "Products";
    private static final String DEFAULT_SORT = "Name (A to Z)";
    private static final int EXPECTED_INVENTORY_ITEMS = 6;

    // Valida carregamento da home e elementos básicos.
    @Test
    @Tag("home")
    @Tag("smoke")
    @Tag("tc02")
    @DisplayName("TC02 - Deve exibir elementos principais da home")
    @Story("Home Load")
    @Owner("gabriel")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Valida carregamento da home após login e presença dos principais componentes da tela.")
    void shouldDisplayHomePageMainElements() {
        assertTrue(homePage.isLoaded());
        assertTrue(homePage.hasMainHomeElements());
    }

    // Valida título principal da página
    @Test
    @Tag("home")
    @Tag("tc03")
    @DisplayName("TC03 - Deve exibir título Products")
    @Story("Home Header")
    @Severity(SeverityLevel.NORMAL)
    @Description("Valida o título da home como Products.")
    void shouldDisplayProductsTitle() {
        assertEquals(PRODUCTS_TITLE, homePage.getPageTitle());
    }

    // Valida opção de ordenação padrão.
    @Test
    @Tag("home")
    @Tag("tc04")
    @DisplayName("TC04 - Deve exibir ordenação padrão Name (A to Z)")
    @Story("Sorting")
    @Severity(SeverityLevel.NORMAL)
    @Description("Valida a opção padrão de ordenação na primeira carga da home.")
    void shouldDisplayDefaultSortingOption() {
        assertEquals(DEFAULT_SORT, homePage.getActiveSortOption());
    }

    // Valida quantidade de produtos esperada no SauceDemo.
    @Test
    @Tag("home")
    @Tag("tc05")
    @DisplayName("TC05 - Deve exibir 6 itens na listagem")
    @Story("Catalog")
    @Severity(SeverityLevel.NORMAL)
    @Description("Valida a quantidade esperada de produtos no inventário.")
    void shouldDisplaySixInventoryItems() {
        assertEquals(EXPECTED_INVENTORY_ITEMS, homePage.getInventoryItemCount());
    }

    // Ordenação por nome crescente (A-Z).
    @Test
    @Tag("home")
    @Tag("sorting")
    @Tag("tc06")
    @DisplayName("TC06 - Deve ordenar por nome crescente")
    @Story("Sorting")
    @Severity(SeverityLevel.NORMAL)
    @Description("Valida ordenação por nome crescente (A to Z).")
    void shouldSortProductsByNameAscending() {
        homePage.sortBy("az");
        assertTrue(homePage.areProductNamesSortedAscending());
    }

    // Ordenação por nome decrescente (Z-A).
    @Test
    @Tag("home")
    @Tag("sorting")
    @Tag("tc07")
    @DisplayName("TC07 - Deve ordenar por nome decrescente")
    @Story("Sorting")
    @Severity(SeverityLevel.NORMAL)
    @Description("Valida ordenação por nome decrescente (Z to A).")
    void shouldSortProductsByNameDescending() {
        homePage.sortBy("za");
        assertTrue(homePage.areProductNamesSortedDescending());
    }

    // Ordenação por preço crescente.
    @Test
    @Tag("home")
    @Tag("sorting")
    @Tag("tc08")
    @DisplayName("TC08 - Deve ordenar por preço crescente")
    @Story("Sorting")
    @Severity(SeverityLevel.NORMAL)
    @Description("Valida ordenação por preço crescente (low to high).")
    void shouldSortProductsByPriceAscending() {
        homePage.sortBy("lohi");
        assertTrue(homePage.arePricesSortedAscending());
    }

    // Ordenação por preço decrescente.
    @Test
    @Tag("home")
    @Tag("sorting")
    @Tag("tc09")
    @DisplayName("TC09 - Deve ordenar por preço decrescente")
    @Story("Sorting")
    @Severity(SeverityLevel.NORMAL)
    @Description("Valida ordenação por preço decrescente (high to low).")
    void shouldSortProductsByPriceDescending() {
        homePage.sortBy("hilo");
        assertTrue(homePage.arePricesSortedDescending());
    }

    // Adiciona item ao carrinho e valida estado visual/contador.
    @Test
    @Tag("home")
    @Tag("cart")
    @Tag("tc10")
    @DisplayName("TC10 - Deve adicionar mochila ao carrinho")
    @Story("Cart")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Adiciona o item Backpack ao carrinho e valida estado visual e badge.")
    void shouldAddBackpackToCart() {
        homePage.addBackpackToCart();

        assertTrue(homePage.isBackpackAddedToCart());
        assertCartBadgeCount(1);
    }

    // Remove item do carrinho e valida contador zerado.
    @Test
    @Tag("home")
    @Tag("cart")
    @Tag("tc11")
    @DisplayName("TC11 - Deve remover mochila do carrinho")
    @Story("Cart")
    @Severity(SeverityLevel.NORMAL)
    @Description("Remove item do carrinho e valida badge zerado.")
    void shouldRemoveBackpackFromCart() {
        homePage.addBackpackToCart();
        homePage.removeBackpackFromCart();

        assertCartBadgeCount(0);
    }

    // Navega para carrinho e valida página correta.
    @Test
    @Tag("home")
    @Tag("cart")
    @Tag("tc12")
    @DisplayName("TC12 - Deve abrir página de carrinho")
    @Story("Cart")
    @Severity(SeverityLevel.NORMAL)
    @Description("Navega para a página de carrinho a partir da home.")
    void shouldOpenCartPageFromHome() {
        homePage.openCart();

        assertTrue(homePage.isCartPageLoaded());
    }

    // Reseta estado da app e valida limpeza de carrinho.
    @Test
    @Tag("home")
    @Tag("menu")
    @Tag("tc13")
    @DisplayName("TC13 - Deve resetar estado da aplicação")
    @Story("Menu")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Executa reset do estado da aplicação e valida limpeza do carrinho.")
    void shouldResetAppStateAndClearCartBadge() {
        homePage.addBackpackToCart();
        assertCartBadgeCount(1);

        homePage.resetAppState();

        assertCartBadgeCount(0);
    }

    // Realiza logout e valida retorno para tela de login.
    @Test
    @Tag("home")
    @Tag("menu")
    @Tag("tc14")
    @DisplayName("TC14 - Deve fazer logout pelo menu")
    @Story("Menu")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Realiza logout pelo menu lateral e valida retorno para tela de login.")
    void shouldLogoutFromMenu() {
        homePage.logout();

        assertTrue(loginPage.isLoaded());
    }

    private void assertCartBadgeCount(int expectedCount) {
        assertEquals(expectedCount, homePage.getCartBadgeCount());
    }
}
