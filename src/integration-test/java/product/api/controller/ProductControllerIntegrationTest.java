package product.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.MediaType;
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
import product.api.dto.ProductRequest;
import product.api.entity.*;
import product.api.repository.CategoryRepository;
import product.api.repository.ProductRepository;
import product.api.repository.SupplierRepository;
import product.api.repository.WarehouseRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc(addFilters = false)
@Testcontainers
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class ProductControllerIntegrationTest {

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

    @Test
    void shouldCreateProduct() throws Exception {

        Supplier supplier = new Supplier();
        supplier.setName("Test Supplier");
        supplier.setAddress("123");
        supplier.setCreatedAt(LocalDateTime.now());
        supplier.setUpdatedAt(LocalDateTime.now());
        supplier.setContactInfo("test");
        supplier = supplierRepository.save(supplier);

        Warehouse warehouse = new Warehouse();
        warehouse.setName("Test Warehouse");
        warehouse.setCreatedAt(LocalDateTime.now());
        warehouse.setUpdatedAt(LocalDateTime.now());
        warehouse.setCapacity(1);
        warehouse.setLocation("123");
        warehouse = warehouseRepository.save(warehouse);

        Category category = new Category();
        category.setName("Test Category");
        category.setCreatedAt(LocalDateTime.now());
        category.setDescription("1232");
        category.setUpdatedAt(LocalDateTime.now());
        category = categoryRepository.save(category);

        ProductRequest product = new ProductRequest();
        product.setName("Test Product");
        product.setDescription("Test Product");
        product.setPrice(BigDecimal.valueOf(1000));
        product.setQuantity(10);
        product.setSupplierId(supplier.getId());
        product.setWarehouseId(warehouse.getId());
        product.setCategoryId(category.getId());
        product.setUnit("kg");
        product.setMinStock(10);
        product.setProductCode("2131");
        product.setStatus(ProductStatusEnum.ACTIVE);
        product.setBarcode("121");
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());


        mockMvc.perform(post("/product/create")
                .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isCreated());

    }

    @Test
    void shouldUpdateProduct() throws Exception {

        Supplier supplier = new Supplier();
        supplier.setName("Test Supplier");
        supplier.setAddress("123");
        supplier.setCreatedAt(LocalDateTime.now());
        supplier.setUpdatedAt(LocalDateTime.now());
        supplier.setContactInfo("test");
        supplier = supplierRepository.save(supplier);

        Warehouse warehouse = new Warehouse();
        warehouse.setName("Test Warehouse");
        warehouse.setCreatedAt(LocalDateTime.now());
        warehouse.setUpdatedAt(LocalDateTime.now());
        warehouse.setCapacity(1);
        warehouse.setLocation("123");
        warehouse = warehouseRepository.save(warehouse);

        Category category = new Category();
        category.setName("Test Category");
        category.setCreatedAt(LocalDateTime.now());
        category.setDescription("1232");
        category.setUpdatedAt(LocalDateTime.now());
        category = categoryRepository.save(category);

        Product product = new Product();
        product.setName("Test Product");
        product.setDescription("Test Product");
        product.setPrice(BigDecimal.valueOf(1000));
        product.setQuantity(10);
        product.setSupplier(supplier);
        product.setWarehouse(warehouse);
        product.setCategory(category);
        product.setUnit("kg");
        product.setMinStock(10);
        product.setProductCode("2131");
        product.setStatus(ProductStatusEnum.ACTIVE);
        product.setBarcode("121");
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());

        product = productRepository.save(product);

        ProductRequest updateRequest = new ProductRequest();
        updateRequest.setName("Updated Product");
        updateRequest.setDescription("Updated Description");
        updateRequest.setPrice(BigDecimal.valueOf(1500));
        updateRequest.setQuantity(20);
        updateRequest.setSupplierId(supplier.getId());
        updateRequest.setWarehouseId(warehouse.getId());
        updateRequest.setCategoryId(category.getId());
        updateRequest.setUnit("box");
        updateRequest.setMinStock(5);
        updateRequest.setProductCode("111");
        updateRequest.setStatus(ProductStatusEnum.INACTIVE);
        updateRequest.setBarcode("111");
        updateRequest.setCreatedAt(product.getCreatedAt());
        updateRequest.setUpdatedAt(LocalDateTime.now());

        mockMvc.perform(put("/product/update/"+ product.getId())
                .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                .content(objectMapper.writeValueAsString(updateRequest))
        ).andExpect(status().isOk());
    }

    @Test
    void testDeleteProduct() throws Exception {

        Supplier supplier = new Supplier();
        supplier.setName("Test Supplier");
        supplier.setAddress("123");
        supplier.setCreatedAt(LocalDateTime.now());
        supplier.setUpdatedAt(LocalDateTime.now());
        supplier.setContactInfo("test");
        supplier = supplierRepository.save(supplier);

        Warehouse warehouse = new Warehouse();
        warehouse.setName("Test Warehouse");
        warehouse.setCreatedAt(LocalDateTime.now());
        warehouse.setUpdatedAt(LocalDateTime.now());
        warehouse.setCapacity(1);
        warehouse.setLocation("123");
        warehouse = warehouseRepository.save(warehouse);

        Category category = new Category();
        category.setName("Test Category");
        category.setCreatedAt(LocalDateTime.now());
        category.setDescription("1232");
        category.setUpdatedAt(LocalDateTime.now());
        category = categoryRepository.save(category);

        Product product = new Product();
        product.setName("Test Product");
        product.setDescription("Test Product");
        product.setPrice(BigDecimal.valueOf(1000));
        product.setQuantity(10);
        product.setSupplier(supplier);
        product.setWarehouse(warehouse);
        product.setCategory(category);
        product.setUnit("kg");
        product.setMinStock(10);
        product.setProductCode("2131");
        product.setStatus(ProductStatusEnum.ACTIVE);
        product.setBarcode("121");
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());

        product = productRepository.save(product);

        mockMvc.perform(delete("/product/delete/"+ product.getId())
                .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isOk());
    }

    @Test
    void testSearchProductName() throws Exception {

        Supplier supplier = new Supplier();
        supplier.setName("Test Supplier");
        supplier.setAddress("123");
        supplier.setCreatedAt(LocalDateTime.now());
        supplier.setUpdatedAt(LocalDateTime.now());
        supplier.setContactInfo("test");
        supplier = supplierRepository.save(supplier);

        Warehouse warehouse = new Warehouse();
        warehouse.setName("Test Warehouse");
        warehouse.setCreatedAt(LocalDateTime.now());
        warehouse.setUpdatedAt(LocalDateTime.now());
        warehouse.setCapacity(1);
        warehouse.setLocation("123");
        warehouse = warehouseRepository.save(warehouse);

        Category category = new Category();
        category.setName("Test Category");
        category.setCreatedAt(LocalDateTime.now());
        category.setDescription("1232");
        category.setUpdatedAt(LocalDateTime.now());
        category = categoryRepository.save(category);

        Product product = new Product();
        product.setName("Test Product");
        product.setDescription("Test Product");
        product.setPrice(BigDecimal.valueOf(1000));
        product.setQuantity(10);
        product.setSupplier(supplier);
        product.setWarehouse(warehouse);
        product.setCategory(category);
        product.setUnit("kg");
        product.setMinStock(10);
        product.setProductCode("2131");
        product.setStatus(ProductStatusEnum.ACTIVE);
        product.setBarcode("121");
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());

        product = productRepository.save(product);

        String name = "Test";

        mockMvc.perform(get("/product/search-name").param("name", name)
                .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isOk());
    }

}
