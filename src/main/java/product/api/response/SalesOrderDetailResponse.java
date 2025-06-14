package product.api.response;

import lombok.*;
import product.api.entity.SalesOrderDetail;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SalesOrderDetailResponse {
    private Long id;
    private Long salesOrderId;
    private Long productId;
    private String productName;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;

    public static SalesOrderDetailResponse convertSalesOrderDetail(SalesOrderDetail detail) {
        return SalesOrderDetailResponse.builder()
                .id(detail.getId())
                .salesOrderId(detail.getSalesOrder().getId())
                .productId(detail.getProduct().getId())
                .productName(detail.getProduct().getName())
                .quantity(detail.getQuantity())
                .unitPrice(detail.getUnitPrice())
                .totalPrice(detail.getTotalPrice())
                .build();
    }
}
