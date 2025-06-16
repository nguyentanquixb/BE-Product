package product.api.dto;

import lombok.*;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PurchaseOrderRequest {
    private Long supplierId;
    private BigDecimal totalAmount;
    private List<PurchaseOrderDetailRequest> details;
}
