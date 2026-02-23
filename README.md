# Playwright Java SauceDemo

Framework de automação web com Java 21, Playwright, JUnit 5 e Allure, focado em testes E2E do SauceDemo.

## Visão geral

- Arquitetura baseada em Page Object Model (POM)
- Configuração centralizada por arquivo e propriedades de sistema
- Execução por classe, método e tags (`includeTags` / `excludeTags`)
- Relatórios Allure e screenshots automáticos por teste

## Stack técnica

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
      pages/
        HomePage.java
        InventoryPage.java
        LoginPage.java
      tests/
        HomePageTest.java
        SauceDemoLoginTest.java
    resources/
      config.properties
```

## Pré-requisitos

- JDK 21 configurado no ambiente
- Maven disponível no terminal
- Acesso à internet na primeira execução (download de dependências)

## Configuração

Arquivo principal: `src/test/resources/config.properties`

```properties
baseUrl=https://www.saucedemo.com/
username=standard_user
password=secret_sauce
headless=true
```

Regras de leitura da configuração:

1. Valor informado por `-D` no Maven
2. Valor presente em `config.properties`

> Observação: as chaves `baseUrl`, `username`, `password` e `headless` são obrigatórias.

## Execução dos testes

Executar suíte completa:

```bash
mvn test
```

Executar com navegador visível:

```bash
mvn test -Dheadless=false
```

Executar uma classe:

```bash
mvn -Dtest=HomePageTest test
```

Executar um método:

```bash
mvn -Dtest=SauceDemoLoginTest#shouldLoginWithStandardUser test
```

Executar por tag:

```bash
mvn test -DincludeTags=smoke
```

Excluir tag:

```bash
mvn test -DexcludeTags=menu
```

## Relatórios e evidências

Após a execução:

- Resultado Allure: `target/allure-results`
- Relatório HTML: `target/reports/allure-report/index.html`
- Screenshots: `target/reports/screenshots`

Gerar e abrir relatório Allure local:

```bash
mvn allure:serve
```

## Convenções de testes

- Casos identificados por `TCxx` no `@DisplayName`
- Tags funcionais e de execução (`home`, `login`, `smoke`, `cart`, `menu`, etc.)
- Fluxos comuns centralizados em `BaseTest`
- Regras de tela encapsuladas em Page Objects

## Troubleshooting rápido

- Falha por configuração ausente: valide `config.properties` e nomes das chaves
- Allure em branco ao abrir HTML direto: prefira `mvn allure:serve`
- Erros visuais no VS Code após refactor: reinicie o Java Language Server
