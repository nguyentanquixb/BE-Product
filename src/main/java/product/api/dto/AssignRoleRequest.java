package product.api.dto;
import lombok.*;

@Getter
@Setter
public class AssignRoleRequest {
    private Long userId;
    private Long roleId;
}
