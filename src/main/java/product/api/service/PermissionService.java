package product.api.service;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import product.api.entity.Permission;
import product.api.repository.PermissionRepository;

import java.util.List;
import java.util.Optional;

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
}
