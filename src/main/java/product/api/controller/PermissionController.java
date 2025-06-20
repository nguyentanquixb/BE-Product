package product.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import product.api.dto.PermissionRequest;
import product.api.entity.Permission;
import product.api.response.Response;
import product.api.service.PermissionService;
import product.api.utils.ResponseUtil;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController()
@RequestMapping("/permission")
public class PermissionController {

    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @PostMapping("/list")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> CreatePermission(@RequestBody List<PermissionRequest> permissionRequests) {
        if (permissionRequests == null || permissionRequests.isEmpty()) {
            return ResponseUtil.buildResponse(HttpStatus.BAD_REQUEST, "Permission Request is Empty");
        }

        Set<String> existingPermissionNames = permissionService.getAllPermissionNames();

        Set<String> seenPermissionName = new HashSet<>();

        List<Permission> permissions = new ArrayList<>();
        for (PermissionRequest permissionRequest : permissionRequests) {
            if (permissionRequest.getName() == null || permissionRequest.getName().isEmpty()
                    || permissionRequest.getDescription() == null || permissionRequest.getDescription().isEmpty()) {
                continue;
            }

            if (!seenPermissionName.add(permissionRequest.getName())) {
                continue;
            }

            if (existingPermissionNames.contains(permissionRequest.getName())) {
                continue;
            }

            Permission permission = new Permission();
            permission.setName(permissionRequest.getName());
            permission.setDescription(permissionRequest.getDescription());
            permission.setCreatedAt(LocalDateTime.now());
            permissions.add(permission);
        }
        permissionService.crPermission(permissions);
        return ResponseUtil.buildResponse(HttpStatus.OK, permissions);
    }
}
