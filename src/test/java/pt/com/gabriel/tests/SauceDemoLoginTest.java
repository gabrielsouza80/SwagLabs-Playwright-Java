package pt.com.gabriel.tests;

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
import pt.com.gabriel.base.BaseTest;

// Classe de teste focada em validar se o login padrão funciona.
@Epic("Web Automation")
@Feature("Authentication")
public class SauceDemoLoginTest extends BaseTest {

    // Caso de teste: após o setup (que já loga), deve estar na home de inventário.
    @Test
    @Tag("login")
    @Tag("smoke")
    @Tag("tc01")
    @DisplayName("TC01 - Deve realizar login com usuário padrão")
    @Story("Successful Login")
    @Owner("gabriel")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Valida que o usuário padrão consegue autenticar e acessar a área de inventário.")
    void shouldLoginWithStandardUser() {
        assertTrue(inventoryPage.isLoaded());
    }
}
