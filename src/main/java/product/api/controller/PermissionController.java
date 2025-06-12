package product.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import product.api.dto.PermissionRequest;
import product.api.entity.Permission;
import product.api.response.Response;
import product.api.service.PermissionService;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController()
@RequestMapping("/permission")
public class PermissionController {

    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @PostMapping("/list-permission")
    public ResponseEntity<Response> crPermission(@RequestBody List<PermissionRequest> permissionRequests) {
        if (permissionRequests == null || permissionRequests.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Response.error("Permission list is empty"));
        }
       List<Permission> permissions = new ArrayList<>();
        for (PermissionRequest permissionRequest : permissionRequests) {
            if (permissionRequest.getName() == null || permissionRequest.getName().isEmpty()
                    || permissionRequest.getDescription() == null || permissionRequest.getDescription().isEmpty()) {
                continue;
            }
            Permission permission = new Permission();
            permission.setName(permissionRequest.getName());
            permission.setDescription(permissionRequest.getDescription());
            permission.setCreatedAt(LocalDateTime.now());
            permissions.add(permission);
        }
        permissionService.crPermission(permissions);
        return ResponseEntity.status(HttpStatus.OK).body(Response.ok("Permissions created successfully"));
    }
}
