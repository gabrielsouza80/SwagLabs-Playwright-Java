package com.playwright.java.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import com.playwright.java.base.BaseTest;

// Classe de teste focada em validar se o login padrão funciona.
@Epic("Web Automation")
@Feature("Authentication")
@Owner("gabriel")
public class SauceDemoLoginTest extends BaseTest {

    private static final String INVENTORY_TITLE = "Products";

    @Test
    @Tag("login")
    @Tag("smoke")
    @Tag("tc01")
    @DisplayName("TC01 - Deve realizar login com usuário padrão")
    @Story("Successful Login")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Valida que o usuário padrão autentica com sucesso e visualiza a home de inventário.")
    void shouldLoginWithStandardUser() {
        assertTrue(inventoryPage.isLoaded());
        assertTrue(homePage.isLoaded());
        assertEquals(INVENTORY_TITLE, homePage.getPageTitle());
    }
}
