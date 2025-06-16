package product.api.dto;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SalesOrderRequest {

    private Long productId;
    private int quantity;
    private Long customerId;
    private Long warehouseId;
}
