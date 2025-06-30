package product.api.service;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import product.api.dto.ProductRequest;
import product.api.entity.Category;
import product.api.entity.Product;
import product.api.entity.Supplier;
import product.api.entity.Warehouse;
import product.api.exception.NotFoundException;
import product.api.repository.ProductRepository;


@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryService categoryService;

    @Mock
    private SupplierService supplierService;

    @Mock
    private WarehouseService warehouseService;

    @InjectMocks
    private ProductService productService;

    @Test
    public void getProductByIdTest() {
        Product product = new Product();
        product.setId(1L);
        product.setName("Test Product");
        product.setProductCode("TEST001");
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        Product foundProduct = productService.findById(1L);
        assertEquals("Test Product", foundProduct.getName());
    }

    @Test
    public void findProduct_SuccessTest() {
        Product product = new Product();
        product.setId(1L);
        product.setName("Test Product");
        product.setProductCode("TEST1");

        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        Product foundProduct = productService.findById(1L);
        assertEquals("Test Product", foundProduct.getName());
    }

    @Test
    public void findProduct_NotFoundTest() {
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());
        NotFoundException notFoundException = assertThrows(NotFoundException.class, () -> productService.findById(1L));
        assertEquals("Product Not Found", notFoundException.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, notFoundException.getStatus());
    }

    @Test
    public void createProductTest() {
        ProductRequest productRequest = new ProductRequest();
        productRequest.setId(1L);
        productRequest.setName("Test Product");
        productRequest.setProductCode("TEST1");
        productRequest.setCategoryId(1L);
        productRequest.setWarehouseId(1L);
        productRequest.setSupplierId(1L);

        Product product = new Product();
        product.setId(1L);
        product.setName("Test Product");
        product.setProductCode("TEST001");


        Category category = new Category();
        category.setName("Test Category");
        Warehouse warehouse = new Warehouse();
        warehouse.setName("Test Warehouse");
        Supplier supplier = new Supplier();
        supplier.setName("Test Supplier");

        when(categoryService.findCategory(1L)).thenReturn(category);
        when(warehouseService.findWarehouse(1L)).thenReturn(warehouse);
        when(supplierService.findSupplier(1L)).thenReturn(supplier);
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product createdProduct = productService.createProduct(productRequest);
        assertNotNull(createdProduct);
        assertEquals("Test Product", createdProduct.getName());
    }


}
