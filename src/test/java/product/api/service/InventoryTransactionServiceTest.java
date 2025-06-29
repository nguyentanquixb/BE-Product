package product.api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import product.api.dto.InventoryTransactionRequest;
import product.api.entity.InventoryTransaction;
import product.api.entity.Product;
import product.api.entity.TransactionTypeEnum;
import product.api.entity.Warehouse;
import product.api.exception.NotFoundException;
import product.api.repository.InventoryTransactionRepository;
import product.api.repository.ProductRepository;
import product.api.response.InventoryTransactionResponse;

@ExtendWith(MockitoExtension.class)
public class InventoryTransactionServiceTest {

    @Mock
    private InventoryTransactionRepository inventoryTransactionRepository;

    @Mock
    private ProductService productService;

    @Mock
    private WarehouseService warehouseService;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private InventoryTransactionService inventoryTransactionService;

    @Test
    public void recordTransaction_SuccessTest() {

        InventoryTransactionRequest request = new InventoryTransactionRequest();
        request.setProductId(1L);
        request.setWarehouseId(1L);
        request.setQuantity(10);

        Product product = new Product();
        product.setId(1L);
        product.setName("Test Product");

        Warehouse warehouse = new Warehouse();
        warehouse.setId(1L);
        warehouse.setName("Test Warehouse");

        InventoryTransaction transaction = InventoryTransaction.builder()
                .product(product)
                .warehouse(warehouse)
                .type(TransactionTypeEnum.IN)
                .quantity(10)
                .build();

        when(productService.getProductById(1L)).thenReturn(Optional.of(product));
        when(warehouseService.getWarehouseById(1L)).thenReturn(Optional.of(warehouse));
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(inventoryTransactionRepository.save(any(InventoryTransaction.class))).thenReturn(transaction);

        InventoryTransactionResponse response = inventoryTransactionService.recordTransaction(request);

        assertEquals(1L, response.getProduct().getId());
        assertEquals(1L, response.getWarehouse().getId());
        assertEquals(10, response.getQuantity());
        assertEquals(TransactionTypeEnum.IN, response.getType());
    }

    @Test
    public void recordTransaction_ProductNotFoundTest() {

        InventoryTransactionRequest request = new InventoryTransactionRequest();
        request.setProductId(1L);
        request.setWarehouseId(1L);
        request.setQuantity(10);

        when(productService.getProductById(1L)).thenReturn(Optional.empty());

        NotFoundException notFoundException = assertThrows(NotFoundException.class,
                () -> inventoryTransactionService.recordTransaction(request));

        assertEquals("Product Not Found", notFoundException.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, notFoundException.getStatus());
    }

    @Test
    public void recordTransaction_WarehouseNotFoundTest() {

        InventoryTransactionRequest request = new InventoryTransactionRequest();
        request.setProductId(1L);
        request.setWarehouseId(1L);
        request.setQuantity(10);

        Product product = new Product();
        product.setId(1L);
        product.setName("Test Product");

        when(productService.getProductById(1L)).thenReturn(Optional.of(product));
        when(warehouseService.getWarehouseById(1L)).thenReturn(Optional.empty());

        NotFoundException notFoundException = assertThrows(NotFoundException.class,
                () -> inventoryTransactionService.recordTransaction(request));

        assertEquals("Warehouse Not Found", notFoundException.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, notFoundException.getStatus());
    }
}