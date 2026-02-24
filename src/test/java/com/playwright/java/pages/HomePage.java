package com.playwright.java.pages;

import com.microsoft.playwright.Page;
import com.playwright.java.config.TestData;
import io.qameta.allure.Step;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

// Page Object da Home/Inventory.
// Reúne ações e validações funcionais da página principal após login.
public class HomePage {
    private final Page page;
    private final TestData testData;

    // Seletores dos elementos importantes da home.
    private static final String TITLE = "[data-test='title']";
    private static final String INVENTORY_LIST = "[data-test='inventory-list']";
    private static final String INVENTORY_ITEM = "[data-test='inventory-item']";
    private static final String SORT_DROPDOWN = "[data-test='product-sort-container']";
    private static final String ACTIVE_SORT_OPTION = "[data-test='active-option']";
    private static final String CART_LINK = "[data-test='shopping-cart-link']";
    private static final String CART_BADGE = "[data-test='shopping-cart-badge']";
    private static final String OPEN_MENU = "#react-burger-menu-btn";
    private static final String LOGOUT_SIDEBAR_LINK = "[data-test='logout-sidebar-link']";
    private static final String RESET_SIDEBAR_LINK = "[data-test='reset-sidebar-link']";
    private static final String FOOTER = "[data-test='footer']";
    private static final String PRODUCT_NAME = "[data-test='inventory-item-name']";
    private static final String PRODUCT_PRICE = "[data-test='inventory-item-price']";
    private static final String ADD_BACKPACK_BUTTON = "[data-test='add-to-cart-sauce-labs-backpack']";
    private static final String REMOVE_BACKPACK_BUTTON = "[data-test='remove-sauce-labs-backpack']";

    public HomePage(Page page) {
        this.page = page;
        this.testData = TestData.get();
    }

    // Validação principal de carregamento da home.
    @Step("Validar carregamento da Home/Inventory")
    public boolean isLoaded() {
        return page.url().contains("/inventory.html")
                && page.locator(INVENTORY_LIST).isVisible()
                && testData.expected("homeTitle").equals(page.locator(TITLE).innerText().trim());
    }

    // Conta quantos produtos existem na listagem.
    @Step("Contar itens da listagem")
    public int getInventoryItemCount() {
        return page.locator(INVENTORY_ITEM).count();
    }

    // Retorna o título exibido na home (Products).
    @Step("Obter título da Home")
    public String getPageTitle() {
        return page.locator(TITLE).innerText().trim();
    }

    // Retorna a opção de ordenação ativa no topo da página.
    @Step("Obter opção de ordenação ativa")
    public String getActiveSortOption() {
        return page.locator(ACTIVE_SORT_OPTION).innerText().trim();
    }

    public boolean hasExpectedTitle() {
        return testData.expected("homeTitle").equals(getPageTitle());
    }

    public boolean hasDefaultSortOption() {
        return testData.expected("defaultSortLabel").equals(getActiveSortOption());
    }

    public boolean hasExpectedInventoryItemCount() {
        return getInventoryItemCount() == testData.expectedInt("inventoryItemCount");
    }

    // Altera ordenação via value do select: az, za, lohi, hilo.
    @Step("Ordenar produtos por opção: {optionValue}")
    public void sortBy(String optionValue) {
        page.locator(SORT_DROPDOWN).selectOption(optionValue);
    }

    @Step("Ordenar por nome crescente (A-Z)")
    public void sortByNameAscending() {
        sortBy(testData.sortOption("nameAsc"));
    }

    @Step("Ordenar por nome decrescente (Z-A)")
    public void sortByNameDescending() {
        sortBy(testData.sortOption("nameDesc"));
    }

    @Step("Ordenar por preço crescente (low to high)")
    public void sortByPriceAscending() {
        sortBy(testData.sortOption("priceAsc"));
    }

    @Step("Ordenar por preço decrescente (high to low)")
    public void sortByPriceDescending() {
        sortBy(testData.sortOption("priceDesc"));
    }

    // Captura os nomes de todos os produtos visíveis.
    public List<String> getProductNames() {
        return page.locator(PRODUCT_NAME).allInnerTexts().stream()
                .map(String::trim)
                .collect(Collectors.toList());
    }

    // Captura preços de todos os produtos e converte para número.
    public List<Double> getProductPrices() {
        return page.locator(PRODUCT_PRICE).allInnerTexts().stream()
                .map(text -> text.replace("$", "").trim())
                .map(Double::parseDouble)
                .collect(Collectors.toList());
    }

    // Verifica se nomes estão em ordem crescente (A-Z).
    public boolean areProductNamesSortedAscending() {
        List<String> names = getProductNames();
        List<String> sorted = names.stream().sorted().collect(Collectors.toList());
        return names.equals(sorted);
    }

    // Verifica se nomes estão em ordem decrescente (Z-A).
    public boolean areProductNamesSortedDescending() {
        List<String> names = getProductNames();
        List<String> sorted = names.stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList());
        return names.equals(sorted);
    }

    // Verifica se preços estão do menor para o maior.
    public boolean arePricesSortedAscending() {
        List<Double> prices = getProductPrices();
        List<Double> sorted = prices.stream().sorted().collect(Collectors.toList());
        return prices.equals(sorted);
    }

    // Verifica se preços estão do maior para o menor.
    public boolean arePricesSortedDescending() {
        List<Double> prices = getProductPrices();
        List<Double> sorted = prices.stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList());
        return prices.equals(sorted);
    }

    // Adiciona o item Backpack ao carrinho.
    @Step("Adicionar produto Backpack ao carrinho")
    public void addBackpackToCart() {
        page.locator(ADD_BACKPACK_BUTTON).click();
    }

    // Remove o item Backpack do carrinho.
    @Step("Remover produto Backpack do carrinho")
    public void removeBackpackFromCart() {
        page.locator(REMOVE_BACKPACK_BUTTON).click();
    }

    // Se o botão Remove está visível, significa que o item foi adicionado.
    public boolean isBackpackAddedToCart() {
        return page.locator(REMOVE_BACKPACK_BUTTON).isVisible();
    }

    // Retorna quantidade de itens no badge do carrinho.
    // Se não existir badge, considera 0.
    public int getCartBadgeCount() {
        if (page.locator(CART_BADGE).count() == 0) {
            return 0;
        }
        return Integer.parseInt(page.locator(CART_BADGE).innerText().trim());
    }

    public boolean hasCartBadgeCount(int expectedCount) {
        return getCartBadgeCount() == expectedCount;
    }

    // Abre a tela do carrinho.
    @Step("Abrir página do carrinho")
    public void openCart() {
        page.locator(CART_LINK).click();
    }

    // Valida se está na página de carrinho.
    public boolean isCartPageLoaded() {
        return page.url().contains("/cart.html")
                && testData.expected("cartTitle").equals(page.locator(TITLE).innerText().trim());
    }

    // Abre menu lateral hamburguer.
    @Step("Abrir menu lateral")
    public void openMenu() {
        page.locator(OPEN_MENU).click();
    }

    // Faz logout pelo menu lateral.
    @Step("Fazer logout pela Home")
    public void logout() {
        openMenu();
        page.locator(LOGOUT_SIDEBAR_LINK).click();
    }

    // Reseta estado da aplicação (limpa carrinho/estado interno da sessão).
    @Step("Resetar estado da aplicação")
    public void resetAppState() {
        openMenu();
        page.locator(RESET_SIDEBAR_LINK).click();
    }

    // Verifica presença de elementos principais da home.
    public boolean hasMainHomeElements() {
        return page.locator(SORT_DROPDOWN).isVisible()
                && page.locator(CART_LINK).isVisible()
                && page.locator(OPEN_MENU).isVisible()
                && page.locator(FOOTER).isVisible();
    }
}
