package product.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import product.api.entity.*;
import product.api.repository.CategoryRepository;
import product.api.repository.ProductRepository;
import product.api.repository.SupplierRepository;
import product.api.repository.WarehouseRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc(addFilters = false)
@Testcontainers
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class InventoryControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private WarehouseRepository warehouseRepository;


    @Container
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("inventory-test-db")
            .withUsername("test")
            .withPassword("123456");
    @Autowired
    private ProductRepository productRepository;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
        System.out.println("JDBC URL: " + postgres.getJdbcUrl());
    }

    private Product createTestProduct(boolean lowStock){

        Supplier supplier = new Supplier();
        supplier.setName("Test Supplier");
        supplier.setAddress("Address");
        supplier.setContactInfo("test@gmail.com");
        supplier.setCreatedAt(LocalDateTime.now());
        supplier.setUpdatedAt(LocalDateTime.now());
        supplier = supplierRepository.save(supplier);

        Warehouse warehouse = new Warehouse();
        warehouse.setName("Test Warehouse");
        warehouse.setLocation("Location");
        warehouse.setCapacity(100);
        warehouse.setCreatedAt(LocalDateTime.now());
        warehouse.setUpdatedAt(LocalDateTime.now());
        warehouse = warehouseRepository.save(warehouse);

        Category category = new Category();
        category.setName("Test Category");
        category.setDescription("Test description");
        category.setCreatedAt(LocalDateTime.now());
        category.setUpdatedAt(LocalDateTime.now());
        category = categoryRepository.save(category);

        Product product = new Product();
        product.setName("Test Product");
        product.setDescription("Test");
        product.setPrice(BigDecimal.valueOf(100));
        product.setQuantity(lowStock ? 2 : 50);
        product.setMinStock(5);
        product.setUnit("kg");
        product.setProductCode("1232");
        product.setBarcode("123");
        product.setStatus(ProductStatusEnum.ACTIVE);
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());
        product.setSupplier(supplier);
        product.setCategory(category);
        product.setWarehouse(warehouse);

        return productRepository.save(product);
    }

    @Test
    void testGetProductsByWarehouse() throws Exception {
        Product product = createTestProduct(false);

        mockMvc.perform(get("/inventory")
                        .param("warehouseId", String.valueOf(product.getWarehouse().getId()))
                        .param("status", "ACTIVE"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetLowStockProducts() throws Exception {
        createTestProduct(true);

        mockMvc.perform(get("/inventory/low-stock"))
                .andExpect(status().isOk());
    }
}
