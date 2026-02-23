package pt.com.gabriel.config;

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

    // Construtor privado: força a criação via método load().
    private TestConfig(String baseUrl, String username, String password, boolean headless) {
        this.baseUrl = baseUrl;
        this.username = username;
        this.password = password;
        this.headless = headless;
    }

    // Lê configurações do arquivo config.properties
    // e permite sobrescrever por parâmetros -D no Maven.
    public static TestConfig load() {
        // Estrutura que armazena pares chave=valor.
        Properties properties = new Properties();

        // Tenta ler o arquivo src/test/resources/config.properties do classpath.
        try (InputStream input = TestConfig.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input != null) {
                properties.load(input);
            }
        } catch (IOException exception) {
            // Interrompe com mensagem clara se falhar ao ler o arquivo.
            throw new IllegalStateException("Failed to load config.properties", exception);
        }

        // Prioridade: parâmetro de sistema (-D) > arquivo properties > valor default.
        String baseUrl = System.getProperty("baseUrl", properties.getProperty("baseUrl", "https://www.saucedemo.com/"));
        String username = System.getProperty("username", properties.getProperty("username", "standard_user"));
        String password = System.getProperty("password", properties.getProperty("password", "secret_sauce"));
        boolean headless = Boolean.parseBoolean(System.getProperty("headless", properties.getProperty("headless", "true")));

        // Retorna um objeto imutável de configuração.
        return new TestConfig(baseUrl, username, password, headless);
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
}
