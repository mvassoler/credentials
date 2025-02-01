package br.com.mvassoler.credentials.configurations;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Supplier;

@ExtendWith(SpringExtension.class)
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(value = "test")
public class FunctionalBaseTest {

    @Container
    private static final PostgreSQLContainer<?> POSTGRES_CONTAINER = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("credentialstest")
            .withUsername("postgres")
            .withPassword("admin");

    @Autowired
    protected ObjectMapper objectMapper;
    protected MockMvc mvc;

    @Autowired
    private WebApplicationContext context;

    // 3. Configuração dinâmica das propriedades para o Spring
    @DynamicPropertySource
    static void setDatasourceProperties(DynamicPropertyRegistry registry) {
        // Pega os valores do contêiner e vincula às propriedades Spring
        registry.add("spring.datasource.url", POSTGRES_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES_CONTAINER::getUsername);
        registry.add("spring.datasource.password", POSTGRES_CONTAINER::getPassword);
    }

    @BeforeAll
    static void beforeAll() {
        POSTGRES_CONTAINER.start();
    }

    @AfterAll
    public static void encerrar() {
        POSTGRES_CONTAINER.stop();
    }

    @BeforeEach
    public void setup() {

        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .addFilter((request, response, chain) -> {
                    response.setCharacterEncoding("UTF-8"); // this is crucial
                    chain.doFilter(request, response);
                }, "/*")
                .build();
    }

    protected String getObjectAsString(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    protected <T> List<T> extractEntityListFromMvcResult(MvcResult result, Class<T> entityType, String parentKey, String listKey) throws IOException {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        String responseContent = result.getResponse().getContentAsString();
        JsonNode responseData = objectMapper.readTree(responseContent).path(parentKey);
        JavaType type = objectMapper.getTypeFactory().constructCollectionType(List.class, entityType);
        return objectMapper.readValue(responseData.path(listKey).traverse(), type);
    }

    protected <T> T extractEntityFromMvcResult(MvcResult result, Class<T> elementType, String elementKey) throws IOException {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        String responseContent = result.getResponse().getContentAsString();
        if (elementKey == null) {
            JavaType type = objectMapper.getTypeFactory().constructType(elementType);
            return objectMapper.readValue(responseContent, type);
        } else {
            JsonNode responseData = objectMapper.readTree(responseContent).path(elementKey);
            JavaType type = objectMapper.getTypeFactory().constructType(elementType);
            return objectMapper.readValue(responseData.toString(), type);
        }
    }

    protected <T> T extractEntityFromString(String result, Class<T> elementType, String elementKey) throws IOException {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        String responseContent = result;
        if (elementKey == null) {
            JavaType type = objectMapper.getTypeFactory().constructType(elementType);
            return objectMapper.readValue(responseContent, type);
        } else {
            JsonNode responseData = objectMapper.readTree(responseContent).path(elementKey);
            JavaType type = objectMapper.getTypeFactory().constructType(elementType);
            return objectMapper.readValue(responseData.toString(), type);
        }
    }

    protected <T> List<T> extractEntityListFromMvcResult(MvcResult result, Class<T> elementType) throws IOException {
        return extractEntityListFromMvcResult(result, elementType, "data", "list");
    }

    protected <T> T extractEntityFromMvcResult(MvcResult result, Class<T> elementType) throws IOException {
        return extractEntityFromMvcResult(result, elementType, null);
    }

    @SafeVarargs
    protected final <T> List<T> saveEntityList(int numberOfEntities,
                                               Supplier<T> entitySupplier,
                                               Consumer<List<T>> saveEntities,
                                               BiPredicate<List<T>, T>... shouldAddEntityParam) {
        BiPredicate<List<T>, T> shouldAddEntity = shouldAddEntityParam == null || shouldAddEntityParam.length != 1 ? (a, b) -> true : shouldAddEntityParam[0];
        List<T> entities = new LinkedList<>();
        for (int i = 0; i < numberOfEntities; i++) {
            T entity;
            do {
                entity = entitySupplier.get();
            } while (!shouldAddEntity.test(entities, entity));
            entities.add(entity);
        }
        saveEntities.accept(entities);
        return entities;
    }

    protected String getStringByFilter(String field) {
        return "?page=1&sortBy=" + field + "&sortDirection=ASC&" + field + "=";
    }
}
