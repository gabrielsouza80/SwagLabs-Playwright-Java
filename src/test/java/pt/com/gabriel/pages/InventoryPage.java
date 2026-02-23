package pt.com.gabriel.pages;

import com.microsoft.playwright.Page;

// Page Object da tela de inventário (home logada do SauceDemo).
public class InventoryPage {
    // Referência da aba atual.
    private final Page page;

    // Seletor principal da lista de produtos.
    private static final String INVENTORY_LIST = ".inventory_list";

    public InventoryPage(Page page) {
        this.page = page;
    }

    // Considera a página carregada quando:
    // 1) URL contém /inventory.html
    // 2) lista de itens está visível
    public boolean isLoaded() {
        return page.url().contains("/inventory.html")
                && page.locator(INVENTORY_LIST).isVisible();
    }
}
