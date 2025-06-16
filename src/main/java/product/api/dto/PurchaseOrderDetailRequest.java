package product.api.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PurchaseOrderDetailRequest {
    private Long productId;
    private Integer quantity;
    private BigDecimal unitPrice;
}
