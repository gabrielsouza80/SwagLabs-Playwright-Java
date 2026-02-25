package com.playwright.java.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

// Class responsible for centralizing test configuration.
// Example: URL, user, password, and headless mode.
public class TestConfig {
    // Final values used during test execution.
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

    // Private constructor: forces creation through load().
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

    // Reads configuration from config.properties
    // and allows overriding with Maven -D parameters.
    public static TestConfig load() {
        // Structure that stores key=value pairs.
        Properties properties = new Properties();

        // Tries to read src/test/resources/config/config.properties from classpath.
        try (InputStream input = TestConfig.class.getClassLoader().getResourceAsStream("config/config.properties")) {
            if (input == null) {
                throw new IllegalStateException("config.properties was not found in test resources");
            }
            properties.load(input);
        } catch (IOException exception) {
            // Stops execution with a clear message if file loading fails.
            throw new IllegalStateException("Failed to load config.properties", exception);
        }

        // Priority: system parameter (-D) > properties file.
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

        // Returns an immutable configuration object.
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

    // Getters in modern Java style (short names).
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
