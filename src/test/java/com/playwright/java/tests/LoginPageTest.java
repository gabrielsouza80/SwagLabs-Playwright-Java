package com.playwright.java.tests;

import static org.junit.jupiter.api.Assertions.assertFalse;
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
@Feature("Authentication")
@Owner("Gabriel Souza")
@TestMethodOrder(MethodOrderer.DisplayName.class)
public class LoginPageTest extends BaseTest {

    private final TestData testData = TestData.get();

    @Override
    protected boolean requiresAuthenticatedSession() {
        return false;
    }

    @Test
    @Tag("login")
    @Tag("smoke")
    @Tag("tc01")
    @DisplayName("TC01 - Should log in with standard user")
    @Story("Successful Login")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Validates that the standard user can authenticate successfully and access inventory.")
    void shouldLoginWithStandardUser() {
        Allure.step("Given the user opened the login screen", () ->
                assertTrue(loginPage.isLoaded()));

        Allure.step("When logging in with standard user", () ->
                loginPage.loginWithStandardUser());

        Allure.step("Then the user should access the inventory home", () ->
                assertTrue(homePage.isLoaded()));
    }

    @Test
    @Tag("login")
    @Tag("tc02")
    @DisplayName("TC02 - Should display logo and required login fields")
    @Story("Login Screen Layout")
    @Severity(SeverityLevel.NORMAL)
    @Description("Validates that login screen displays logo, username and password inputs, and login button.")
    void shouldDisplayLogoAndLoginFormElements() {
        Allure.step("Given the user opened the login page", () ->
                assertTrue(loginPage.isLoaded()));

        Allure.step("When validating logo, login button, and credentials panel", () -> {
            assertTrue(loginPage.isLogoVisible());
            assertTrue(loginPage.isUsernameInputVisible());
            assertTrue(loginPage.isPasswordInputVisible());
            assertTrue(loginPage.isLoginButtonVisible());
            assertTrue(loginPage.isCredentialsPanelVisible());
        });

        Allure.step("Then the login screen structure should be visible", () ->
                assertTrue(loginPage.isLoaded()));
    }

    @Test
    @Tag("login")
    @Tag("tc03")
    @DisplayName("TC03 - Should display accepted users and default password")
    @Story("Login Screen Content")
    @Severity(SeverityLevel.NORMAL)
    @Description("Validates accepted users text and password hint on the login screen.")
    void shouldDisplayAcceptedUsersAndPasswordHint() {
        Allure.step("Given the user is on the login screen", () ->
                assertTrue(loginPage.isLoaded()));

        Allure.step("When checking accepted users list", () ->
                assertTrue(loginPage.hasAllAcceptedUsers()));

        Allure.step("Then the default password hint should be displayed", () ->
                assertTrue(loginPage.hasDefaultPasswordHint()));
    }

    @Test
    @Tag("login")
    @Tag("negative")
    @Tag("tc04")
    @DisplayName("TC04 - Should display error when logging in without username")
    @Story("Login Validations")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Validates error message when authenticating without username.")
    void shouldShowErrorWhenUsernameIsEmpty() {
        Allure.step("Given the user is on the login screen", () ->
                assertTrue(loginPage.isLoaded()));

        Allure.step("When trying to login without username", () ->
                loginPage.tryLoginWithoutUsername());

        Allure.step("Then username required error should be displayed", () ->
                assertTrue(loginPage.hasErrorMessageContaining(testData.error("usernameRequired"))));

        Allure.step("And the user should not access inventory", () -> {
            assertTrue(loginPage.isLoaded());
            assertFalse(loginPage.isOnInventoryPage());
        });
    }

    @Test
    @Tag("login")
    @Tag("negative")
    @Tag("tc05")
    @DisplayName("TC05 - Should display error when logging in without password")
    @Story("Login Validations")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Validates error message when authenticating without password.")
    void shouldShowErrorWhenPasswordIsEmpty() {
        Allure.step("Given the user is on the login screen", () ->
                assertTrue(loginPage.isLoaded()));

        Allure.step("When trying to login without password", () ->
                loginPage.tryLoginWithoutPassword());

        Allure.step("Then password required error should be displayed", () ->
                assertTrue(loginPage.hasErrorMessageContaining(testData.error("passwordRequired"))));

        Allure.step("And the user should not access inventory", () -> {
            assertTrue(loginPage.isLoaded());
            assertFalse(loginPage.isOnInventoryPage());
        });
    }

    @Test
    @Tag("login")
    @Tag("negative")
    @Tag("tc06")
    @DisplayName("TC06 - Should display error for locked user")
    @Story("Login Validations")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Validates that locked user cannot authenticate and receives proper message.")
    void shouldShowErrorForLockedOutUser() {
        Allure.step("Given the user is on the login screen", () ->
                assertTrue(loginPage.isLoaded()));

        Allure.step("When trying to login with locked user", () ->
                loginPage.loginWithLockedOutUser());

        Allure.step("Then locked out error should be displayed", () ->
                assertTrue(loginPage.hasErrorMessageContaining(testData.error("lockedOut"))));

        Allure.step("And the user should not access inventory", () -> {
            assertTrue(loginPage.isLoaded());
            assertFalse(loginPage.isOnInventoryPage());
        });
    }

    @Test
    @Tag("login")
    @Tag("tc07")
    @DisplayName("TC07 - Should log in with performance_glitch_user")
    @Story("Alternative Valid Users")
    @Severity(SeverityLevel.NORMAL)
    @Description("Validates login with performance_glitch_user and access to inventory.")
    void shouldLoginWithPerformanceGlitchUser() {
        Allure.step("Given the user is on the login screen", () ->
                assertTrue(loginPage.isLoaded()));

        Allure.step("When logging in with performance_glitch_user", () ->
                loginPage.loginWithPerformanceGlitchUser());

        Allure.step("Then the user should access inventory", () ->
                assertTrue(homePage.isLoaded()));
    }

    @Test
    @Tag("login")
    @Tag("tc08")
    @DisplayName("TC08 - Should log in with problem_user")
    @Story("Alternative Valid Users")
    @Severity(SeverityLevel.NORMAL)
    @Description("Validates login with problem_user and access to inventory.")
    void shouldLoginWithProblemUser() {
        Allure.step("Given the user is on the login screen", () ->
                assertTrue(loginPage.isLoaded()));

        Allure.step("When logging in with problem_user", () ->
                loginPage.loginWithProblemUser());

        Allure.step("Then the user should access inventory", () ->
                assertTrue(homePage.isLoaded()));
    }

    @Test
    @Tag("login")
    @Tag("tc09")
    @DisplayName("TC09 - Should log in with error_user")
    @Story("Alternative Valid Users")
    @Severity(SeverityLevel.NORMAL)
    @Description("Validates login with error_user and access to inventory.")
    void shouldLoginWithErrorUser() {
        Allure.step("Given the user is on the login screen", () ->
                assertTrue(loginPage.isLoaded()));

        Allure.step("When logging in with error_user", () ->
                loginPage.loginWithErrorUser());

        Allure.step("Then the user should access inventory", () ->
                assertTrue(homePage.isLoaded()));
    }

    @Test
    @Tag("login")
    @Tag("tc10")
    @DisplayName("TC10 - Should log in with visual_user")
    @Story("Alternative Valid Users")
    @Severity(SeverityLevel.NORMAL)
    @Description("Validates login with visual_user and access to inventory.")
    void shouldLoginWithVisualUser() {
        Allure.step("Given the user is on the login screen", () ->
                assertTrue(loginPage.isLoaded()));

        Allure.step("When logging in with visual_user", () ->
                loginPage.loginWithVisualUser());

        Allure.step("Then the user should access inventory", () ->
                assertTrue(homePage.isLoaded()));
    }
}
