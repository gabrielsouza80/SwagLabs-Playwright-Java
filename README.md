# Playwright Java SauceDemo (Guia de Estudo)

Este projeto foi organizado para você aprender **Java + Playwright** de forma progressiva.

> Projeto **100% focado em automação de testes** (sem código de aplicação em `src/main`).

## Estrutura do projeto

- `src/test/java/pt/com/gabriel/base/BaseTest.java`
  - Setup de suíte e setup por teste (`@BeforeAll`, `@BeforeEach`, `@AfterEach`, `@AfterAll`)
  - Inicializa Playwright/browser e cria contexto isolado por teste
  - Login global uma vez e reaproveitamento de sessão autenticada

- `src/test/java/pt/com/gabriel/config/TestConfig.java`
  - Lê configurações de `src/test/resources/config.properties`
  - Permite sobrescrever por parâmetro `-D`

- `src/test/java/pt/com/gabriel/pages/*.java`
  - Page Objects (uma classe por página)
  - Métodos de ação e validação da UI

- `src/test/java/pt/com/gabriel/tests/*.java`
  - Casos de teste (`@Test`)
  - Orquestram fluxo usando os Page Objects

- `src/test/resources/config.properties`
  - URL, usuário, senha, modo headless

---

## Como executar os testes

Na raiz do projeto:

```bash
mvn test
```

Com navegador visível:

```bash
mvn test -Dheadless=false
```

Rodar apenas uma classe de teste:

```bash
mvn -Dtest=HomePageTest test
```

Rodar apenas o primeiro teste (TC01):

```bash
mvn -Dtest=SauceDemoLoginTest#shouldLoginWithStandardUser test
```

Rodar por tag (ex.: smoke):

```bash
mvn test -DincludeTags=smoke
```

Excluir uma tag (ex.: menu):

```bash
mvn test -DexcludeTags=menu
```

Gerar testes + relatório final HTML (Allure) em um comando:

```bash
mvn test
```

Abrir relatório gerado em:

```text
target/reports/allure-report/index.html
```

Screenshots tirados no tearDown ficam em:

```text
target/reports/screenshots
```

Observação: a cada nova execução, os resultados e o relatório anterior são apagados e substituídos pelo atual.

---

## Ordem de aprendizado recomendada

1. **BaseTest**
   - Entenda ciclo de vida do JUnit (`@BeforeEach`, `@AfterEach`)
   - Entenda como browser/context/page são criados

2. **LoginPage**
   - Veja como um Page Object usa seletores (`data-test`)
   - Entenda `open()`, `isLoaded()`, `login()`

3. **HomePage**
   - Entenda validações (`isLoaded`, título, contagem)
   - Entenda ações (sort, carrinho, menu, logout)

4. **Testes**
   - `SauceDemoLoginTest`: teste simples de login
   - `HomePageTest`: cenários principais da home

---

## Como pensar em testes (modelo simples)

Para cada funcionalidade, siga:

1. **Pré-condição** (ex.: usuário logado)
2. **Ação** (ex.: clicar em Add to cart)
3. **Validação** (ex.: badge do carrinho = 1)

---

## Exercícios práticos (para evoluir)

1. Criar teste de **login inválido** e validar mensagem de erro
2. Criar método na HomePage para adicionar **2 produtos diferentes**
3. Validar que o carrinho mostra os itens corretos
4. Criar teste de **logout** e validar retorno para login
5. Criar tags de suíte (`smoke`, `regression`) com JUnit

---

## Dicas para quem vem de Robot Framework

- Em Robot você chama keywords; em Java você chama **métodos**.
- `BaseTest` é o equivalente ao setup/teardown global por teste.
- `Page Object` é onde ficam os “keywords” da página.
- Teste deve ser legível: poucas linhas, foco em regra de negócio.

---

## Configuração de ambiente

Arquivo: `src/test/resources/config.properties`

Exemplo:

```properties
baseUrl=https://www.saucedemo.com/
username=standard_user
password=secret_sauce
headless=true
```

Você pode sobrescrever via Maven:

```bash
mvn test -Dheadless=false -Dusername=standard_user -Dpassword=secret_sauce
```
