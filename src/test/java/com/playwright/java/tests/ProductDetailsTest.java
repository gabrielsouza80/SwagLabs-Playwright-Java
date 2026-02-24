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

// Suíte de testes da página de detalhes do produto.
@Epic("Web Automation")
@Feature("Product Details")
@Owner("Gabriel Souza")
public class ProductDetailsTest extends BaseTest {
    private static final String BACKPACK_PRODUCT_NAME = "Sauce Labs Backpack";
    private static final String BACKPACK_PRODUCT_PRICE = "$29.99";

    @Test
    @Tag("product-details")
    @Tag("tc20")
    @DisplayName("TC20 - Deve abrir página de detalhes ao clicar em um produto")
    @Story("Product Details")
    @Severity(SeverityLevel.NORMAL)
    @Description("Valida que clicar em um produto leva à página de detalhes com informações corretas.")
    void shouldOpenProductDetailsPage() {
        // Dado que o usuário está na Home
        assertTrue(homePage.isLoaded());

        // Quando clica em um produto (Backpack)
        homePage.clickProductByName(BACKPACK_PRODUCT_NAME);

        // Então a página de detalhes deve carregar
        assertTrue(productDetailsPage.isLoaded());
        assertEquals(BACKPACK_PRODUCT_NAME, productDetailsPage.getProductName());
        assertTrue(productDetailsPage.getProductPrice().contains("29.99"));
    }

    @Test
    @Tag("product-details")
    @Tag("tc21")
    @DisplayName("TC21 - Deve exibir informações corretas do produto nos detalhes")
    @Story("Product Details")
    @Severity(SeverityLevel.NORMAL)
    @Description("Valida que todas as informações do produto (nome, descrição, preço) são exibidas corretamente.")
    void shouldDisplayProductDetailsCorrectly() {
        // Dado que o usuário está na Home
        assertTrue(homePage.isLoaded());

        // Quando clica em um produto
        homePage.clickProductByName(BACKPACK_PRODUCT_NAME);

        // Então deve exibir as informações completas
        assertTrue(productDetailsPage.isLoaded());
        assertEquals(BACKPACK_PRODUCT_NAME, productDetailsPage.getProductName());
        assertTrue(productDetailsPage.getProductDescription().contains("carry.allTheThings()"));
        assertEquals(BACKPACK_PRODUCT_PRICE, productDetailsPage.getProductPrice());
        assertTrue(productDetailsPage.isAddToCartButtonVisible());
    }

    @Test
    @Tag("product-details")
    @Tag("tc22")
    @DisplayName("TC22 - Deve adicionar produto ao carrinho a partir de detalhes")
    @Story("Product Details")
    @Severity(SeverityLevel.NORMAL)
    @Description("Valida que é possível adicionar um produto ao carrinho a partir da página de detalhes.")
    void shouldAddProductToCartFromDetails() {
        // Dado que o usuário está na página de detalhes de um produto
        assertTrue(homePage.isLoaded());
        homePage.clickProductByName(BACKPACK_PRODUCT_NAME);
        assertTrue(productDetailsPage.isLoaded());

        // Quando clica no botão "Add to cart"
        productDetailsPage.addToCart();

        // Então o produto deve ser adicionado (não há feedback visual aqui, mas a ação ocorre)
        // Para validar melhor, voltar à Home e verificar badge do carrinho
        productDetailsPage.backToProducts();
        assertTrue(homePage.isLoaded());
        assertEquals(1, homePage.getCartBadgeCount());
    }

    @Test
    @Tag("product-details")
    @Tag("tc23")
    @DisplayName("TC23 - Deve voltar para lista ao clicar em Back to products")
    @Story("Product Details")
    @Severity(SeverityLevel.NORMAL)
    @Description("Valida que o botão Back to products retorna corretamente à home.")
    void shouldNavigateBackToProductsList() {
        // Dado que o usuário está na página de detalhes
        assertTrue(homePage.isLoaded());
        homePage.clickProductByName(BACKPACK_PRODUCT_NAME);
        assertTrue(productDetailsPage.isLoaded());
        assertTrue(productDetailsPage.isBackButtonVisible());

        // Quando clica no botão "Back to products"
        productDetailsPage.backToProducts();

        // Então deve retornar à home
        assertTrue(homePage.isLoaded());
        assertEquals(6, homePage.getInventoryItemCount());
    }

    @Test
    @Tag("product-details")
    @Tag("tc24")
    @DisplayName("TC24 - Deve manter estado do carrinho ao navegar para detalhes e voltar")
    @Story("Product Details")
    @Severity(SeverityLevel.NORMAL)
    @Description("Valida que a quantidade do carrinho é mantida ao abrir e fechar detalhes.")
    void shouldMaintainCartStateWhenNavigatingDetails() {
        // Dado que o usuário tem items no carrinho
        assertTrue(homePage.isLoaded());
        homePage.addBackpackToCart();
        assertTrue(homePage.isBackpackAddedToCart());

        // Quando abre os detalhes d um produto e volta
        homePage.clickProductByName(BACKPACK_PRODUCT_NAME);
        assertTrue(productDetailsPage.isLoaded());
        productDetailsPage.backToProducts();

        // Então o carrinho deve manter o item
        assertTrue(homePage.isLoaded());
        assertEquals(1, homePage.getCartBadgeCount());
        assertTrue(homePage.isBackpackAddedToCart());
    }
}
