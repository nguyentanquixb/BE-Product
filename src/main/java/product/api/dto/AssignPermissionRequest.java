package product.api.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AssignPermissionRequest {
    private Long roleId;
    private List<Long> permissionIds;
}
