package product.api.service;

import org.springframework.stereotype.Service;
import product.api.entity.Product;
import product.api.entity.ProductStatusEnum;
import product.api.repository.ProductRepository;
import product.api.response.ProductResponse;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InventoryService {

    private final ProductRepository productRepository;

    public InventoryService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductResponse> getProductsByWarehouse(Long warehouseId, String status) {
        List<Product> products;
        if ("active".equalsIgnoreCase(status)) {
            products = productRepository.findByWarehouseIdAndStatus(warehouseId, ProductStatusEnum.valueOf("ACTIVE"));
        } else {
            products = productRepository.findByWarehouseId(warehouseId);
        }
        return products.stream().map(ProductResponse::convertProduct).toList();
    }

    public List<ProductResponse> getLowStockProducts() {
        List<Product> lowStockProducts = productRepository.findByQuantityLessThanMinStock();
        return lowStockProducts.stream().map(ProductResponse::convertProduct).collect(Collectors.toList());
    }
}
