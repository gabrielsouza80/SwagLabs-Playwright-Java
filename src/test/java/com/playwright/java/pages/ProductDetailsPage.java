package com.playwright.java.pages;

import com.microsoft.playwright.Page;
import io.qameta.allure.Step;

// Page Object da página de detalhes do produto.
// Encapsula ações e validações específicas da página de detalhe.
public class ProductDetailsPage {
    private final Page page;

    // Seletores elementos da página de detalhes
    private static final String BACK_TO_PRODUCTS_BUTTON = "[data-test='back-to-products']";
    private static final String PRODUCT_NAME = "[data-test='inventory-item-name']";
    private static final String PRODUCT_DESC = "[data-test='inventory-item-desc']";
    private static final String PRODUCT_PRICE = "[data-test='inventory-item-price']";
    private static final String ADD_TO_CART_BUTTON = "[data-test='add-to-cart']";

    public ProductDetailsPage(Page page) {
        this.page = page;
    }

    // Validação se está na página de detalhes do produto.
    @Step("Validar carregamento da página de detalhes")
    public boolean isLoaded() {
        return page.url().contains("/inventory-item.html")
                && page.locator(PRODUCT_NAME).isVisible()
                && page.locator(PRODUCT_DESC).isVisible()
                && page.locator(PRODUCT_PRICE).isVisible();
    }

    // Retorna o nome do produto exibido.
    @Step("Obter nome do produto")
    public String getProductName() {
        return page.locator(PRODUCT_NAME).innerText().trim();
    }

    // Retorna a descrição do produto.
    @Step("Obter descrição do produto")
    public String getProductDescription() {
        return page.locator(PRODUCT_DESC).innerText().trim();
    }

    // Retorna o preço do produto.
    @Step("Obter preço do produto")
    public String getProductPrice() {
        return page.locator(PRODUCT_PRICE).innerText().trim();
    }

    // Clica no botão "Add to cart".
    @Step("Adicionar produto ao carrinho a partir de detalhes")
    public void addToCart() {
        page.locator(ADD_TO_CART_BUTTON).click();
    }

    // Valida se o botão "Add to cart" está visível.
    public boolean isAddToCartButtonVisible() {
        return page.locator(ADD_TO_CART_BUTTON).isVisible();
    }

    // Clica no botão "Back to products".
    @Step("Voltar para lista de produtos")
    public void backToProducts() {
        page.locator(BACK_TO_PRODUCTS_BUTTON).click();
    }

    // Valida se o botão "Back to products" está visível.
    public boolean isBackButtonVisible() {
        return page.locator(BACK_TO_PRODUCTS_BUTTON).isVisible();
    }
}
