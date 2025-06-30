package product.api.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import product.api.dto.ProductRequest;
import product.api.entity.*;
import product.api.filters.JwtFilter;
import product.api.response.ProductResponse;
import product.api.response.Response;
import product.api.service.ProductService;
import product.api.service.S3Service;
import product.api.utils.JwtUtil;
import product.api.validate.ProductValidate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;

@WebMvcTest(ProductController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ProductControllerTest {

    @Autowired
    private ProductController productController;

    @MockitoBean
    private ProductService productService;

    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private JwtFilter jwtFilter;

    @MockitoBean
    private S3Service s3Service;

    @MockitoBean
    private ProductValidate productValidate;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void TestgetProductByIdSuccess() {

        Category  category = new Category();
        category.setId(1L);

        Warehouse warehouse = new Warehouse();
        warehouse.setId(1L);

        Supplier supplier = new Supplier();
        supplier.setId(1L);

        Product product = new Product();
        product.setId(1L);
        product.setName("product1");
        product.setCategory(category);
        product.setWarehouse(warehouse);
        product.setSupplier(supplier);

        when(productService.findById(1L)).thenReturn(product);

        ResponseEntity<Response> responseEntity = productController.getProductById(1L);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        Response response = responseEntity.getBody();
        assertNotNull(response);

        assertInstanceOf(ProductResponse.class, response.getData());

        ProductResponse productResponse = (ProductResponse) response.getData();
        assertEquals(1L, productResponse.getId());
        assertEquals("product1", productResponse.getName());
    }

    @Test
    public void TestgetAllProduct(){

        Product product = new Product();
        product.setId(1L);
        product.setName("product1");
        Warehouse warehouse = new Warehouse();
        warehouse.setId(1L);

        Category category = new Category();
        category.setId(1L);

        Supplier supplier = new Supplier();
        supplier.setId(1L);

        product.setWarehouse(warehouse);
        product.setSupplier(supplier);
        product.setCategory(category);

        List<Product> products = new ArrayList<>();
        products.add(product);

        when(productService.getAllProduct()).thenReturn(products);

        ResponseEntity<Response> responseEntity = productController.getAllProduct();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        Response response = responseEntity.getBody();
        assertNotNull(response);
        assertInstanceOf(List.class, response.getData());
        List<ProductResponse> productResponse = (List<ProductResponse>) response.getData();
        assertEquals(1L, productResponse.get(0).getId());
        assertEquals("product1", productResponse.get(0).getName());
    }

    @Test
    public void testCreateProduct(){
        ProductRequest request = new ProductRequest();

        when(productValidate.validateProduct(request)).thenReturn(Collections.emptyList());

        Product product = new Product();
        product.setId(1L);
        product.setName("product1");

        Category category = new Category();
        category.setId(1L);
        Supplier supplier = new Supplier();
        supplier.setId(1L);
        Warehouse warehouse = new Warehouse();
        warehouse.setId(1L);

        product.setCategory(category);
        product.setSupplier(supplier);
        product.setWarehouse(warehouse);

        when(productService.createProduct(request)).thenReturn(product);

        ResponseEntity<Response> responseEntity = productController.createProduct(request);

        responseEntity.getBody();
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        Response response = responseEntity.getBody();
        assertNotNull(response);
        assertInstanceOf(ProductResponse.class, response.getData());
        ProductResponse productResponse = (ProductResponse) response.getData();
        assertEquals(1L, productResponse.getId());
        assertEquals("product1", productResponse.getName());

    }

    @Test
    public void testUpdateProduct(){
        Long productId = 1L;
        ProductRequest request = new ProductRequest();
        request.setName("Product Name");
        request.setCategoryId(1L);
        request.setSupplierId(1L);
        request.setWarehouseId(1L);

        Product updatedProduct = new Product();
        updatedProduct.setId(productId);
        updatedProduct.setName("Product Name");

        Category category = new Category();
        category.setId(1L);
        Supplier supplier = new Supplier();
        supplier.setId(1L);
        Warehouse warehouse = new Warehouse();
        warehouse.setId(1L);

        updatedProduct.setCategory(category);
        updatedProduct.setSupplier(supplier);
        updatedProduct.setWarehouse(warehouse);

        ProductResponse expectedResponse = ProductResponse.convertProduct(updatedProduct);

        when(productValidate.validateProduct(request)).thenReturn(Collections.emptyList());
        when(productService.updateProduct(request,productId)).thenReturn(updatedProduct);

        ResponseEntity<Response> response = productController.updateProduct(productId, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        ProductResponse data = (ProductResponse) response.getBody().getData();
        assertEquals(expectedResponse.getName(), data.getName());

    }

    @Test
    public void testDeleteProduct(){

        ResponseEntity<Response> responseEntity = productController.deleteProduct(1L);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        Response response = responseEntity.getBody();
        assertNotNull(response);
    }

    @Test
    public void testSearchProducts() {

        String nameOrCode = "test";
        Long categoryId = 1L;
        Long warehouseId = 2L;
        Long supplierId = 3L;
        ProductStatusEnum status = ProductStatusEnum.ACTIVE;
        int page = 0;
        int size = 10;

        Category category = new Category();
        Supplier supplier = new Supplier();
        Warehouse warehouse = new Warehouse();

        category.setId(categoryId);
        supplier.setId(supplierId);
        warehouse.setId(warehouseId);

        Product product = new Product();
        product.setId(1L);
        product.setName("test product");
        product.setCategory(category);
        product.setSupplier(supplier);
        product.setWarehouse(warehouse);

        ProductResponse productResponse = new ProductResponse();
        productResponse.setId(1L);
        productResponse.setName("test product");

        Page<Product> mockPage = new PageImpl<>(List.of(product), PageRequest.of(page, size), 1);

        when(productService.searchProducts(
                nameOrCode, categoryId, warehouseId, supplierId, status, page, size)
        ).thenReturn(mockPage);

        ResponseEntity<Response> responseEntity = productController.searchProducts(nameOrCode,categoryId, warehouseId, supplierId, status, page, size);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Response response = responseEntity.getBody();
        assertNotNull(response);

        Map<String, Object> data = (Map<String, Object>) response.getData();
        assertNotNull(data);

        List<?> productList = (List<?>) data.get("data");
        assertNotNull(productList);
        assertEquals(1, productList.size());

        ProductResponse postResponse = (ProductResponse) productList.get(0);
        assertEquals(1L, postResponse.getId());
        assertEquals("test product", postResponse.getName());

    }
}
