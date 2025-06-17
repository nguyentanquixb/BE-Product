package product.api.response;

import lombok.*;
import product.api.dto.ProductRequest;
import product.api.entity.Product;
import product.api.entity.ProductStatusEnum;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponse {
    private Long id;
    private String name;
    private String productCode;
    private String description;
    private BigDecimal price;
    private Integer quantity;
    private Integer minStock;
    private String unit;
    private String barcode;
    private ProductStatusEnum status;
    private Long categoryId;
    private Long warehouseId;
    private Long supplierId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ProductResponse convertProduct(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .productCode(product.getProductCode())
                .description(product.getDescription())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .minStock(product.getMinStock())
                .unit(product.getUnit())
                .barcode(product.getBarcode())
                .status(product.getStatus())
                .categoryId(product.getCategory().getId())
                .warehouseId(product.getWarehouse().getId())
                .supplierId(product.getSupplier().getId())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }

    public static ProductResponse convertFromRequest(ProductRequest request) {
        return ProductResponse.builder()
                .id(request.getId())
                .name(request.getName())
                .productCode(request.getProductCode())
                .description(request.getDescription())
                .price(request.getPrice())
                .quantity(request.getQuantity())
                .minStock(request.getMinStock())
                .unit(request.getUnit())
                .barcode(request.getBarcode())
                .status(ProductStatusEnum.valueOf(String.valueOf(request.getStatus())))
                .categoryId(request.getCategoryId())
                .warehouseId(request.getWarehouseId())
                .supplierId(request.getSupplierId())
                .createdAt(request.getCreatedAt())
                .updatedAt(request.getUpdatedAt())
                .build();
    }

}
