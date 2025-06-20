package product.api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import product.api.entity.Permission;
import product.api.repository.PermissionRepository;

@ExtendWith(MockitoExtension.class)
public class PermissionServiceTest {

    @Mock
    private PermissionRepository permissionRepository;

    @InjectMocks
    private PermissionService permissionService;

    @Test
    public void crPermission_SuccessTest() {

        Permission permission1 = new Permission();
        permission1.setId(1L);
        permission1.setName("CREATE-PRODUCT");
        Permission permission2 = new Permission();
        permission2.setId(2L);
        permission2.setName("UPDATE-PRODUCT");

        List<Permission> permissions = Arrays.asList(permission1, permission2);

        when(permissionRepository.saveAll(anyList())).thenReturn(permissions);

        permissionService.crPermission(permissions);

    }

    @Test
    public void getPermissionById_SuccessTest() {

        Permission permission1 = new Permission();
        permission1.setId(1L);
        permission1.setName("CREATE-PRODUCT");

        Permission permission2 = new Permission();
        permission2.setId(2L);
        permission2.setName("UPDATE-PRODUCT");

        List<Permission> permissions = Arrays.asList(permission1, permission2);
        List<Long> permissionIds = Arrays.asList(1L, 2L);

        when(permissionRepository.findAllById(anyList())).thenReturn(permissions);

        List<Permission> result = permissionService.getPermissionById(permissionIds);

        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(p -> p.getName().equals("UPDATE-PRODUCT")));
        assertTrue(result.stream().anyMatch(p -> p.getName().equals("CREATE-PRODUCT")));
    }

    @Test
    public void getAllPermissionNames_SuccessTest() {

        Permission permission1 = new Permission();
        permission1.setId(1L);
        permission1.setName("UPDATE-PRODUCT");

        Permission permission2 = new Permission();
        permission2.setId(2L);
        permission2.setName("CREATE-PRODUCT");

        List<Permission> permissions = Arrays.asList(permission1, permission2);
        Set<String> expectedNames = new HashSet<>(Arrays.asList("UPDATE-PRODUCT", "CREATE-PRODUCT"));

        when(permissionRepository.findAll()).thenReturn(permissions);

        Set<String> result = permissionService.getAllPermissionNames();

        assertEquals(expectedNames, result);
    }
}