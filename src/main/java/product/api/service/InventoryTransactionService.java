package product.api.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import product.api.dto.InventoryTransactionRequest;
import product.api.entity.InventoryTransaction;
import product.api.entity.Product;
import product.api.entity.TransactionTypeEnum;
import product.api.entity.Warehouse;
import product.api.exception.NotFoundException;
import product.api.repository.InventoryTransactionRepository;
import product.api.repository.ProductRepository;
import product.api.response.InventoryTransactionResponse;
import java.time.LocalDateTime;

@Service
public class InventoryTransactionService {

    private final ProductService productService;

    private final WarehouseService warehouseService;

    private final ProductRepository productRepository;


    private final InventoryTransactionRepository inventoryTransactionRepository;

    public InventoryTransactionService(InventoryTransactionRepository inventoryTransactionRepository, ProductService productService, WarehouseService warehouseService, ProductRepository productRepository) {
        this.inventoryTransactionRepository = inventoryTransactionRepository;
        this.productService = productService;
        this.warehouseService = warehouseService;
        this.productRepository = productRepository;
    }


    public InventoryTransactionResponse recordTransaction(InventoryTransactionRequest request) {
        Product product = productService.getProductById(request.getProductId())
                .orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND,"Product Not Found"));

        Warehouse warehouse = warehouseService.getWarehouseById(request.getWarehouseId())
                .orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND,"Warehouse Not Found"));

        product.setQuantity(product.getQuantity() + request.getQuantity());
        productRepository.save(product);

        InventoryTransaction transaction = InventoryTransaction.builder()
                .product(product)
                .warehouse(warehouse)
                .type(TransactionTypeEnum.IN)
                .quantity(request.getQuantity())
                .transactionDate(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .build();

        InventoryTransaction savedTransaction = inventoryTransactionRepository.save(transaction);
        return InventoryTransactionResponse.convertTransaction(savedTransaction);
    }
}
