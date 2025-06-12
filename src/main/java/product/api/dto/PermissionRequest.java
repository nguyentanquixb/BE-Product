package product.api.dto;

import lombok.Getter;
import lombok.Setter;
import java.sql.Timestamp;
@Getter
@Setter
public class PermissionRequest {
    private String name;
    private String description;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
