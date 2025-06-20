package product.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import product.api.dto.AssignPermissionRequest;
import product.api.dto.AssignRoleRequest;
import product.api.dto.UserDTO;
import product.api.dto.UserRequest;
import product.api.entity.Permission;
import product.api.entity.Role;
import product.api.entity.User;
import product.api.response.Response;
import product.api.service.PermissionService;
import product.api.service.RoleService;
import product.api.service.UserService;
import product.api.utils.ResponseUtil;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    private final PermissionService permissionService;

    private final RoleService roleService;

    public UserController(UserService userService, PermissionService permissionService, RoleService roleService) {
        this.userService = userService;
        this.permissionService = permissionService;
        this.roleService = roleService;
    }

    @PostMapping("/create-user")
    public ResponseEntity<Response> createUser(@RequestBody UserRequest userRequest) {
        User user = userService.saveUser(userRequest);
        return ResponseUtil.buildResponse(HttpStatus.OK, user);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> getUserById(@PathVariable Long id) {
        UserDTO user = userService.getUserById(id);
        return ResponseUtil.buildResponse(HttpStatus.OK, user);
    }

    @PutMapping("/role")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> updateUserRole(@RequestBody AssignRoleRequest assignRoleRequest) {
        Optional<User> userOptional = userService.findById(assignRoleRequest.getUserId());
        if (userOptional.isEmpty()) {
            return ResponseUtil.buildResponse(HttpStatus.NOT_FOUND, "User not found");
        }

        if (assignRoleRequest.getRoleId() == null) {
            return ResponseUtil.buildResponse(HttpStatus.BAD_REQUEST, "Role ID is required");
        }

        Optional<Role> roleOptional = roleService.findById(assignRoleRequest.getRoleId());
        if (roleOptional.isEmpty()) {
            return ResponseUtil.buildResponse(HttpStatus.NOT_FOUND, "Role not found");
        }

        User user = userOptional.get();

        user.getRoles().clear();
        user.getRoles().add(roleOptional.get());

        userService.updateUser(user);

        return ResponseUtil.buildResponse(HttpStatus.OK, user);
    }

    @PostMapping("/permissions")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> assignPermissionsToRole(@RequestBody AssignPermissionRequest request) {
        if (request.getRoleId() == null || request.getPermissionIds() == null || request.getPermissionIds().isEmpty()) {
            return ResponseUtil.buildResponse(HttpStatus.BAD_REQUEST, "Role ID and permission IDs are required");
        }

        Optional<Role> roleOptional = roleService.findById(request.getRoleId());
        if (roleOptional.isEmpty()) {
            return ResponseUtil.buildResponse(HttpStatus.NOT_FOUND, "Role not found");
        }

        Role role = roleOptional.get();

        Set<Long> existingPermissionIds = role.getPermissions().stream()
                .map(Permission::getId)
                .collect(Collectors.toSet());

        Set<Long> requestedPermissionIds = new HashSet<>(request.getPermissionIds());
        requestedPermissionIds.removeAll(existingPermissionIds);

        if (requestedPermissionIds.isEmpty()) {
            return ResponseUtil.buildResponse(HttpStatus.BAD_REQUEST, "All permissions already exist in this role");
        }

        List<Permission> permissionsToAdd = permissionService.getPermissionById(new ArrayList<>(requestedPermissionIds));

        if (permissionsToAdd.isEmpty()) {
            return ResponseUtil.buildResponse(HttpStatus.BAD_REQUEST, "No valid permissions found in database");
        }

        role.getPermissions().addAll(permissionsToAdd);
        roleService.updateRole(role);

        return ResponseUtil.buildResponse(HttpStatus.OK, role);
    }
}


