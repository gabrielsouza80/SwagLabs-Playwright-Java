package com.playwright.java.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

// Classe responsável por centralizar as configurações do teste.
// Exemplo: URL, usuário, senha e modo headless.
public class TestConfig {
    // Valores finais usados durante os testes.
    private final String baseUrl;
    private final String username;
    private final String password;
    private final boolean headless;
    private final int viewportWidth;
    private final int viewportHeight;
    private final int defaultTimeoutMs;
    private final int navigationTimeoutMs;
    private final int slowMoMs;
    private final boolean screenshotOnTeardown;

    // Construtor privado: força a criação via método load().
    private TestConfig(
            String baseUrl,
            String username,
            String password,
            boolean headless,
            int viewportWidth,
            int viewportHeight,
            int defaultTimeoutMs,
            int navigationTimeoutMs,
            int slowMoMs,
            boolean screenshotOnTeardown) {
        this.baseUrl = baseUrl;
        this.username = username;
        this.password = password;
        this.headless = headless;
        this.viewportWidth = viewportWidth;
        this.viewportHeight = viewportHeight;
        this.defaultTimeoutMs = defaultTimeoutMs;
        this.navigationTimeoutMs = navigationTimeoutMs;
        this.slowMoMs = slowMoMs;
        this.screenshotOnTeardown = screenshotOnTeardown;
    }

    // Lê configurações do arquivo config.properties
    // e permite sobrescrever por parâmetros -D no Maven.
    public static TestConfig load() {
        // Estrutura que armazena pares chave=valor.
        Properties properties = new Properties();

        // Tenta ler o arquivo src/test/resources/config/config.properties do classpath.
        try (InputStream input = TestConfig.class.getClassLoader().getResourceAsStream("config/config.properties")) {
            if (input == null) {
                throw new IllegalStateException("config.properties was not found in test resources");
            }
            properties.load(input);
        } catch (IOException exception) {
            // Interrompe com mensagem clara se falhar ao ler o arquivo.
            throw new IllegalStateException("Failed to load config.properties", exception);
        }

        // Prioridade: parâmetro de sistema (-D) > arquivo properties.
        String baseUrl = readRequiredSetting("baseUrl", properties);
        String username = readRequiredSetting("username", properties);
        String password = readRequiredSetting("password", properties);
        boolean headless = Boolean.parseBoolean(readRequiredSetting("headless", properties));
        int viewportWidth = readOptionalIntSetting("viewportWidth", properties, 1280);
        int viewportHeight = readOptionalIntSetting("viewportHeight", properties, 720);
        int defaultTimeoutMs = readOptionalIntSetting("defaultTimeoutMs", properties, 15_000);
        int navigationTimeoutMs = readOptionalIntSetting("navigationTimeoutMs", properties, 30_000);
        int slowMoMs = readOptionalIntSetting("slowMoMs", properties, 0);
        boolean screenshotOnTeardown = readOptionalBooleanSetting("screenshotOnTeardown", properties, true);

        // Retorna um objeto imutável de configuração.
        return new TestConfig(
            baseUrl,
            username,
            password,
            headless,
            viewportWidth,
            viewportHeight,
            defaultTimeoutMs,
            navigationTimeoutMs,
            slowMoMs,
            screenshotOnTeardown);
    }

    // Getters no estilo Java moderno (nome curto).
    public String baseUrl() {
        return baseUrl;
    }

    public String username() {
        return username;
    }

    public String password() {
        return password;
    }

    public boolean headless() {
        return headless;
    }

    public int viewportWidth() {
        return viewportWidth;
    }

    public int viewportHeight() {
        return viewportHeight;
    }

    public int defaultTimeoutMs() {
        return defaultTimeoutMs;
    }

    public int navigationTimeoutMs() {
        return navigationTimeoutMs;
    }

    public int slowMoMs() {
        return slowMoMs;
    }

    public boolean screenshotOnTeardown() {
        return screenshotOnTeardown;
    }

    private static String readRequiredSetting(String key, Properties properties) {
        String value = System.getProperty(key);
        if (value == null || value.isBlank()) {
            value = properties.getProperty(key);
        }

        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Missing required configuration key: " + key);
        }

        return value.trim();
    }

    private static int readOptionalIntSetting(String key, Properties properties, int defaultValue) {
        String value = System.getProperty(key);
        if (value == null || value.isBlank()) {
            value = properties.getProperty(key);
        }

        if (value == null || value.isBlank()) {
            return defaultValue;
        }

        try {
            int parsed = Integer.parseInt(value.trim());
            if (parsed < 0) {
                throw new IllegalStateException("Configuration key must be >= 0: " + key);
            }
            return parsed;
        } catch (NumberFormatException exception) {
            throw new IllegalStateException("Invalid integer value for configuration key: " + key, exception);
        }
    }

    private static boolean readOptionalBooleanSetting(String key, Properties properties, boolean defaultValue) {
        String value = System.getProperty(key);
        if (value == null || value.isBlank()) {
            value = properties.getProperty(key);
        }

        if (value == null || value.isBlank()) {
            return defaultValue;
        }

        return Boolean.parseBoolean(value.trim());
    }
}
