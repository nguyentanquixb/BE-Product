package product.api.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class ProductRequest {

    private Long id;
    private String name;
    private String productCode;
    private String description;
    private BigDecimal price;
    private Integer quantity;
    private Integer minStock;
    private String unit;
    private String barcode;
    private String status;
    private Long categoryId;
    private Long warehouseId;
    private Long supplierId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
