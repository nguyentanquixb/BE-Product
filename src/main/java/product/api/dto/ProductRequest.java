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
    private Long unit;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
