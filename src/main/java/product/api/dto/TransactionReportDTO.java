package product.api.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import product.api.entity.Product;
import product.api.entity.TransactionTypeEnum;

import java.time.LocalDateTime;
@Getter
@Setter
@Builder
public class TransactionReportDTO {
    private Long transactionId;
    private Product product;
    private TransactionTypeEnum transactionType;
    private int quantity;
    private LocalDateTime transactionDate;

}
