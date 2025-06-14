package product.api.response;

import lombok.*;
import product.api.entity.OrderStatusEnum;
import product.api.entity.SalesOrder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SalesOrderResponse {
    private Long id;
    private Long warehouseId;
    private String warehouseLocation;
    private Long customerId;
    private String customerName;
    private OrderStatusEnum status;
    private BigDecimal totalAmount;
    private LocalDateTime orderDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<SalesOrderDetailResponse> details;

    public static SalesOrderResponse convertSalesOrder(SalesOrder order) {
        return SalesOrderResponse.builder()
                .id(order.getId())
                .warehouseId(order.getWarehouse().getId())
                .warehouseLocation(order.getWarehouse().getLocation())
                .customerId(order.getCustomer() != null ? order.getCustomer().getId() : null)
                .customerName(order.getCustomer() != null ? order.getCustomer().getName() : null)
                .status(order.getStatus())
                .totalAmount(order.getTotalAmount())
                .orderDate(order.getOrderDate())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .details(order.getDetails().stream()
                        .map(SalesOrderDetailResponse::convertSalesOrderDetail)
                        .toList())
                .build();
    }
}
