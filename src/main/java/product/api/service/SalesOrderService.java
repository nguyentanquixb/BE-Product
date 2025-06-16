package product.api.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import product.api.dto.SalesOrderRequest;
import product.api.entity.InventoryTransaction;
import product.api.entity.Product;
import product.api.entity.SalesOrder;
import product.api.entity.TransactionTypeEnum;
import product.api.repository.InventoryTransactionRepository;
import product.api.repository.ProductRepository;
import product.api.repository.SalesOrderRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SalesOrderService {

    private final SalesOrderRepository salesOrderRepository;

    private final ProductRepository productRepository;

    final ProductService productService;

    private final InventoryTransactionRepository inventoryTransactionRepository;

    final CustomerService customerService;

    public SalesOrderService(SalesOrderRepository salesOrderRepository, ProductRepository productRepository, ProductService productService, InventoryTransactionRepository inventoryTransactionRepository, CustomerService customerService) {
        this.salesOrderRepository = salesOrderRepository;
        this.productRepository = productRepository;
        this.productService = productService;
        this.inventoryTransactionRepository = inventoryTransactionRepository;
        this.customerService = customerService;
    }

    public Optional<SalesOrder> getSalesOrderById(Long id) {
        return salesOrderRepository.findById(id);
    }

    public List<SalesOrder> getAllSalesOrders() {
        return salesOrderRepository.findAll();
    }

    public SalesOrder createSalesOrder(SalesOrder salesOrder) {
        return salesOrderRepository.save(salesOrder);
    }

    public void deleteSalesOrder(Long id) {
        salesOrderRepository.deleteById(id);
    }

    @Transactional
    public void createSalesOrder(SalesOrderRequest request) {

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (product.getQuantity() < request.getQuantity()) {
            throw new RuntimeException("Product quantity less than requested quantity");
        }

        product.setQuantity(product.getQuantity() - request.getQuantity());
        productRepository.save(product);

        SalesOrder salesOrder = new SalesOrder();
        salesOrder.setCustomer(customerService.getCustomerById(request.getCustomerId()));
        salesOrder.setOrderDate(LocalDateTime.now());
        salesOrderRepository.save(salesOrder);

        InventoryTransaction transaction = new InventoryTransaction();
        transaction.setProduct(productService.findById(request.getProductId()));
        transaction.setType(TransactionTypeEnum.valueOf("SALE"));
        transaction.setQuantity(request.getQuantity());
        transaction.setTransactionDate(LocalDateTime.now());
        inventoryTransactionRepository.save(transaction);
    }
}
