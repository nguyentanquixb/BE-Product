package product.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import product.api.dto.AssignPermissionRequest;
import product.api.dto.UserDTO;
import product.api.dto.UserRequest;
import product.api.entity.Permission;
import product.api.entity.User;
import product.api.repository.PermissionRepository;
import product.api.repository.UserRepository;
import product.api.response.Response;
import product.api.service.PermissionService;
import product.api.service.UserService;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private PermissionService  permissionService;

    @Autowired
    private PermissionRepository permissionRepository;

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody UserRequest userRequest) {
        User user = userService.saveUser(userRequest);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        UserDTO user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/permissions")
    public ResponseEntity<Response> assignPermissionsToUser(@RequestBody AssignPermissionRequest assignPermissionRequest) {
        Optional<User> userOptional = userService.findById(assignPermissionRequest.getUserId());
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Response.error("User not found"));
        }

        if (assignPermissionRequest.getPermissionIds() == null || assignPermissionRequest.getPermissionIds().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Response.error("Permission list is empty"));
        }

        User user = userOptional.get();

        Set<Long> existingPermissionIds = user.getPermissions().stream()
                .map(Permission::getId)
                .collect(Collectors.toSet());

        List<Permission> permissions = permissionService.getPermissionById(assignPermissionRequest.getPermissionIds())
                .stream()
                .filter(permission -> !existingPermissionIds.contains(permission.getId())) // Loại bỏ quyền đã tồn tại
                .toList();

        if (permissions.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Response.error("No new permissions to assign"));
        }

        user.getPermissions().addAll(permissions);
        userService.updateUser(user);

        return ResponseEntity.ok(Response.ok(user));
    }




}

