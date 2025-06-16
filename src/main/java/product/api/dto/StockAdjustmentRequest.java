package product.api.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StockAdjustmentRequest {
    private Long productId;
    private Long warehouseId;
    private Integer adjustmentQuantity;
    private String adjustmentReason;
}
