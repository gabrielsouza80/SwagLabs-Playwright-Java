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
        TestData.java
      pages/
        HomePage.java
        InventoryPage.java
        LoginPage.java
      tests/
        HomePageTest.java
        LoginTest.java
    resources/
      config/
        config.properties
      data/
        tests-data.json
```

## Pré-requisitos

- JDK 21 configurado no ambiente
- Maven disponível no terminal
- Acesso à internet na primeira execução (download de dependências)

## Configuração

Arquivo principal: `src/test/resources/config/config.properties`

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

Dados de teste centralizados:

- Arquivo: `src/test/resources/data/tests-data.json`
- Uso: mensagens esperadas, opções de sort, usuários alternativos e demais valores de cenário

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
mvn -Dtest=LoginTest#shouldLoginWithStandardUser test
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

## Pipeline e relatório online (GitHub Pages)

Este repositório possui pipeline em [.github/workflows/allure-pages.yml](.github/workflows/allure-pages.yml) para:

- Executar testes automaticamente no GitHub Actions
- Gerar Allure report com histórico (Trend entre execuções)
- Publicar relatório online na branch `gh-pages`
- Gerar versão `single-file` do Allure e anexar como artifact
- Comentar automaticamente no PR com links do relatório e artefatos da execução

URL do relatório online (após primeira execução da pipeline):

```text
https://<seu-usuario>.github.io/<seu-repositorio>/
```

Exemplo para este projeto:

```text
https://gabrielsouza80.github.io/playwright-java-saucedemo/
```

Arquivo single-file publicado:

```text
https://gabrielsouza80.github.io/playwright-java-saucedemo/single-file/index.html
```

### Como habilitar no GitHub

1. Faça push do repositório com o workflow.
2. Vá em **Settings > Pages**.
3. Em **Build and deployment**, selecione **Deploy from a branch**.
4. Escolha a branch **gh-pages** e a pasta **/(root)**.

> Dica: em repositórios novos, a branch `gh-pages` é criada automaticamente após a primeira execução bem-sucedida do workflow.

## Convenções de testes

- Casos identificados por `TCxx` no `@DisplayName`
- Tags funcionais e de execução (`home`, `login`, `smoke`, `cart`, `menu`, etc.)
- Fluxos comuns centralizados em `BaseTest`
- Regras de tela encapsuladas em Page Objects

## Glossário rápido

- CI (Continuous Integration): execução automática de testes a cada push/PR
- CD (Continuous Delivery): publicação automática do relatório (Allure no Pages)
- Single-file: relatório Allure em um único HTML (bom para compartilhar, pode ficar pesado)

## Troubleshooting rápido

- Falha por configuração ausente: valide `config.properties` e nomes das chaves
- Allure em branco ao abrir HTML direto: prefira `mvn allure:serve`
- Widget `Categories` vazio: esperado quando não existem testes `failed`/`broken` (suíte 100% verde)
- Erros visuais no VS Code após refactor: reinicie o Java Language Server
