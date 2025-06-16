package product.api.entity;

import lombok.Getter;

@Getter
public enum ProductStatusEnum {
    ACTIVE("active"),
    INACTIVE("inactive");

    private final String status;

    ProductStatusEnum(String status) {
        this.status = status;
    }

    public static ProductStatusEnum fromString(String text) {
        if (text == null || text.isEmpty()) {
            throw new IllegalArgumentException("Status cannot be null or empty.");
        }

        for (ProductStatusEnum statusEnum : ProductStatusEnum.values()) {
            if (statusEnum.status.equalsIgnoreCase(text)) {
                return statusEnum;
            }
        }

        throw new IllegalArgumentException("No enum constant for value: " + text);
    }


}
