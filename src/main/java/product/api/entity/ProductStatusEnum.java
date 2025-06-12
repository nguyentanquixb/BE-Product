package product.api.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum ProductStatusEnum {
    ACTIVE("active"),
    INACTIVE("inactive");

    private  String status;


}
