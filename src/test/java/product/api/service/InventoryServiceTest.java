package product.api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import product.api.entity.Product;
import product.api.entity.ProductStatusEnum;
import product.api.entity.Warehouse;
import product.api.repository.ProductRepository;
import product.api.response.ProductResponse;

@ExtendWith(MockitoExtension.class)
public class InventoryServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private InventoryService inventoryService;

    @Test
    public void getProductsByWarehouse_ActiveStatusTest() {

        Product product = new Product();
        product.setId(1L);
        product.setName("Test Product");
        product.setStatus(ProductStatusEnum.ACTIVE);

        Warehouse warehouse = new Warehouse();
        warehouse.setName("Test Warehouse");
        product.setWarehouse(warehouse);

        List<Product> products = new ArrayList<>();
        products.add(product);

        when(productRepository.findByWarehouseIdAndStatus(1L, ProductStatusEnum.ACTIVE)).thenReturn(products);

        List<ProductResponse> result = inventoryService.getProductsByWarehouse(1L, "ACTIVE");

        assertEquals(1, result.size());
        assertEquals("Test Product", result.get(0).getName());
    }

    @Test
    public void getProductsByWarehouse_AllStatusTest() {

        Product product = new Product();
        product.setName("Test Product");
        product.setStatus(ProductStatusEnum.INACTIVE);

        Warehouse warehouse = new Warehouse();
        warehouse.setName("Test Warehouse");

        product.setWarehouse(warehouse);

        List<Product> products = new ArrayList<>();
        products.add(product);

        when(productRepository.findByWarehouseId(1L)).thenReturn(products);

        List<ProductResponse> result = inventoryService.getProductsByWarehouse(1L, "INACTIVE");

        assertEquals(1, result.size());
        assertEquals("Test Product", result.get(0).getName());
    }

    @Test
    public void getLowStockProductsTest() {

        Product product = new Product();
        product.setName("Low Stock Product");
        product.setStatus(ProductStatusEnum.ACTIVE);

        Warehouse warehouse = new Warehouse();
        warehouse.setName("Test Warehouse");
        product.setWarehouse(warehouse);

        List<Product> lowStockProducts = new ArrayList<>();
        lowStockProducts.add(product);

        when(productRepository.findByQuantityLessThanMinStock()).thenReturn(lowStockProducts);

        List<ProductResponse> result = inventoryService.getLowStockProducts();

        assertEquals(1, result.size());
        assertEquals("Low Stock Product", result.get(0).getName());
    }
}
