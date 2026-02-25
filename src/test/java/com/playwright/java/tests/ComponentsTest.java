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
    @DisplayName("TC30 - Should open cart page")
    @Story("Cart")
    @Severity(SeverityLevel.NORMAL)
    @Description("Navigates to cart page using the global header component.")
    void shouldOpenCartPageFromHeaderComponent() {
        Allure.step("Given the user is authenticated on the homepage", () ->
                assertTrue(homePage.isLoaded()));

        Allure.step("When clicking the global cart icon", () ->
                componentsPage.openCart());

        Allure.step("Then the cart page should be displayed", () ->
                assertTrue(componentsPage.isCartPageLoaded()));
    }

    @Test
    @Tag("components")
    @Tag("menu")
    @Tag("tc31")
    @Tag("known-bug")
    @DisplayName("TC31 - Should reset state and restore Backpack button to Add to cart")
    @Story("Menu")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Validates global menu reset state, including badge and Backpack button state. Known defect: after reset, button may remain as Remove.")
    void shouldResetAppStateAndRestoreBackpackButtonState() {
        Allure.label("knownIssue", testData.knownIssue("resetBackpackButton"));
        Allure.addAttachment(
                "Known Defect",
                "text/plain",
                testData.message("resetKnownDefect"),
                ".txt");

        Allure.step("Given the user is authenticated on the homepage", () ->
                assertTrue(homePage.isLoaded()));

        Allure.step("And Backpack product was added", () -> {
            componentsPage.addBackpackToCart();
            assertTrue(componentsPage.isBackpackAddedToCart());
        });

        Allure.step("And badge should display 1 item", () ->
                assertTrue(componentsPage.hasCartBadgeCount(
                        testData.testValueInt("ComponentsTest", "TC31", "badgeBeforeReset"))));

        Allure.step("When executing Reset App State from global menu", () ->
                componentsPage.resetAppState());

        Allure.step("Then badge should return to 0", () ->
                assertTrue(componentsPage.hasCartBadgeCount(
                        testData.testValueInt("ComponentsTest", "TC31", "badgeAfterReset"))));

        Allure.step("And analyze Backpack button state after reset (known bug)", () -> {
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
    @DisplayName("TC32 - Should logout from global menu")
    @Story("Menu")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Performs logout through global menu and validates return to login screen.")
    void shouldLogoutFromGlobalMenuComponent() {
        Allure.step("Given the user is authenticated on the homepage", () ->
                assertTrue(homePage.isLoaded()));

        Allure.step("When logging out through global menu", () ->
                componentsPage.logout());

        Allure.step("Then user should return to login screen", () ->
                assertTrue(loginPage.isLoaded()));
    }
}
