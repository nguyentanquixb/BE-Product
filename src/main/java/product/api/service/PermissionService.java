package product.api.service;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import product.api.entity.Permission;
import product.api.repository.PermissionRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PermissionService {
    private final PermissionRepository permissionRepository;

    public PermissionService(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    public void crPermission(List<Permission> permissions) {
        permissionRepository.saveAll(permissions);
    }

    public List<Permission> getPermissionById(List<Long> permissionIds ) {
        return permissionRepository.findAllById(permissionIds);
    }

    public Set<String> getAllPermissionNames() {
        return permissionRepository.findAll().stream()
                .map(Permission::getName)
                .collect(Collectors.toSet());
    }

}
