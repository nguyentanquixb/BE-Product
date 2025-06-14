package product.api.response;

import lombok.*;
import product.api.entity.InventoryTransaction;
import product.api.entity.TransactionTypeEnum;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InventoryTransactionResponse {
    private Long id;
    private Long productId;
    private String productName;
    private Long warehouseId;
    private String warehouseLocation;
    private TransactionTypeEnum type;
    private Integer quantity;
    private LocalDateTime transactionDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static InventoryTransactionResponse convertTransaction(InventoryTransaction transaction) {
        return InventoryTransactionResponse.builder()
                .id(transaction.getId())
                .productId(transaction.getProduct().getId())
                .productName(transaction.getProduct().getName())
                .warehouseId(transaction.getWarehouse().getId())
                .warehouseLocation(transaction.getWarehouse().getLocation())
                .type(transaction.getType())
                .quantity(transaction.getQuantity())
                .transactionDate(transaction.getTransactionDate())
                .createdAt(transaction.getCreatedAt())
                .updatedAt(transaction.getUpdatedAt())
                .build();
    }
}
