package com.playwright.java.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.playwright.java.base.BaseTest;
import com.playwright.java.config.TestData;
import com.playwright.java.pages.HomePage;
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

// HomePage test suite focused on core homepage behavior.
@Epic("Web Automation")
@Feature("Homepage")
@Owner("Gabriel Souza")
@TestMethodOrder(MethodOrderer.DisplayName.class)
public class HomePageTest extends BaseTest {
    private final TestData testData = TestData.get();

    @Test
    @Tag("home")
    @Tag("smoke")
    @Tag("tc11")
    @DisplayName("TC11 - Should display main homepage elements")
    @Story("Home Load")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Validates homepage loading after login and presence of key UI elements.")
    void shouldDisplayHomePageMainElements() {
        Allure.step("Given the user is authenticated and on the homepage", () ->
                assertTrue(homePage.isLoaded()));

        Allure.step("When checking title and homepage elements", () -> {
            assertTrue(homePage.hasExpectedTitle());
            assertTrue(homePage.hasMainHomeElements());
        });

        Allure.step("Then the main homepage elements should be visible", () ->
                assertTrue(homePage.hasMainHomeElements()));
    }

    @Test
    @Tag("home")
    @Tag("tc12")
    @DisplayName("TC12 - Should display Products title")
    @Story("Home Header")
    @Severity(SeverityLevel.NORMAL)
    @Description("Validates homepage title is Products.")
    void shouldDisplayProductsTitle() {
        Allure.step("Given the user is on the homepage", () ->
                assertTrue(homePage.isLoaded()));

        Allure.step("When checking the displayed header title", () ->
                homePage.getPageTitle());

        Allure.step("Then the displayed title should be Products", () ->
                assertTrue(homePage.hasExpectedTitle()));
    }

    @Test
    @Tag("home")
    @Tag("tc13")
    @DisplayName("TC13 - Should display default sorting Name (A to Z)")
    @Story("Sorting")
    @Severity(SeverityLevel.NORMAL)
    @Description("Validates default sorting option on first homepage load.")
    void shouldDisplayDefaultSortingOption() {
        Allure.step("Given the user opened the homepage", () ->
                assertTrue(homePage.isLoaded()));

        Allure.step("When checking the selected default sorting option", () ->
                homePage.getActiveSortOption());

        Allure.step("Then default sorting should be Name (A to Z)", () ->
                assertTrue(homePage.hasDefaultSortOption()));
    }

    @Test
    @Tag("home")
    @Tag("tc14")
    @DisplayName("TC14 - Should display 6 items in inventory list")
    @Story("Catalog")
    @Severity(SeverityLevel.NORMAL)
    @Description("Validates expected product count in inventory.")
    void shouldDisplaySixInventoryItems() {
        Allure.step("Given the user is on the homepage", () ->
                assertTrue(homePage.isLoaded()));

        Allure.step("When checking number of displayed items", () ->
                homePage.getInventoryItemCount());

        Allure.step("Then inventory list should contain 6 items", () ->
                assertTrue(homePage.hasExpectedInventoryItemCount()));
    }

    @Test
    @Tag("home")
    @Tag("sorting")
    @Tag("tc15")
    @DisplayName("TC15 - Should sort by name ascending")
    @Story("Sorting")
    @Severity(SeverityLevel.NORMAL)
    @Description("Validates sorting by name ascending (A to Z).")
    void shouldSortProductsByNameAscending() {
        Allure.step("Given the user is on the homepage", () ->
                assertTrue(homePage.isLoaded()));

        Allure.step("When selecting name ascending sorting (A-Z)", () ->
                homePage.sortByNameAscending());

        Allure.step("Then product names should be in ascending order", () ->
                assertTrue(homePage.areProductNamesSortedAscending()));
    }

    @Test
    @Tag("home")
    @Tag("sorting")
    @Tag("tc16")
    @DisplayName("TC16 - Should sort by name descending")
    @Story("Sorting")
    @Severity(SeverityLevel.NORMAL)
    @Description("Validates sorting by name descending (Z to A).")
    void shouldSortProductsByNameDescending() {
        Allure.step("Given the user is on the homepage", () ->
                assertTrue(homePage.isLoaded()));

        Allure.step("When selecting name descending sorting (Z-A)", () ->
                homePage.sortByNameDescending());

        Allure.step("Then product names should be in descending order", () ->
                assertTrue(homePage.areProductNamesSortedDescending()));
    }

    @Test
    @Tag("home")
    @Tag("sorting")
    @Tag("tc17")
    @DisplayName("TC17 - Should sort by price ascending")
    @Story("Sorting")
    @Severity(SeverityLevel.NORMAL)
    @Description("Validates sorting by price ascending (low to high).")
    void shouldSortProductsByPriceAscending() {
        Allure.step("Given the user is on the homepage", () ->
                assertTrue(homePage.isLoaded()));

        Allure.step("When selecting price ascending sorting (low to high)", () ->
                homePage.sortByPriceAscending());

        Allure.step("Then prices should be in ascending order", () ->
                assertTrue(homePage.arePricesSortedAscending()));
    }

    @Test
    @Tag("home")
    @Tag("sorting")
    @Tag("tc18")
    @DisplayName("TC18 - Should sort by price descending")
    @Story("Sorting")
    @Severity(SeverityLevel.NORMAL)
    @Description("Validates sorting by price descending (high to low).")
    void shouldSortProductsByPriceDescending() {
        Allure.step("Given the user is on the homepage", () ->
                assertTrue(homePage.isLoaded()));

        Allure.step("When selecting price descending sorting (high to low)", () ->
                homePage.sortByPriceDescending());

        Allure.step("Then prices should be in descending order", () ->
                assertTrue(homePage.arePricesSortedDescending()));
    }

    @Test
    @Tag("home")
    @Tag("cart")
    @Tag("tc19")
    @DisplayName("TC19 - Should add backpack to cart")
    @Story("Cart")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Adds Backpack to cart and validates visual state and badge count.")
    void shouldAddBackpackToCart() {
        Allure.step("Given the user is on the homepage", () ->
                assertTrue(homePage.isLoaded()));

        Allure.step("When adding Backpack to cart", () ->
                homePage.addBackpackToCart());

        Allure.step("Then Backpack remove button should be visible", () ->
                assertTrue(homePage.isBackpackAddedToCart()));

        Allure.step("And cart badge should display 1 item", () ->
                assertTrue(homePage.hasCartBadgeCount(1)));
    }

    @Test
    @Tag("home")
    @Tag("cart")
    @Tag("tc20")
    @DisplayName("TC20 - Should remove backpack from cart")
    @Story("Cart")
    @Severity(SeverityLevel.NORMAL)
    @Description("Removes item from cart and validates zero badge.")
    void shouldRemoveBackpackFromCart() {
        Allure.step("Given the user is on the homepage", () ->
                assertTrue(homePage.isLoaded()));

        Allure.step("And Backpack was already added", () ->
                homePage.addBackpackToCart());

        Allure.step("When removing Backpack from cart", () ->
                homePage.removeBackpackFromCart());

        Allure.step("Then cart badge should display 0 item", () ->
                assertTrue(homePage.hasCartBadgeCount(0)));
    }

    @Test
    @Tag("home")
    @Tag("product-details")
    @Tag("tc21")
    @DisplayName("TC21 - Should open product details page when clicking a product")
    @Story("Product Details In Home")
    @Severity(SeverityLevel.NORMAL)
    @Description("Validates that clicking a product opens its details page with correct information.")
    void shouldOpenProductDetailsPageFromHome() {
        Allure.step("Given the user is on the homepage", () ->
                assertTrue(homePage.isLoaded()));

        Allure.step("When clicking Backpack product", () ->
                homePage.clickProductByName(testData.product("backpack", "name")));

        Allure.step("Then product details page should load", () -> {
            assertTrue(homePage.isProductDetailsLoaded());
            assertEquals(testData.product("backpack", "name"), homePage.getProductDetailsName());
            assertTrue(homePage.getProductDetailsPrice().contains(testData.product("backpack", "price").replace("$", "")));
        });
    }

    @Test
    @Tag("home")
    @Tag("product-details")
    @Tag("tc22")
    @DisplayName("TC22 - Should display correct product information on details page")
    @Story("Product Details In Home")
    @Severity(SeverityLevel.NORMAL)
    @Description("Validates product name, description, and price on details page.")
    void shouldDisplayProductDetailsCorrectlyFromHome() {
        Allure.step("Given the user is on the homepage", () ->
                assertTrue(homePage.isLoaded()));

        Allure.step("When clicking Backpack product", () ->
                homePage.clickProductByName(testData.product("backpack", "name")));

        Allure.step("Then full product information should be displayed", () -> {
            assertTrue(homePage.isProductDetailsLoaded());
            assertEquals(testData.product("backpack", "name"), homePage.getProductDetailsName());
            assertTrue(homePage.getProductDetailsDescription().contains(testData.product("backpack", "descriptionContains")));
            assertEquals(testData.product("backpack", "price"), homePage.getProductDetailsPrice());
            assertTrue(homePage.isAddToCartButtonVisibleOnDetails());
        });
    }

    @Test
    @Tag("home")
    @Tag("product-details")
    @Tag("tc23")
    @DisplayName("TC23 - Should add product to cart from details page")
    @Story("Product Details In Home")
    @Severity(SeverityLevel.NORMAL)
    @Description("Validates adding a product to cart from details page.")
    void shouldAddProductToCartFromDetailsInHome() {
        Allure.step("Given the user is on the homepage", () ->
                assertTrue(homePage.isLoaded()));

        Allure.step("When opening Backpack details and adding to cart", () -> {
            homePage.clickProductByName(testData.product("backpack", "name"));
            assertTrue(homePage.isProductDetailsLoaded());
            homePage.addToCartFromProductDetails();
            homePage.backToProductsFromDetails();
        });

        Allure.step("Then cart badge should display 1 item", () -> {
            assertTrue(homePage.isLoaded());
            assertEquals(testData.testValueInt("HomePageTest", "TC23", "expectedCartBadge"), homePage.getCartBadgeCount());
        });
    }

    @Test
    @Tag("home")
    @Tag("product-details")
    @Tag("tc24")
    @DisplayName("TC24 - Should return to product list when clicking Back to products")
    @Story("Product Details In Home")
    @Severity(SeverityLevel.NORMAL)
    @Description("Validates that Back to products returns correctly to homepage.")
    void shouldNavigateBackToProductsListFromDetailsInHome() {
        Allure.step("Given the user is on product details page", () -> {
            assertTrue(homePage.isLoaded());
            homePage.clickProductByName(testData.product("backpack", "name"));
            assertTrue(homePage.isProductDetailsLoaded());
            assertTrue(homePage.isBackButtonVisibleOnDetails());
        });

        Allure.step("When clicking Back to products", () ->
                homePage.backToProductsFromDetails());

        Allure.step("Then the user should return to homepage", () -> {
            assertTrue(homePage.isLoaded());
            assertEquals(testData.expectedInt("inventoryItemCount"), homePage.getInventoryItemCount());
        });
    }

    @Test
    @Tag("home")
    @Tag("product-details")
    @Tag("tc25")
    @DisplayName("TC25 - Should keep cart state while navigating to details and back")
    @Story("Product Details In Home")
    @Severity(SeverityLevel.NORMAL)
    @Description("Validates cart quantity remains unchanged after opening and closing details page.")
    void shouldMaintainCartStateWhenNavigatingDetailsInHome() {
        Allure.step("Given the user has Backpack in cart on homepage", () -> {
            assertTrue(homePage.isLoaded());
            homePage.addBackpackToCart();
            assertTrue(homePage.isBackpackAddedToCart());
        });

        Allure.step("When opening product details and returning", () -> {
            homePage.clickProductByName(testData.product("backpack", "name"));
            assertTrue(homePage.isProductDetailsLoaded());
            homePage.backToProductsFromDetails();
        });

        Allure.step("Then cart should keep the item", () -> {
            assertTrue(homePage.isLoaded());
            assertEquals(testData.testValueInt("HomePageTest", "TC25", "expectedCartBadge"), homePage.getCartBadgeCount());
            assertTrue(homePage.isBackpackAddedToCart());
        });
    }

    @Test
    @Tag("home")
    @Tag("multi-user")
    @Tag("known-bug")
    @Tag("tc26")
    @DisplayName("TC26 - Should confirm homepage anomalies with problem_user")
    @Story("Home With Alternative Users")
    @Severity(SeverityLevel.NORMAL)
    @Description("Confirms known homepage issues for problem_user.")
    void shouldConfirmProblemUserHomeAnomalies() {
        Allure.label("knownIssue", testData.knownIssue("problemUserHome"));

        Allure.step("Given standard user is authenticated on homepage", () ->
                assertTrue(homePage.isLoaded()));

        Allure.step("When logging out and signing in with problem_user", () -> {
            homePage.logout();
            loginPage.loginWithProblemUser();
        });

        Allure.step("Then homepage should load for problem_user", () -> {
            assertTrue(homePage.isLoaded());
            assertTrue(homePage.hasExpectedInventoryItemCount());
        });

        HomePage.HomeAnomalyResult anomalyResult = homePage.analyzeProblemUserHomeAnomalies();

        Allure.step("And problem_user anomalies should be detected", () ->
                assertTrue(anomalyResult.hasProblemUserSpecificIssue()));

        Allure.addAttachment(
                "Known Defect Evidence",
                "text/plain",
                anomalyResult.toEvidenceText("problem_user"),
                ".txt"
        );
    }

    @Test
    @Tag("home")
    @Tag("multi-user")
    @Tag("known-bug")
    @Tag("tc27")
    @DisplayName("TC27 - Should confirm homepage anomaly with performance_glitch_user")
    @Story("Home With Alternative Users")
    @Severity(SeverityLevel.NORMAL)
    @Description("Confirms homepage delay/anomaly behavior for performance_glitch_user.")
    void shouldConfirmPerformanceGlitchUserHomeAnomalies() {
        Allure.label("knownIssue", testData.knownIssue("performanceGlitchHome"));
        final long[] loginDurationMs = new long[]{0L};

        Allure.step("Given standard user is authenticated on homepage", () ->
                assertTrue(homePage.isLoaded()));

        Allure.step("When logging out and signing in with performance_glitch_user", () -> {
            homePage.logout();
            assertTrue(loginPage.isLoaded());
            loginDurationMs[0] = loginPage.loginWithPerformanceGlitchUserAndMeasureDurationMs();
        });

        Allure.step("Then homepage should load for performance_glitch_user", () -> {
            assertTrue(homePage.isLoaded());
            assertTrue(homePage.hasExpectedInventoryItemCount());
        });

        HomePage.PerformanceGlitchHomeAnomalyResult anomalyResult =
                homePage.analyzePerformanceGlitchUserIssues(loginDurationMs[0]);

        Allure.step("And performance glitch delay should be detected", () ->
                assertTrue(anomalyResult.hasPerformanceGlitchSpecificIssue()));

        Allure.addAttachment(
                "Known Defect Evidence",
                "text/plain",
                anomalyResult.toEvidenceText(),
                ".txt"
        );
    }

    @Test
    @Tag("home")
    @Tag("multi-user")
    @Tag("known-bug")
    @Tag("tc28")
    @DisplayName("TC28 - Should analyze homepage anomalies with error_user")
    @Story("Home With Alternative Users")
    @Severity(SeverityLevel.NORMAL)
    @Description("Analyzes homepage anomalies for error_user and records findings.")
    void shouldConfirmErrorUserHomeAnomalies() {
        Allure.label("knownIssue", testData.knownIssue("errorUserHome"));

        Allure.step("Given standard user is authenticated on homepage", () ->
                assertTrue(homePage.isLoaded()));

        Allure.step("When logging out and signing in with error_user", () -> {
            homePage.logout();
            assertTrue(loginPage.isLoaded());
            loginPage.loginWithErrorUser();
        });

        Allure.step("Then homepage should load for error_user", () -> {
            assertTrue(homePage.isLoaded());
            assertTrue(homePage.hasExpectedInventoryItemCount());
        });

        HomePage.HomeAnomalyResult anomalyResult = homePage.analyzeErrorUserHomeAnomalies();

        Allure.step("And anomaly analysis should be attached", () ->
                Allure.addAttachment(
                        "Analysis Result",
                        "text/plain",
                        anomalyResult.toEvidenceText("error_user"),
                        ".txt"
                ));
    }

    @Test
    @Tag("home")
    @Tag("multi-user")
    @Tag("known-bug")
    @Tag("tc29")
    @DisplayName("TC29 - Should confirm visual homepage anomaly with visual_user")
    @Story("Home With Alternative Users")
    @Severity(SeverityLevel.NORMAL)
    @Description("Confirms visual CSS anomaly behavior for visual_user.")
    void shouldConfirmVisualUserHomeAnomalies() {
        Allure.label("knownIssue", testData.knownIssue("visualUserHome"));

        Allure.step("Given standard user is authenticated on homepage", () ->
                assertTrue(homePage.isLoaded()));

        Allure.step("When logging out and signing in with visual_user", () -> {
            homePage.logout();
            assertTrue(loginPage.isLoaded());
            loginPage.loginWithVisualUser();
        });

        Allure.step("Then homepage should load for visual_user", () -> {
            assertTrue(homePage.isLoaded());
            assertTrue(homePage.hasExpectedInventoryItemCount());
        });

        HomePage.VisualUserHomeAnomalyResult visualAnomalyResult = homePage.analyzeVisualUserHomeAnomalies();

        Allure.step("And visual CSS anomaly should be detected", () ->
                assertTrue(visualAnomalyResult.hasVisualUserSpecificIssue()));

        Allure.addAttachment(
                "Known Defect Evidence",
                "text/plain",
                visualAnomalyResult.toEvidenceText(),
                ".txt"
        );
    }
}
