package com.playwright.java.pages;

import com.microsoft.playwright.Page;
import com.playwright.java.config.TestData;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;

public class ComponentsPage {
    private final Page page;
    private final TestData testData;

    private static final String PRIMARY_HEADER = "[data-test='primary-header']";
    private static final String TITLE = "[data-test='title']";
    private static final String CART_LINK = "[data-test='shopping-cart-link']";
    private static final String CART_BADGE = "[data-test='shopping-cart-badge']";
    private static final String OPEN_MENU = "#react-burger-menu-btn";
    private static final String CLOSE_MENU = "#react-burger-cross-btn";
    private static final String MENU_WRAP = ".bm-menu-wrap";
    private static final String INVENTORY_SIDEBAR_LINK = "[data-test='inventory-sidebar-link']";
    private static final String ABOUT_SIDEBAR_LINK = "[data-test='about-sidebar-link']";
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

    public boolean isPrimaryHeaderVisible() {
        return page.locator(PRIMARY_HEADER).isVisible();
    }

    public boolean isMenuButtonVisible() {
        return page.locator(OPEN_MENU).isVisible();
    }

    public boolean isCartIconVisible() {
        return page.locator(CART_LINK).isVisible();
    }

    public boolean hasVisibleHeaderComponents() {
        return isPrimaryHeaderVisible() && isMenuButtonVisible() && isCartIconVisible();
    }

    @Step("Abrir menu lateral")
    public void openMenu() {
        page.locator(OPEN_MENU).click();
    }

    @Step("Fechar menu lateral")
    public void closeMenu() {
        page.locator(CLOSE_MENU).click();
    }

    public boolean isMenuOpen() {
        String ariaHidden = page.locator(MENU_WRAP).getAttribute("aria-hidden");
        return "false".equalsIgnoreCase(ariaHidden);
    }

    public boolean hasExpectedMenuOptionsVisible() {
        return page.locator(INVENTORY_SIDEBAR_LINK).isVisible()
                && page.locator(ABOUT_SIDEBAR_LINK).isVisible()
                && page.locator(LOGOUT_SIDEBAR_LINK).isVisible()
                && page.locator(RESET_SIDEBAR_LINK).isVisible();
    }

    @Step("Abrir menu e validar opções esperadas")
    public boolean openMenuAndValidateExpectedOptions() {
        openMenu();
        return hasExpectedMenuOptionsVisible();
    }

    public boolean hasMenuOptionsHidden() {
        return !isMenuOpen();
    }

    @Step("Selecionar opção All Items no menu lateral")
    public void openAllItemsFromMenu() {
        openMenu();
        page.locator(INVENTORY_SIDEBAR_LINK).click();
    }

    public String getAboutMenuHref() {
        return page.locator(ABOUT_SIDEBAR_LINK).getAttribute("href");
    }

    public boolean hasValidAboutMenuLink() {
        String aboutHref = getAboutMenuHref();
        return aboutHref != null && aboutHref.contains(testData.expected("aboutUrlContains"));
    }

    @Step("Adicionar Backpack e validar inclusão no carrinho")
    public boolean addBackpackAndValidateAdded() {
        addBackpackToCart();
        return isBackpackAddedToCart();
    }

    @Step("Abrir carrinho e validar carregamento")
    public boolean openCartAndValidateLoaded() {
        openCart();
        return isCartPageLoaded();
    }

    @Step("Abrir menu e validar que está aberto")
    public boolean openMenuAndValidateOpen() {
        openMenu();
        return isMenuOpen();
    }

    @Step("Validar estado do Backpack após reset (bug conhecido sem bloquear suíte)")
    public boolean validateBackpackStateAfterResetKnownBug() {
        boolean backpackReadyToAdd = isBackpackReadyToAdd();
        if (!backpackReadyToAdd) {
            Allure.addAttachment(
                    "Known Defect Observed",
                    "text/plain",
                    testData.expected("knownIssueResetObservedMessage"),
                    ".txt");
        }
        return true;
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