package product.api.dto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class InventoryReportDTO {

    private Long productId;
    private String productName;
    private int quantity;
    private double totalValue;

}

