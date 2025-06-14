package product.api.response;

import lombok.*;
import product.api.entity.Warehouse;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WarehouseResponse {
    private Long id;
    private String name;
    private String location;
    private Integer capacity;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static WarehouseResponse convertWarehouse(Warehouse warehouse) {
        return WarehouseResponse.builder()
                .id(warehouse.getId())
                .name(warehouse.getName())
                .location(warehouse.getLocation())
                .capacity(warehouse.getCapacity())
                .createdAt(warehouse.getCreatedAt())
                .updatedAt(warehouse.getUpdatedAt())
                .build();
    }
}
