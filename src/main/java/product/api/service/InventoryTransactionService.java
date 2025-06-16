package product.api.service;

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
import java.util.List;
import java.util.Optional;

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

    public Optional<InventoryTransaction> getTransactionById(Long id) {
        return inventoryTransactionRepository.findById(id);
    }

    public List<InventoryTransaction> getAllTransactions() {
        return inventoryTransactionRepository.findAll();
    }

    public InventoryTransaction createTransaction(InventoryTransaction transaction) {
        return inventoryTransactionRepository.save(transaction);
    }

    public void deleteTransaction(Long id) {
        inventoryTransactionRepository.deleteById(id);
    }

    public InventoryTransactionResponse recordTransaction(InventoryTransactionRequest request) {
        Product product = productService.getProductById(request.getProductId())
                .orElseThrow(() -> new NotFoundException("Product not found"));

        Warehouse warehouse = warehouseService.getWarehouseById(request.getWarehouseId())
                .orElseThrow(() -> new NotFoundException("Warehouse not found"));

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
