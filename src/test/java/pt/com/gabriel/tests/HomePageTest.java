package pt.com.gabriel.tests;

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
import pt.com.gabriel.base.BaseTest;

// Suíte de testes da HomePage com foco em funcionalidades principais.
@Epic("Web Automation")
@Feature("Homepage")
public class HomePageTest extends BaseTest {

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

    // Valida título principal da página.
    @Test
    @Tag("home")
    @Tag("tc03")
    @DisplayName("TC03 - Deve exibir título Products")
    @Story("Home Header")
    @Severity(SeverityLevel.NORMAL)
    @Description("Valida o título da home como Products.")
    void shouldDisplayProductsTitle() {
        assertEquals("Products", homePage.getPageTitle());
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
        assertEquals("Name (A to Z)", homePage.getActiveSortOption());
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
        assertEquals(6, homePage.getInventoryItemCount());
    }

    // Ordenação por nome crescente (A-Z).
    @Test
    @Tag("home")
    @Tag("sorting")
    @Tag("tc06")
    @DisplayName("TC06 - Deve ordenar por nome crescente")
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
    void shouldAddBackpackToCart() {
        homePage.addBackpackToCart();

        assertTrue(homePage.isBackpackAddedToCart());
        assertEquals(1, homePage.getCartBadgeCount());
    }

    // Remove item do carrinho e valida contador zerado.
    @Test
    @Tag("home")
    @Tag("cart")
    @Tag("tc11")
    @DisplayName("TC11 - Deve remover mochila do carrinho")
    void shouldRemoveBackpackFromCart() {
        homePage.addBackpackToCart();
        homePage.removeBackpackFromCart();

        assertEquals(0, homePage.getCartBadgeCount());
    }

    // Navega para carrinho e valida página correta.
    @Test
    @Tag("home")
    @Tag("cart")
    @Tag("tc12")
    @DisplayName("TC12 - Deve abrir página de carrinho")
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
    void shouldResetAppStateAndClearCartBadge() {
        homePage.addBackpackToCart();
        assertEquals(1, homePage.getCartBadgeCount());

        homePage.resetAppState();

        assertEquals(0, homePage.getCartBadgeCount());
    }

    // Realiza logout e valida retorno para tela de login.
    @Test
    @Tag("home")
    @Tag("menu")
    @Tag("tc14")
    @DisplayName("TC14 - Deve fazer logout pelo menu")
    void shouldLogoutFromMenu() {
        homePage.logout();

        assertTrue(loginPage.isLoaded());
    }
}
