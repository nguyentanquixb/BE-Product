package product.api.response;

import lombok.*;
import product.api.entity.StockAdjustment;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StockAdjustmentResponse {
    private Long id;
    private Long productId;
    private Long warehouseId;
    private Integer quantity;
    private String reason;
    private LocalDateTime adjustmentDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static StockAdjustmentResponse convertAdjustment(StockAdjustment adjustment) {
        return StockAdjustmentResponse.builder()
                .id(adjustment.getId())
                .productId(adjustment.getProduct().getId())
                .warehouseId(adjustment.getWarehouse().getId())
                .quantity(adjustment.getQuantity())
                .reason(adjustment.getReason())
                .adjustmentDate(adjustment.getAdjustmentDate())
                .createdAt(adjustment.getCreatedAt())
                .updatedAt(adjustment.getUpdatedAt())
                .build();
    }
}

