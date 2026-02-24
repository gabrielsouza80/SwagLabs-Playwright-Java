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
    private final long performanceGlitchDelayThresholdMs;

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
            // problem_user DEVE ter imagens quebradas (sl-404 placeholder)
            // OU começar com Backpack em estado Remove (sem poder adicionar)
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
        this.performanceGlitchDelayThresholdMs = testData.expectedInt("performanceGlitchDelayThresholdMs");
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

    // Clica em um produto específico pelo nome.
    @Step("Clicar no produto: {productName}")
    public void clickProductByName(String productName) {
        page.locator("[data-test='inventory-item-name']:has-text('" + productName + "')")
            .first()
            .click();
    }

    // Clica em um produto específico pelo data-test do item.
    @Step("Clicar no produto com ID: {itemDataTest}")
    public void clickProductByDataTest(String itemDataTest) {
        page.locator("[data-test='" + itemDataTest + "-img-link']").click();
    }

    @Step("Validar carregamento da página de detalhes do produto")
    public boolean isProductDetailsLoaded() {
        return page.url().contains("/inventory-item.html")
                && page.locator(PRODUCT_NAME).isVisible()
                && page.locator(PRODUCT_DESC).isVisible()
                && page.locator(PRODUCT_PRICE).isVisible();
    }

    @Step("Obter nome do produto nos detalhes")
    public String getProductDetailsName() {
        return page.locator(PRODUCT_NAME).innerText().trim();
    }

    @Step("Obter descrição do produto nos detalhes")
    public String getProductDetailsDescription() {
        return page.locator(PRODUCT_DESC).innerText().trim();
    }

    @Step("Obter preço do produto nos detalhes")
    public String getProductDetailsPrice() {
        return page.locator(PRODUCT_PRICE).innerText().trim();
    }

    @Step("Adicionar ao carrinho a partir de detalhes")
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

    @Step("Voltar dos detalhes para a listagem")
    public void backToProductsFromDetails() {
        page.locator(BACK_TO_PRODUCTS_BUTTON).click();
    }

    public boolean isBackButtonVisibleOnDetails() {
        return page.locator(BACK_TO_PRODUCTS_BUTTON).isVisible();
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

    // Tenta remover Backpack sem adicionar antes.
    // Retorna true quando o botão Remove estava disponível e foi clicado.
    @Step("Tentar remover Backpack sem adicionar")
    public boolean tryRemoveBackpackWithoutAdding() {
        if (!page.locator(REMOVE_BACKPACK_BUTTON).isVisible()) {
            return false;
        }

        page.locator(REMOVE_BACKPACK_BUTTON).click();
        return true;
    }

    // Se o botão Remove está visível, significa que o item foi adicionado.
    public boolean isBackpackAddedToCart() {
        return page.locator(REMOVE_BACKPACK_BUTTON).isVisible();
    }

    public boolean isBackpackReadyToAdd() {
        return page.locator(ADD_BACKPACK_BUTTON).isVisible()
                && page.locator(REMOVE_BACKPACK_BUTTON).count() == 0;
    }

    // Anomalia observada no problem_user: Backpack já aparece como Remove sem adicionar.
    public boolean isBackpackInIncorrectDefaultState() {
        return page.locator(REMOVE_BACKPACK_BUTTON).isVisible()
                && page.locator(ADD_BACKPACK_BUTTON).count() == 0;
    }

    // Anomalia observada no problem_user: imagens da lista usam placeholder sl-404.
    public boolean areAllInventoryImagesUsingErrorPlaceholder() {
        List<String> imageSources = page.locator("img[data-test$='-img']").all().stream()
                .map(locator -> locator.getAttribute("src"))
                .filter(source -> source != null && !source.isBlank())
                .collect(Collectors.toList());

        return !imageSources.isEmpty() && imageSources.stream().allMatch(source -> source.contains("sl-404"));
    }

    // Versão menos rígida: confirma pelo menos uma imagem quebrada no inventário.
    public boolean hasAnyInventoryImageUsingErrorPlaceholder() {
        return page.locator("img[data-test$='-img']").all().stream()
                .map(locator -> locator.getAttribute("src"))
                .filter(source -> source != null && !source.isBlank())
                .anyMatch(source -> source.contains("sl-404"));
    }

    // Anomalia visual do visual_user: alguns nomes de produtos estão com texto desalinhado.
    public boolean hasAnyProductNameWithMisalignment() {
        return page.locator("[data-test='inventory-item-name'].align_right").count() > 0;
    }

    // Anomalia visual do visual_user: alguns botões estão desalinhados visualmente.
    public boolean hasAnyButtonWithMisalignment() {
        return page.locator("button.btn_inventory_misaligned").count() > 0;
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

    @Step("Analisar anomalias da Home para problem_user")
    public HomeAnomalyResult analyzeProblemUserHomeAnomalies() {
        return analyzeCurrentHomeAnomalies();
    }

    @Step("Analisar anomalias da Home para performance_glitch_user")
    public HomeAnomalyResult analyzePerformanceGlitchUserHomeAnomalies() {
        return analyzeCurrentHomeAnomalies();
    }

    @Step("Analisar anomalias da Home para error_user")
    public HomeAnomalyResult analyzeErrorUserHomeAnomalies() {
        return analyzeCurrentHomeAnomalies();
    }

    @Step("Analisar anomalias visuais da Home para visual_user")
    public VisualUserHomeAnomalyResult analyzeVisualUserHomeAnomalies() {
        boolean textMisalignment = hasAnyProductNameWithMisalignment();
        boolean buttonMisalignment = hasAnyButtonWithMisalignment();
        
        return new VisualUserHomeAnomalyResult(textMisalignment, buttonMisalignment);
    }

    @Step("Analisar anomalias da Home e lentidão para performance_glitch_user")
    public PerformanceGlitchHomeAnomalyResult analyzePerformanceGlitchUserIssues(long loginDurationMs) {
        HomeAnomalyResult homeAnomalyResult = analyzePerformanceGlitchUserHomeAnomalies();
        return new PerformanceGlitchHomeAnomalyResult(
                homeAnomalyResult,
                loginDurationMs,
            performanceGlitchDelayThresholdMs);
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
