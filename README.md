# SwagLabs Java Playwright

End-to-end web test automation framework for SauceDemo, built with Java 21, Playwright, JUnit 5, and Allure.

- Repository: https://github.com/gabrielsouza80/playwright-java-saucedemo
- Online Allure report: https://gabrielsouza80.github.io/playwright-java-saucedemo/
- Single-file Allure report: https://gabrielsouza80.github.io/playwright-java-saucedemo/single-file/index.html

## Table of contents

- [Overview](#overview)
- [Technology stack](#technology-stack)
- [Architecture and design](#architecture-and-design)
- [Project structure](#project-structure)
- [Prerequisites](#prerequisites)
- [Configuration](#configuration)
- [Test data strategy](#test-data-strategy)
- [How to run tests](#how-to-run-tests)
- [Reports and evidence](#reports-and-evidence)
- [VS Code tasks](#vs-code-tasks)
- [CI/CD and GitHub Pages](#cicd-and-github-pages)
- [Conventions](#conventions)
- [Troubleshooting](#troubleshooting)

## Overview

This project validates key SauceDemo user journeys through maintainable, deterministic E2E tests.

Primary goals:

- Keep tests readable with Page Object Model (POM)
- Centralize runtime configuration and test data
- Support flexible execution by class, method, and tags
- Produce rich execution evidence with Allure and screenshots

## Technology stack

- Java 21
- Maven
- Playwright for Java
- JUnit Jupiter (JUnit 5)
- Allure Report

## Architecture and design

The framework follows a layered test architecture:

- `base`: shared test lifecycle, browser context handling, common setup/teardown
- `pages`: UI interaction layer (selectors + business actions)
- `tests`: scenario layer (assertions and test intent)
- `config`: configuration and externalized test data loading

Core design decisions:

- **POM-first approach** to reduce selector duplication
- **Single source of truth** for configuration and expected values
- **Per-test isolation** via context lifecycle to avoid state leakage
- **Traceable execution** through Allure labels, steps, and attachments

## Project structure

```text
src/
  test/
    java/com/playwright/java/
      base/
        BaseTest.java
      config/
        TestConfig.java
        TestData.java
      pages/
        ComponentsPage.java
        HomePage.java
        LoginPage.java
      tests/
        ComponentsTest.java
        HomePageTest.java
        LoginPageTest.java
    resources/
      config/
        config.properties
      data/
        tests-data.json
```

## Prerequisites

- JDK 21 installed and configured
- Maven available in terminal (`mvn -v`)
- Internet access on first execution (dependency download)

## Configuration

Primary configuration file:

- `src/test/resources/config/config.properties`

Example:

```properties
baseUrl=https://www.saucedemo.com/
username=standard_user
password=secret_sauce
headless=true
```

Resolution order:

1. Maven system properties (`-Dkey=value`)
2. `config.properties`

Required keys:

- `baseUrl`
- `username`
- `password`
- `headless`

## Test data strategy

Test data is centralized in:

- `src/test/resources/data/tests-data.json`

Typical contents:

- Expected messages and labels
- Product values (name, price, description)
- Route fragments and threshold values
- Scenario-specific values by test case (`TCxx`)

Benefits:

- Easier maintenance during UI/content changes
- Lower code churn in test classes
- Better separation of test intent and raw data

## How to run tests

Run the complete suite:

```bash
mvn test
```

Run in headed mode:

```bash
mvn test -Dheadless=false
```

Run a single class:

```bash
mvn -Dtest=HomePageTest test
```

Run a single method:

```bash
mvn -Dtest=LoginPageTest#shouldLoginWithStandardUser test
```

Run by tag:

```bash
mvn test -DincludeTags=smoke
```

Exclude tag:

```bash
mvn test -DexcludeTags=menu
```

Compile and refresh classpath without executing tests:

```bash
mvn clean test -DskipTests
```

## Reports and evidence

After execution:

- Allure raw results: `target/allure-results`
- Allure HTML report: `target/reports/allure-report/index.html`
- Screenshots: `target/reports/screenshots`

Generate and serve report locally:

```bash
mvn allure:serve
```

## VS Code tasks

This repository includes ready-to-run VS Code tasks in `.vscode/tasks.json`:

- `Java Refresh (clean + testCompile)` → `mvn clean test -DskipTests`
- `Allure Serve` → `mvn allure:serve`

Use via:

- **Terminal > Run Task...**
- Or **Command Palette > Tasks: Run Task**

## CI/CD and GitHub Pages

Workflow file:

- `.github/workflows/allure-pages.yml`

Pipeline responsibilities:

- Execute tests automatically on CI
- Generate Allure report with history/trend
- Publish report to `gh-pages`
- Build and upload a single-file report artifact
- Comment on pull requests with report links

GitHub Pages setup:

1. Push repository with workflow enabled.
2. Go to **Settings > Pages**.
3. In **Build and deployment**, choose **Deploy from a branch**.
4. Select branch **gh-pages** and folder **/(root)**.

## Conventions

- Test case IDs use `TCxx` in `@DisplayName`
- Functional and execution tags include `home`, `login`, `smoke`, `cart`, `menu`
- Shared lifecycle and reusable flows are centralized in `BaseTest`
- UI behavior and selectors are encapsulated in Page Objects

## Troubleshooting

If tests fail due to configuration:

- Verify key names and values in `config.properties`
- Confirm `-D` overrides are valid and correctly typed

If IDE shows false red errors after refactor/branch switch:

1. Run `mvn clean test -DskipTests`
2. Run **Java: Clean Java Language Server Workspace**
3. Reload VS Code window

If Allure report appears empty:

- Prefer `mvn allure:serve` over opening static HTML directly
- Confirm that `target/allure-results` contains fresh execution files

If `Categories` widget is empty:

- This is expected when the suite has no `failed` or `broken` tests
