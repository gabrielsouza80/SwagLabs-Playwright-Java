package com.playwright.java.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class TestData {
    private static final String TEST_DATA_FILE = "data/tests-data.json";
    private static final TestData INSTANCE = load();

    private final JsonNode root;

    private TestData(JsonNode root) {
        this.root = root;
    }

    public static TestData get() {
        return INSTANCE;
    }

    private static TestData load() {
        ObjectMapper mapper = new ObjectMapper();
        try (InputStream input = TestData.class.getClassLoader().getResourceAsStream(TEST_DATA_FILE)) {
            if (input == null) {
                throw new IllegalStateException("Test data file not found: " + TEST_DATA_FILE);
            }
            return new TestData(mapper.readTree(input));
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to load test data from " + TEST_DATA_FILE, exception);
        }
    }

    public String user(String key) {
        return requiredText("global", "users", key);
    }

    public List<String> allUsers() {
        JsonNode usersNode = requiredNode("global", "users");
        List<String> users = new ArrayList<>();
        Iterator<JsonNode> iterator = usersNode.elements();
        while (iterator.hasNext()) {
            users.add(iterator.next().asText());
        }
        return users;
    }

    public String password() {
        return requiredText("global", "password");
    }

    public String expected(String key) {
        return requiredText("global", "expected", key);
    }

    public int expectedInt(String key) {
        return requiredNode("global", "expected", key).asInt();
    }

    public String sortOption(String key) {
        return requiredText("global", "sortOptions", key);
    }

    public String error(String key) {
        return requiredText("global", "errors", key);
    }

    public String route(String key) {
        return requiredText("global", "routes", key);
    }

    public int thresholdMs(String key) {
        return requiredNode("global", "thresholds", key).asInt();
    }

    public String product(String productKey, String field) {
        return requiredText("global", "products", productKey, field);
    }

    public String knownIndicator(String key) {
        return requiredText("global", "knownIndicators", key);
    }

    public String knownIssue(String key) {
        return requiredText("global", "knownIssues", key);
    }

    public String message(String key) {
        return requiredText("global", "messages", key);
    }

    public String testValue(String testClass, String tcKey, String field) {
        return requiredText("tests", testClass, tcKey, field);
    }

    public int testValueInt(String testClass, String tcKey, String field) {
        return requiredNode("tests", testClass, tcKey, field).asInt();
    }

    private String requiredText(String... path) {
        JsonNode node = requiredNode(path);
        return node.asText();
    }

    private JsonNode requiredNode(String... path) {
        JsonNode current = root;
        for (String segment : path) {
            current = current.path(segment);
            if (current.isMissingNode()) {
                throw new IllegalStateException("Missing test data path: " + String.join(".", path));
            }
        }
        return current;
    }
}