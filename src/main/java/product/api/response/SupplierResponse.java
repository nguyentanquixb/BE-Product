package product.api.response;

import lombok.*;
import product.api.entity.Supplier;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SupplierResponse {
    private Long id;
    private String name;
    private String contactInfo;
    private String address;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static SupplierResponse convertSupplier(Supplier supplier) {
        return SupplierResponse.builder()
                .id(supplier.getId())
                .name(supplier.getName())
                .contactInfo(supplier.getContactInfo())
                .address(supplier.getAddress())
                .createdAt(supplier.getCreatedAt())
                .updatedAt(supplier.getUpdatedAt())
                .build();
    }
}
