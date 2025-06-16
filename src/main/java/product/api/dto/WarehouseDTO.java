package product.api.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class WarehouseDTO {
    private Long warehouseId;
    private String name;
    private String location;
    private int capacity;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

