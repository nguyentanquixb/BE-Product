package product.api.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InventoryTransactionRequest {
    private Long productId;
    private Long warehouseId;
    private Integer quantity;
}
