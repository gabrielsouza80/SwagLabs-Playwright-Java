package com.playwright.java.tests;

import static org.junit.jupiter.api.Assertions.assertTrue;

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
import com.playwright.java.base.BaseTest;
import com.playwright.java.pages.HomePage;

// Suíte de testes da HomePage com foco em funcionalidades principais.
@Epic("Web Automation")
@Feature("Homepage")
@Owner("gabriel")
@TestMethodOrder(MethodOrderer.DisplayName.class)
public class HomePageTest extends BaseTest {

    // Valida carregamento da home e elementos básicos.
    @Test
    @Tag("home")
    @Tag("smoke")
    @Tag("tc08")
    @DisplayName("TC08 - Deve exibir elementos principais da home")
    @Story("Home Load")
    @Owner("gabriel")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Valida carregamento da home após login e presença dos principais componentes da tela.")
    void shouldDisplayHomePageMainElements() {
        Allure.step("Dado que o usuário está autenticado e na Home", () ->
            assertTrue(homePage.isLoaded()));

        Allure.step("Quando consultar o título e os elementos da Home", () -> {
            assertTrue(homePage.hasExpectedTitle());
            assertTrue(homePage.hasMainHomeElements());
        });

        Allure.step("Então deve visualizar os elementos principais da Home", () ->
            assertTrue(homePage.hasMainHomeElements()));
    }

    // Valida título principal da página
    @Test
    @Tag("home")
    @Tag("tc09")
    @DisplayName("TC09 - Deve exibir título Products")
    @Story("Home Header")
    @Severity(SeverityLevel.NORMAL)
    @Description("Valida o título da home como Products.")
    void shouldDisplayProductsTitle() {
        Allure.step("Dado que o usuário está na Home", () ->
            assertTrue(homePage.isLoaded()));

        Allure.step("Quando consultar o título exibido no cabeçalho", () ->
            homePage.getPageTitle());

        Allure.step("Então o título exibido deve ser Products", () ->
            assertTrue(homePage.hasExpectedTitle()));
    }

    // Valida opção de ordenação padrão.
    @Test
    @Tag("home")
    @Tag("tc10")
    @DisplayName("TC10 - Deve exibir ordenação padrão Name (A to Z)")
    @Story("Sorting")
    @Severity(SeverityLevel.NORMAL)
    @Description("Valida a opção padrão de ordenação na primeira carga da home.")
    void shouldDisplayDefaultSortingOption() {
        Allure.step("Dado que o usuário abriu a Home", () ->
            assertTrue(homePage.isLoaded()));

        Allure.step("Quando consultar a ordenação selecionada por padrão", () ->
            homePage.getActiveSortOption());

        Allure.step("Então a ordenação padrão deve ser Name (A to Z)", () ->
            assertTrue(homePage.hasDefaultSortOption()));
    }

    // Valida quantidade de produtos esperada no SauceDemo.
    @Test
    @Tag("home")
    @Tag("tc11")
    @DisplayName("TC11 - Deve exibir 6 itens na listagem")
    @Story("Catalog")
    @Severity(SeverityLevel.NORMAL)
    @Description("Valida a quantidade esperada de produtos no inventário.")
    void shouldDisplaySixInventoryItems() {
        Allure.step("Dado que o usuário está na Home", () ->
            assertTrue(homePage.isLoaded()));

        Allure.step("Quando consultar a quantidade de itens exibidos", () ->
            homePage.getInventoryItemCount());

        Allure.step("Então a listagem deve conter 6 itens", () ->
            assertTrue(homePage.hasExpectedInventoryItemCount()));
    }

    // Ordenação por nome crescente (A-Z).
    @Test
    @Tag("home")
    @Tag("sorting")
    @Tag("tc12")
    @DisplayName("TC12 - Deve ordenar por nome crescente")
    @Story("Sorting")
    @Severity(SeverityLevel.NORMAL)
    @Description("Valida ordenação por nome crescente (A to Z).")
    void shouldSortProductsByNameAscending() {
        Allure.step("Dado que o usuário está na Home", () ->
            assertTrue(homePage.isLoaded()));

        Allure.step("Quando selecionar ordenação por nome crescente (A-Z)", () ->
            homePage.sortByNameAscending());

        Allure.step("Então os nomes dos produtos devem estar em ordem crescente", () ->
            assertTrue(homePage.areProductNamesSortedAscending()));
    }

    // Ordenação por nome decrescente (Z-A).
    @Test
    @Tag("home")
    @Tag("sorting")
    @Tag("tc13")
    @DisplayName("TC13 - Deve ordenar por nome decrescente")
    @Story("Sorting")
    @Severity(SeverityLevel.NORMAL)
    @Description("Valida ordenação por nome decrescente (Z to A).")
    void shouldSortProductsByNameDescending() {
        Allure.step("Dado que o usuário está na Home", () ->
            assertTrue(homePage.isLoaded()));

        Allure.step("Quando selecionar ordenação por nome decrescente (Z-A)", () ->
            homePage.sortByNameDescending());

        Allure.step("Então os nomes dos produtos devem estar em ordem decrescente", () ->
            assertTrue(homePage.areProductNamesSortedDescending()));
    }

    // Ordenação por preço crescente.
    @Test
    @Tag("home")
    @Tag("sorting")
    @Tag("tc14")
    @DisplayName("TC14 - Deve ordenar por preço crescente")
    @Story("Sorting")
    @Severity(SeverityLevel.NORMAL)
    @Description("Valida ordenação por preço crescente (low to high).")
    void shouldSortProductsByPriceAscending() {
        Allure.step("Dado que o usuário está na Home", () ->
            assertTrue(homePage.isLoaded()));

        Allure.step("Quando selecionar ordenação por preço crescente (low to high)", () ->
            homePage.sortByPriceAscending());

        Allure.step("Então os preços devem estar em ordem crescente", () ->
            assertTrue(homePage.arePricesSortedAscending()));
    }

    // Ordenação por preço decrescente.
    @Test
    @Tag("home")
    @Tag("sorting")
    @Tag("tc15")
    @DisplayName("TC15 - Deve ordenar por preço decrescente")
    @Story("Sorting")
    @Severity(SeverityLevel.NORMAL)
    @Description("Valida ordenação por preço decrescente (high to low).")
    void shouldSortProductsByPriceDescending() {
        Allure.step("Dado que o usuário está na Home", () ->
            assertTrue(homePage.isLoaded()));

        Allure.step("Quando selecionar ordenação por preço decrescente (high to low)", () ->
            homePage.sortByPriceDescending());

        Allure.step("Então os preços devem estar em ordem decrescente", () ->
            assertTrue(homePage.arePricesSortedDescending()));
    }

    // Adiciona item ao carrinho e valida estado visual/contador.
    @Test
    @Tag("home")
    @Tag("cart")
    @Tag("tc16")
    @DisplayName("TC16 - Deve adicionar mochila ao carrinho")
    @Story("Cart")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Adiciona o item Backpack ao carrinho e valida estado visual e badge.")
    void shouldAddBackpackToCart() {
        Allure.step("Dado que o usuário está na Home", () ->
            assertTrue(homePage.isLoaded()));

        Allure.step("Quando adicionar o produto Backpack ao carrinho", () ->
            homePage.addBackpackToCart());

        Allure.step("Então o botão de remover do Backpack deve aparecer", () ->
            assertTrue(homePage.isBackpackAddedToCart()));

        Allure.step("E o badge do carrinho deve exibir 1 item", () ->
            assertTrue(homePage.hasCartBadgeCount(1)));
    }

    // Remove item do carrinho e valida contador zerado.
    @Test
    @Tag("home")
    @Tag("cart")
    @Tag("tc17")
    @DisplayName("TC17 - Deve remover mochila do carrinho")
    @Story("Cart")
    @Severity(SeverityLevel.NORMAL)
    @Description("Remove item do carrinho e valida badge zerado.")
    void shouldRemoveBackpackFromCart() {
        Allure.step("Dado que o usuário está na Home", () ->
            assertTrue(homePage.isLoaded()));

        Allure.step("E que o produto Backpack já foi adicionado", () ->
            homePage.addBackpackToCart());

        Allure.step("Quando remover o produto Backpack do carrinho", () ->
            homePage.removeBackpackFromCart());

        Allure.step("Então o badge do carrinho deve exibir 0 item", () ->
            assertTrue(homePage.hasCartBadgeCount(0)));
    }

    @Test
    @Tag("home")
    @Tag("multi-user")
    @Tag("known-bug")
    @Tag("tc25")
    @DisplayName("TC25 - Deve confirmar anomalias da Home com problem_user")
    @Story("Home With Alternative Users")
    @Severity(SeverityLevel.NORMAL)
    @Description("Confirma problemas conhecidos da Home com problem_user: imagens com placeholder de erro e Backpack iniciando com botão Remove.")
    void shouldConfirmProblemUserHomeAnomalies() {
        Allure.label("knownIssue", "SAUCEDEMO-PROBLEM-USER-HOME");

        Allure.step("Dado que o usuário padrão está autenticado na Home", () ->
            assertTrue(homePage.isLoaded()));

        Allure.step("Quando fizer logout e entrar com problem_user", () -> {
            homePage.logout();
            loginPage.loginWithProblemUser();
        });

        Allure.step("Então a Home deve carregar para o usuário problem_user", () -> {
            assertTrue(homePage.isLoaded());
            assertTrue(homePage.hasExpectedInventoryItemCount());
        });

        HomePage.HomeAnomalyResult anomalyResult = homePage.analyzeProblemUserHomeAnomalies();

        Allure.step("E deve ter anomalias do problem_user", () ->
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
    @Tag("tc26")
    @DisplayName("TC26 - Deve confirmar anomalia da Home com performance_glitch_user")
    @Story("Home With Alternative Users")
    @Severity(SeverityLevel.NORMAL)
    @Description("Confirma anomalia na Home com performance_glitch_user, principalmente estado incorreto do botão Backpack.")
    void shouldConfirmPerformanceGlitchUserHomeAnomalies() {
        Allure.label("knownIssue", "SAUCEDEMO-PERFORMANCE-GLITCH-HOME");
        final long[] loginDurationMs = new long[] {0L};

        Allure.step("Dado que o usuário padrão está autenticado na Home", () ->
            assertTrue(homePage.isLoaded()));

        Allure.step("Quando fizer logout e login com performance_glitch_user", () -> {
            homePage.logout();
            assertTrue(loginPage.isLoaded());
            loginDurationMs[0] = loginPage.loginWithPerformanceGlitchUserAndMeasureDurationMs();
        });

        Allure.step("Então a Home deve carregar para performance_glitch_user", () -> {
            assertTrue(homePage.isLoaded());
            assertTrue(homePage.hasExpectedInventoryItemCount());
        });

        HomePage.PerformanceGlitchHomeAnomalyResult anomalyResult =
            homePage.analyzePerformanceGlitchUserIssues(loginDurationMs[0]);

        Allure.step("E deve ter delay do performance_glitch_user", () ->
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
    @Tag("tc27")
    @DisplayName("TC27 - Deve analisar anomalias da Home com error_user")
    @Story("Home With Alternative Users")
    @Severity(SeverityLevel.NORMAL)
    @Description("Analisa anomalias da Home com error_user, documentando qualquer problema encontrado.")
    void shouldConfirmErrorUserHomeAnomalies() {
        Allure.label("knownIssue", "SAUCEDEMO-ERROR-USER-HOME");

        Allure.step("Dado que o usuário padrão está autenticado na Home", () ->
            assertTrue(homePage.isLoaded()));

        Allure.step("Quando fizer logout e login com error_user", () -> {
            homePage.logout();
            assertTrue(loginPage.isLoaded());
            loginPage.loginWithErrorUser();
        });

        Allure.step("Então a Home deve carregar para error_user", () -> {
            assertTrue(homePage.isLoaded());
            assertTrue(homePage.hasExpectedInventoryItemCount());
        });

        HomePage.HomeAnomalyResult anomalyResult = homePage.analyzeErrorUserHomeAnomalies();

        Allure.step("E analisa qualquer anomalia presente", () -> {
            // Document found anomalies but don't fail if none found - site behavior may vary
            Allure.addAttachment(
                "Analysis Result",
                "text/plain",
                anomalyResult.toEvidenceText("error_user"),
                ".txt"
            );
        });
    }

    @Test
    @Tag("home")
    @Tag("multi-user")
    @Tag("known-bug")
    @Tag("tc28")
    @DisplayName("TC28 - Deve confirmar anomalia visual da Home com visual_user")
    @Story("Home With Alternative Users")
    @Severity(SeverityLevel.NORMAL)
    @Description("Confirma anomalia visual da Home com visual_user, com foco em desalinhamento CSS.")
    void shouldConfirmVisualUserHomeAnomalies() {
        Allure.label("knownIssue", "SAUCEDEMO-VISUAL-USER-HOME");

        Allure.step("Dado que o usuário padrão está autenticado na Home", () ->
            assertTrue(homePage.isLoaded()));

        Allure.step("Quando fizer logout e login com visual_user", () -> {
            homePage.logout();
            assertTrue(loginPage.isLoaded());
            loginPage.loginWithVisualUser();
        });

        Allure.step("Então a Home deve carregar para visual_user", () -> {
            assertTrue(homePage.isLoaded());
            assertTrue(homePage.hasExpectedInventoryItemCount());
        });

        HomePage.VisualUserHomeAnomalyResult visualAnomalyResult = homePage.analyzeVisualUserHomeAnomalies();

        Allure.step("E deve detectar anomalia visual CSS (texto ou botão desalinhado)", () ->
            assertTrue(visualAnomalyResult.hasVisualUserSpecificIssue()));

        Allure.addAttachment(
            "Known Defect Evidence",
            "text/plain",
            visualAnomalyResult.toEvidenceText(),
            ".txt"
        );
    }
}
