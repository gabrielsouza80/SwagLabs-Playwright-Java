# Playwright Java SauceDemo

Web automation framework with Java 21, Playwright, JUnit 5, and Allure, focused on SauceDemo E2E tests.

## Overview

- Architecture based on Page Object Model (POM)
- Centralized configuration via file and system properties
- Execution by class, method, and tags (`includeTags` / `excludeTags`)
- Allure reports and automatic screenshots per test

## Technical stack

- Java 21
- Maven
- Playwright for Java
- JUnit Jupiter (JUnit 5)
- Allure Report

## Estrutura do projeto

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
        HomePage.java
        LoginPage.java
      tests/
        HomePageTest.java
        LoginPageTest.java
    resources/
      config/
        config.properties
      data/
        tests-data.json
```

## Prerequisites

- JDK 21 configured in the environment
- Maven available in the terminal
- Internet access on first run (dependency download)

## Configuration

Main file: `src/test/resources/config/config.properties`

```properties
baseUrl=https://www.saucedemo.com/
username=standard_user
password=secret_sauce
headless=true
```

Configuration resolution rules:

1. Value passed with Maven `-D`
2. Value present in `config.properties`

> Note: `baseUrl`, `username`, `password`, and `headless` keys are required.

Centralized test data:

- File: `src/test/resources/data/tests-data.json`
- Usage: expected messages, sorting options, alternative users, and other scenario values

## Test execution

Run full suite:

```bash
mvn test
```

Run with visible browser:

```bash
mvn test -Dheadless=false
```

Run one class:

```bash
mvn -Dtest=HomePageTest test
```

Run one method:

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

## Reports and evidence

After execution:

- Allure results: `target/allure-results`
- HTML report: `target/reports/allure-report/index.html`
- Screenshots: `target/reports/screenshots`

Generate and open local Allure report:

```bash
mvn allure:serve
```

## Pipeline and online report (GitHub Pages)

This repository includes a pipeline in [.github/workflows/allure-pages.yml](.github/workflows/allure-pages.yml) to:

- Run tests automatically in GitHub Actions
- Generate Allure report with history (trend between runs)
- Publish online report to branch `gh-pages`
- Generate Allure `single-file` version and upload it as an artifact
- Automatically comment on PRs with report and execution artifact links

Online report URL (after first successful pipeline run):

```text
https://<seu-usuario>.github.io/<seu-repositorio>/
```

Example for this project:

```text
https://gabrielsouza80.github.io/playwright-java-saucedemo/
```

Published single-file URL:

```text
https://gabrielsouza80.github.io/playwright-java-saucedemo/single-file/index.html
```

### How to enable on GitHub

1. Push the repository with the workflow.
2. Go to **Settings > Pages**.
3. Under **Build and deployment**, select **Deploy from a branch**.
4. Choose branch **gh-pages** and folder **/(root)**.

> Tip: in new repositories, branch `gh-pages` is created automatically after the first successful workflow run.

## Test conventions

- Cases identified by `TCxx` in `@DisplayName`
- Functional and execution tags (`home`, `login`, `smoke`, `cart`, `menu`, etc.)
- Common flows centralized in `BaseTest`
- Screen rules encapsulated in Page Objects

## Quick glossary

- CI (Continuous Integration): automatic test execution on every push/PR
- CD (Continuous Delivery): automatic report publishing (Allure on Pages)
- Single-file: Allure report in a single HTML file (good for sharing, can become heavy)

## Quick troubleshooting

- Missing configuration failure: validate `config.properties` and key names
- Empty Allure when opening static HTML directly: prefer `mvn allure:serve`
- Empty `Categories` widget: expected when there are no `failed`/`broken` tests (100% green suite)
- Visual glitches in VS Code after refactor: restart the Java Language Server
