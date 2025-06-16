package product.api.service;

import org.springframework.stereotype.Service;
import product.api.dto.StockAdjustmentRequest;
import product.api.entity.Product;
import product.api.entity.StockAdjustment;
import product.api.repository.ProductRepository;
import product.api.repository.StockAdjustmentRepository;
import product.api.response.StockAdjustmentResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StockAdjustmentService {

    private final StockAdjustmentRepository stockAdjustmentRepository;

    final ProductService productService;

    private final ProductRepository productRepository;

    final WarehouseService warehouseService;

    public StockAdjustmentService(StockAdjustmentRepository stockAdjustmentRepository, ProductService productService, ProductRepository productRepository, WarehouseService warehouseService) {
        this.stockAdjustmentRepository = stockAdjustmentRepository;
        this.productService = productService;
        this.productRepository = productRepository;
        this.warehouseService = warehouseService;
    }

    public Optional<StockAdjustment> getStockAdjustmentById(Long id) {
        return stockAdjustmentRepository.findById(id);
    }

    public List<StockAdjustment> getAllStockAdjustments() {
        return stockAdjustmentRepository.findAll();
    }

    public StockAdjustment createStockAdjustment(StockAdjustment stockAdjustment) {
        return stockAdjustmentRepository.save(stockAdjustment);
    }

    public void deleteStockAdjustment(Long id) {
        stockAdjustmentRepository.deleteById(id);
    }

    public StockAdjustmentResponse createStockAdjustment(StockAdjustmentRequest request) {
        Product product = productService.findById(request.getProductId());

        Integer newQuantity = product.getQuantity() + request.getAdjustmentQuantity();
        product.setQuantity(newQuantity);
        productRepository.save(product);

        StockAdjustment adjustment = StockAdjustment.builder()
                .product(product)
                .warehouse(warehouseService.findWarehouse(request.getWarehouseId()))
                .quantity(request.getAdjustmentQuantity())
                .reason(request.getAdjustmentReason())
                .adjustmentDate(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .build();

        StockAdjustment savedAdjustment = stockAdjustmentRepository.save(adjustment);
        return StockAdjustmentResponse.convertAdjustment(savedAdjustment);
    }

    public List<StockAdjustmentResponse> getStockAdjustmentsByProduct(Long productId) {
        List<StockAdjustment> adjustments = stockAdjustmentRepository.findAByProductId(productId);
        return adjustments.stream()
                .map(StockAdjustmentResponse::convertAdjustment)
                .collect(Collectors.toList());
    }
}
