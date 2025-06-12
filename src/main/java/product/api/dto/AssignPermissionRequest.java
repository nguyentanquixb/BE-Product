package product.api.dto;
import lombok.*;

import java.util.List;

@Getter
@Setter
public class AssignPermissionRequest {
    private Long userId;
    private List<Long> permissionIds;
}
