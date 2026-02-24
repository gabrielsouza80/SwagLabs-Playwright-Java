package com.playwright.java.pages;

import com.microsoft.playwright.Page;
import com.playwright.java.config.TestData;
import io.qameta.allure.Step;

public class ComponentsPage {
    private final Page page;
    private final TestData testData;

    private static final String TITLE = "[data-test='title']";
    private static final String CART_LINK = "[data-test='shopping-cart-link']";
    private static final String CART_BADGE = "[data-test='shopping-cart-badge']";
    private static final String OPEN_MENU = "#react-burger-menu-btn";
    private static final String LOGOUT_SIDEBAR_LINK = "[data-test='logout-sidebar-link']";
    private static final String RESET_SIDEBAR_LINK = "[data-test='reset-sidebar-link']";
    private static final String ADD_BACKPACK_BUTTON = "[data-test='add-to-cart-sauce-labs-backpack']";
    private static final String REMOVE_BACKPACK_BUTTON = "[data-test='remove-sauce-labs-backpack']";

    public ComponentsPage(Page page) {
        this.page = page;
        this.testData = TestData.get();
    }

    @Step("Adicionar produto Backpack ao carrinho")
    public void addBackpackToCart() {
        page.locator(ADD_BACKPACK_BUTTON).click();
    }

    public boolean isBackpackAddedToCart() {
        return page.locator(REMOVE_BACKPACK_BUTTON).isVisible();
    }

    public boolean isBackpackReadyToAdd() {
        return page.locator(ADD_BACKPACK_BUTTON).isVisible() && page.locator(REMOVE_BACKPACK_BUTTON).count() == 0;
    }

    public int getCartBadgeCount() {
        if (page.locator(CART_BADGE).count() == 0) {
            return 0;
        }
        return Integer.parseInt(page.locator(CART_BADGE).innerText().trim());
    }

    public boolean hasCartBadgeCount(int expectedCount) {
        return getCartBadgeCount() == expectedCount;
    }

    @Step("Abrir página do carrinho")
    public void openCart() {
        page.locator(CART_LINK).click();
    }

    public boolean isCartPageLoaded() {
        return page.url().contains("/cart.html")
                && testData.expected("cartTitle").equals(page.locator(TITLE).innerText().trim());
    }

    @Step("Abrir menu lateral")
    public void openMenu() {
        page.locator(OPEN_MENU).click();
    }

    @Step("Fazer logout")
    public void logout() {
        openMenu();
        page.locator(LOGOUT_SIDEBAR_LINK).click();
    }

    @Step("Resetar estado da aplicação")
    public void resetAppState() {
        openMenu();
        page.locator(RESET_SIDEBAR_LINK).click();
    }
}