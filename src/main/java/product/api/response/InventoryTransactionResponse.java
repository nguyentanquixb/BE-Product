
package product.api.response;

import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import product.api.entity.InventoryTransaction;
import product.api.entity.Product;
import product.api.entity.TransactionTypeEnum;
import product.api.entity.Warehouse;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InventoryTransactionResponse {
    private Long id;
    private Product product;
    private String productName;
    private Warehouse warehouse;
    private String warehouseLocation;
    private TransactionTypeEnum type;
    private Integer quantity;
    private LocalDateTime transactionDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private TransactionTypeEnum transactionType;

    private static final Logger logger = LoggerFactory.getLogger(InventoryTransactionResponse.class);

    public static InventoryTransactionResponse convertTransaction(InventoryTransaction transaction) {

        logger.info("Transaction type: {}", transaction.getType());
        return InventoryTransactionResponse.builder()
                .id(transaction.getId())
                .product(transaction.getProduct())
                .productName(transaction.getProduct().getName())
                .warehouse(transaction.getWarehouse())
                .warehouseLocation(transaction.getWarehouse().getLocation())
                .type(transaction.getType())
                .quantity(transaction.getQuantity())
                .transactionDate(transaction.getTransactionDate())
                .createdAt(transaction.getCreatedAt())
                .updatedAt(transaction.getUpdatedAt())
                .build();
    }

}

