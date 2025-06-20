package product.api.response;

import lombok.*;
import product.api.entity.Permission;
import product.api.entity.Role;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoleResponse {

    private Long id;
    private String name;
    private String description;
    private List<PermissionResponse> permissions;

    public static RoleResponse convert(Role role) {
        if (role == null) return null;

        return RoleResponse.builder()
                .id(role.getId())
                .name(role.getName())
                .description(role.getDescription())
                .permissions(convertPermissions(role.getPermissions()))
                .build();
    }

    private static List<PermissionResponse> convertPermissions(Set<Permission> permissions) {
        if (permissions == null) return List.of();
        return permissions.stream()
                .map(PermissionResponse::convert)
                .collect(Collectors.toList());
    }
}
