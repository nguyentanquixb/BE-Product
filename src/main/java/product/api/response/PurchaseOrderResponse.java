package product.api.response;

import lombok.*;
import product.api.entity.OrderStatusEnum;
import product.api.entity.PurchaseOrder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PurchaseOrderResponse {
    private Long id;
    private Long supplierId;
    private String supplierName;
    private OrderStatusEnum status;
    private BigDecimal totalAmount;
    private LocalDateTime orderDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<PurchaseOrderDetailResponse> details;

    public static PurchaseOrderResponse convertPurchaseOrder(PurchaseOrder order) {
        return PurchaseOrderResponse.builder()
                .id(order.getId())
                .supplierId(order.getSupplier().getId())
                .supplierName(order.getSupplier().getName())
                .status(order.getStatus())
                .totalAmount(order.getTotalAmount())
                .orderDate(order.getOrderDate())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .details(order.getDetails().stream()
                        .map(PurchaseOrderDetailResponse::convertPurchaseOrderDetail)
                        .toList())
                .build();
    }
}
