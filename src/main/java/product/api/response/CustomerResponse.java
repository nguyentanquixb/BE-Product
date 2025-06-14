package product.api.response;

import lombok.*;
import product.api.entity.Customer;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerResponse {
    private Long id;
    private String name;
    private String contactInfo;
    private String address;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static CustomerResponse convertCustomer(Customer customer) {
        return CustomerResponse.builder()
                .id(customer.getId())
                .name(customer.getName())
                .contactInfo(customer.getContactInfo())
                .address(customer.getAddress())
                .createdAt(customer.getCreatedAt())
                .updatedAt(customer.getUpdatedAt())
                .build();
    }
}
