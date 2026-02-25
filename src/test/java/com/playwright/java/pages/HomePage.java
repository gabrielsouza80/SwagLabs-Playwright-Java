package com.playwright.java.pages;

import com.microsoft.playwright.Page;
import com.playwright.java.config.TestData;
import io.qameta.allure.Step;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

// Page Object for Home/Inventory.
// Contains main actions and functional validations after login.
public class HomePage {
    private final Page page;
    private final TestData testData;

    // Selectors for key homepage elements.
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
    private static final String PRODUCT_DESC = "[data-test='inventory-item-desc']";
    private static final String PRODUCT_PRICE = "[data-test='inventory-item-price']";
    private static final String BACK_TO_PRODUCTS_BUTTON = "[data-test='back-to-products']";
    private static final String ADD_TO_CART_DETAILS_BUTTON = "[data-test='add-to-cart']";
    private static final String ADD_BACKPACK_BUTTON = "[data-test='add-to-cart-sauce-labs-backpack']";
    private static final String REMOVE_BACKPACK_BUTTON = "[data-test='remove-sauce-labs-backpack']";

    public static final class HomeAnomalyResult {
        private final boolean brokenImageIssue;
        private final boolean startedWithRemove;
        private final boolean removeDidNotSwitchToAdd;
        private final boolean addDidNotSwitchToRemove;

        public HomeAnomalyResult(
                boolean brokenImageIssue,
                boolean startedWithRemove,
                boolean removeDidNotSwitchToAdd,
                boolean addDidNotSwitchToRemove) {
            this.brokenImageIssue = brokenImageIssue;
            this.startedWithRemove = startedWithRemove;
            this.removeDidNotSwitchToAdd = removeDidNotSwitchToAdd;
            this.addDidNotSwitchToRemove = addDidNotSwitchToRemove;
        }

        public boolean hasAnyKnownIssue() {
            return brokenImageIssue || startedWithRemove || removeDidNotSwitchToAdd || addDidNotSwitchToRemove;
        }

        public boolean hasButtonStateIssue() {
            return startedWithRemove || removeDidNotSwitchToAdd || addDidNotSwitchToRemove;
        }

        public boolean hasProblemUserSpecificIssue() {
            // problem_user may show broken images (sl-404 placeholder)
            // or Backpack starting in Remove state (without being added)
            return brokenImageIssue || startedWithRemove;
        }

        public boolean hasErrorUserSpecificIssue() {
            return brokenImageIssue || startedWithRemove || removeDidNotSwitchToAdd || addDidNotSwitchToRemove;
        }

        public String toEvidenceText(String userKey) {
            return userKey + " anomalies -> brokenImage="
                    + brokenImageIssue
                    + ", startedWithRemove="
                    + startedWithRemove
                    + ", removeDidNotSwitchToAdd="
                    + removeDidNotSwitchToAdd
                    + ", addDidNotSwitchToRemove="
                    + addDidNotSwitchToRemove;
        }
    }

    public static final class PerformanceGlitchHomeAnomalyResult {
        private final HomeAnomalyResult homeAnomalyResult;
        private final long loginDurationMs;
        private final long delayThresholdMs;

        public PerformanceGlitchHomeAnomalyResult(
                HomeAnomalyResult homeAnomalyResult,
                long loginDurationMs,
                long delayThresholdMs) {
            this.homeAnomalyResult = homeAnomalyResult;
            this.loginDurationMs = loginDurationMs;
            this.delayThresholdMs = delayThresholdMs;
        }

        public boolean hasDelayIssue() {
            return loginDurationMs >= delayThresholdMs;
        }

        public boolean hasAnyKnownIssue() {
            return hasDelayIssue() || homeAnomalyResult.hasAnyKnownIssue();
        }

        public boolean hasPerformanceGlitchSpecificIssue() {
            return hasDelayIssue();
        }

        public String toEvidenceText() {
            return homeAnomalyResult.toEvidenceText("performance_glitch_user")
                    + ", loginDurationMs="
                    + loginDurationMs
                    + ", delayThresholdMs="
                    + delayThresholdMs
                    + ", delayIssue="
                    + hasDelayIssue();
        }
    }

    public static final class VisualUserHomeAnomalyResult {
        private final boolean textMisalignmentIssue;
        private final boolean buttonMisalignmentIssue;

        public VisualUserHomeAnomalyResult(
                boolean textMisalignmentIssue,
                boolean buttonMisalignmentIssue) {
            this.textMisalignmentIssue = textMisalignmentIssue;
            this.buttonMisalignmentIssue = buttonMisalignmentIssue;
        }

        public boolean hasAnyKnownIssue() {
            return textMisalignmentIssue || buttonMisalignmentIssue;
        }

        public boolean hasVisualUserSpecificIssue() {
            return textMisalignmentIssue || buttonMisalignmentIssue;
        }

        public String toEvidenceText() {
            return "visual_user CSS anomalies -> textMisalignment="
                    + textMisalignmentIssue
                    + ", buttonMisalignment="
                    + buttonMisalignmentIssue;
        }
    }

    public HomePage(Page page) {
        this.page = page;
        this.testData = TestData.get();
    }

    // Validates the main Home/Inventory load state.
    @Step("Validate Home/Inventory is loaded")
    public boolean isLoaded() {
        return page.url().contains(testData.route("inventory"))
                && page.locator(INVENTORY_LIST).isVisible()
                && testData.expected("homeTitle").equals(page.locator(TITLE).innerText().trim());
    }

    // Counts how many products are listed.
    @Step("Count listed inventory items")
    public int getInventoryItemCount() {
        return page.locator(INVENTORY_ITEM).count();
    }

    // Returns the title shown on home (Products).
    @Step("Get homepage title")
    public String getPageTitle() {
        return page.locator(TITLE).innerText().trim();
    }

    // Returns the active sorting option shown at the top.
    @Step("Get active sort option")
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

    // Changes sorting by select option value: az, za, lohi, hilo.
    @Step("Sort products by option: {optionValue}")
    public void sortBy(String optionValue) {
        page.locator(SORT_DROPDOWN).selectOption(optionValue);
    }

    @Step("Sort by name ascending (A-Z)")
    public void sortByNameAscending() {
        sortBy(testData.sortOption("nameAsc"));
    }

    @Step("Sort by name descending (Z-A)")
    public void sortByNameDescending() {
        sortBy(testData.sortOption("nameDesc"));
    }

    @Step("Sort by price ascending (low to high)")
    public void sortByPriceAscending() {
        sortBy(testData.sortOption("priceAsc"));
    }

    @Step("Sort by price descending (high to low)")
    public void sortByPriceDescending() {
        sortBy(testData.sortOption("priceDesc"));
    }

    // Clicks a specific product by name.
    @Step("Click product: {productName}")
    public void clickProductByName(String productName) {
        page.locator("[data-test='inventory-item-name']:has-text('" + productName + "')")
            .first()
            .click();
    }

    // Clicks a specific product by item data-test.
    @Step("Click product by ID: {itemDataTest}")
    public void clickProductByDataTest(String itemDataTest) {
        page.locator("[data-test='" + itemDataTest + "-img-link']").click();
    }

    @Step("Validate product details page is loaded")
    public boolean isProductDetailsLoaded() {
        return page.url().contains(testData.route("productDetails"))
                && page.locator(PRODUCT_NAME).isVisible()
                && page.locator(PRODUCT_DESC).isVisible()
                && page.locator(PRODUCT_PRICE).isVisible();
    }

    @Step("Get product name from details")
    public String getProductDetailsName() {
        return page.locator(PRODUCT_NAME).innerText().trim();
    }

    @Step("Get product description from details")
    public String getProductDetailsDescription() {
        return page.locator(PRODUCT_DESC).innerText().trim();
    }

    @Step("Get product price from details")
    public String getProductDetailsPrice() {
        return page.locator(PRODUCT_PRICE).innerText().trim();
    }

    @Step("Add to cart from details")
    public void addToCartFromProductDetails() {
        if (page.locator(ADD_TO_CART_DETAILS_BUTTON).count() > 0) {
            page.locator(ADD_TO_CART_DETAILS_BUTTON).click();
            return;
        }
        page.locator("button[data-test^='add-to-cart']").first().click();
    }

    public boolean isAddToCartButtonVisibleOnDetails() {
        return page.locator(ADD_TO_CART_DETAILS_BUTTON).count() > 0
                || page.locator("button[data-test^='add-to-cart']").count() > 0;
    }

    @Step("Return from details to product list")
    public void backToProductsFromDetails() {
        page.locator(BACK_TO_PRODUCTS_BUTTON).click();
    }

    public boolean isBackButtonVisibleOnDetails() {
        return page.locator(BACK_TO_PRODUCTS_BUTTON).isVisible();
    }

    // Captures names of all visible products.
    public List<String> getProductNames() {
        return page.locator(PRODUCT_NAME).allInnerTexts().stream()
                .map(String::trim)
                .collect(Collectors.toList());
    }

    // Captures prices of all products and converts to number.
    public List<Double> getProductPrices() {
        return page.locator(PRODUCT_PRICE).allInnerTexts().stream()
                .map(text -> text.replace("$", "").trim())
                .map(Double::parseDouble)
                .collect(Collectors.toList());
    }

    // Checks if names are in ascending order (A-Z).
    public boolean areProductNamesSortedAscending() {
        List<String> names = getProductNames();
        List<String> sorted = names.stream().sorted().collect(Collectors.toList());
        return names.equals(sorted);
    }

    // Checks if names are in descending order (Z-A).
    public boolean areProductNamesSortedDescending() {
        List<String> names = getProductNames();
        List<String> sorted = names.stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList());
        return names.equals(sorted);
    }

    // Checks if prices are sorted from low to high.
    public boolean arePricesSortedAscending() {
        List<Double> prices = getProductPrices();
        List<Double> sorted = prices.stream().sorted().collect(Collectors.toList());
        return prices.equals(sorted);
    }

    // Checks if prices are sorted from high to low.
    public boolean arePricesSortedDescending() {
        List<Double> prices = getProductPrices();
        List<Double> sorted = prices.stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList());
        return prices.equals(sorted);
    }

    // Adds Backpack to the cart.
    @Step("Add Backpack to cart")
    public void addBackpackToCart() {
        page.locator(ADD_BACKPACK_BUTTON).click();
    }

    // Removes Backpack from the cart.
    @Step("Remove Backpack from cart")
    public void removeBackpackFromCart() {
        page.locator(REMOVE_BACKPACK_BUTTON).click();
    }

    // Tries to remove Backpack without adding it first.
    // Returns true when Remove button was available and clicked.
    @Step("Try removing Backpack without adding")
    public boolean tryRemoveBackpackWithoutAdding() {
        if (!page.locator(REMOVE_BACKPACK_BUTTON).isVisible()) {
            return false;
        }

        page.locator(REMOVE_BACKPACK_BUTTON).click();
        return true;
    }

    // If Remove button is visible, item is considered added.
    public boolean isBackpackAddedToCart() {
        return page.locator(REMOVE_BACKPACK_BUTTON).isVisible();
    }

    public boolean isBackpackReadyToAdd() {
        return page.locator(ADD_BACKPACK_BUTTON).isVisible()
                && page.locator(REMOVE_BACKPACK_BUTTON).count() == 0;
    }

    // Known anomaly for problem_user: Backpack may start as Remove without add.
    public boolean isBackpackInIncorrectDefaultState() {
        return page.locator(REMOVE_BACKPACK_BUTTON).isVisible()
                && page.locator(ADD_BACKPACK_BUTTON).count() == 0;
    }

    // Known anomaly for problem_user: inventory images may use sl-404 placeholder.
    public boolean areAllInventoryImagesUsingErrorPlaceholder() {
        String errorPlaceholder = testData.knownIndicator("imageErrorPlaceholder");
        List<String> imageSources = page.locator("img[data-test$='-img']").all().stream()
                .map(locator -> locator.getAttribute("src"))
                .filter(source -> source != null && !source.isBlank())
                .collect(Collectors.toList());

        return !imageSources.isEmpty() && imageSources.stream().allMatch(source -> source.contains(errorPlaceholder));
    }

    // Less strict check: confirms at least one broken inventory image.
    public boolean hasAnyInventoryImageUsingErrorPlaceholder() {
        String errorPlaceholder = testData.knownIndicator("imageErrorPlaceholder");
        return page.locator("img[data-test$='-img']").all().stream()
                .map(locator -> locator.getAttribute("src"))
                .filter(source -> source != null && !source.isBlank())
            .anyMatch(source -> source.contains(errorPlaceholder));
    }

    // Visual anomaly for visual_user: some product names may be misaligned.
    public boolean hasAnyProductNameWithMisalignment() {
        return page.locator("[data-test='inventory-item-name']." + testData.knownIndicator("visualNameMisalignmentClass"))
                .count() > 0;
    }

    // Visual anomaly for visual_user: some buttons may be misaligned.
    public boolean hasAnyButtonWithMisalignment() {
        return page.locator("button." + testData.knownIndicator("visualButtonMisalignmentClass")).count() > 0;
    }

    private HomeAnomalyResult analyzeCurrentHomeAnomalies() {
        boolean brokenImageIssue = hasAnyInventoryImageUsingErrorPlaceholder();
        boolean startedWithRemove = isBackpackAddedToCart();
        boolean removeDidNotSwitchToAdd = false;
        boolean addDidNotSwitchToRemove = false;

        if (startedWithRemove) {
            removeBackpackFromCart();
            removeDidNotSwitchToAdd = !isBackpackReadyToAdd();
        } else {
            addBackpackToCart();
            addDidNotSwitchToRemove = !isBackpackAddedToCart();
        }

        return new HomeAnomalyResult(
                brokenImageIssue,
                startedWithRemove,
                removeDidNotSwitchToAdd,
                addDidNotSwitchToRemove);
    }

    @Step("Analyze homepage anomalies for problem_user")
    public HomeAnomalyResult analyzeProblemUserHomeAnomalies() {
        return analyzeCurrentHomeAnomalies();
    }

    @Step("Analyze homepage anomalies for performance_glitch_user")
    public HomeAnomalyResult analyzePerformanceGlitchUserHomeAnomalies() {
        return analyzeCurrentHomeAnomalies();
    }

    @Step("Analyze homepage anomalies for error_user")
    public HomeAnomalyResult analyzeErrorUserHomeAnomalies() {
        return analyzeCurrentHomeAnomalies();
    }

    @Step("Analyze visual homepage anomalies for visual_user")
    public VisualUserHomeAnomalyResult analyzeVisualUserHomeAnomalies() {
        boolean textMisalignment = hasAnyProductNameWithMisalignment();
        boolean buttonMisalignment = hasAnyButtonWithMisalignment();
        
        return new VisualUserHomeAnomalyResult(textMisalignment, buttonMisalignment);
    }

    @Step("Analyze homepage anomalies and delay for performance_glitch_user")
    public PerformanceGlitchHomeAnomalyResult analyzePerformanceGlitchUserIssues(long loginDurationMs) {
        HomeAnomalyResult homeAnomalyResult = analyzePerformanceGlitchUserHomeAnomalies();
        return new PerformanceGlitchHomeAnomalyResult(
                homeAnomalyResult,
                loginDurationMs,
                testData.thresholdMs("performanceGlitchDelayMs"));
    }

    // Returns number of items in cart badge.
    // If badge does not exist, returns 0.
    public int getCartBadgeCount() {
        if (page.locator(CART_BADGE).count() == 0) {
            return 0;
        }
        return Integer.parseInt(page.locator(CART_BADGE).innerText().trim());
    }

    public boolean hasCartBadgeCount(int expectedCount) {
        return getCartBadgeCount() == expectedCount;
    }

    // Opens cart page.
    @Step("Open cart page")
    public void openCart() {
        page.locator(CART_LINK).click();
    }

    // Validates whether cart page is loaded.
    public boolean isCartPageLoaded() {
        return page.url().contains(testData.route("cart"))
                && testData.expected("cartTitle").equals(page.locator(TITLE).innerText().trim());
    }

    // Opens side hamburger menu.
    @Step("Open side menu")
    public void openMenu() {
        page.locator(OPEN_MENU).click();
    }

    // Performs logout from side menu.
    @Step("Logout from home")
    public void logout() {
        openMenu();
        page.locator(LOGOUT_SIDEBAR_LINK).click();
    }

    // Resets application state (clears cart/internal session state).
    @Step("Reset application state")
    public void resetAppState() {
        openMenu();
        page.locator(RESET_SIDEBAR_LINK).click();
    }

    // Checks presence of main homepage elements.
    public boolean hasMainHomeElements() {
        return page.locator(SORT_DROPDOWN).isVisible()
                && page.locator(CART_LINK).isVisible()
                && page.locator(OPEN_MENU).isVisible()
                && page.locator(FOOTER).isVisible();
    }
}
