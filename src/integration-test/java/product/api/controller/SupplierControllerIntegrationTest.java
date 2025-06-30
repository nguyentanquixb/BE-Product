package product.api.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import product.api.entity.Supplier;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc(addFilters = false)
@Testcontainers
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class SupplierControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Container
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("inventory-test-db")
            .withUsername("test")
            .withPassword("123456");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
        System.out.println("JDBC URL: " + postgres.getJdbcUrl());

    }

    @Test
    @WithMockUser(authorities = "CREATE_SUPPLIER")
    void shouldCreateSupplierSuccessfully() throws Exception {
        Supplier supplier = new Supplier();
        supplier.setName("Test Supplier");
        supplier.setContactInfo("test@example.com");
        supplier.setAddress("123 Test Street");
        supplier.setCreatedAt(LocalDateTime.now());
        supplier.setUpdatedAt(LocalDateTime.now());

        mockMvc.perform(post("/supplier")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(supplier)))
                .andExpect(status().isCreated());

    }

    @Test
    @WithMockUser(authorities = "VIEW_SUPPLIER")
    @Sql(scripts = {"/sql/insert-test-suppliers.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
//    @Sql(scripts = {"/sql/cleanup-test-suppliers.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getAllSuppliers_ShouldReturnListOfSuppliers() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/supplier")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value("Test Supplier 1"))
                .andExpect(jsonPath("$[0].contactInfo").value("test1@example.com"))
                .andExpect(jsonPath("$[1].name").value("Test Supplier 2"))
                .andExpect(jsonPath("$[1].contactInfo").value("test2@example.com"));
    }

}
