package product.api.service;

import org.springframework.stereotype.Service;
import product.api.entity.Role;
import product.api.repository.RoleRepository;

import java.util.Optional;

@Service
public class RoleService {

    final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Optional<Role> findById(Long id ){
        return roleRepository.findById(id);
    }

    public void updateRole(Role role){
        roleRepository.save(role);
    }
    public Role findByName(String name){
        return roleRepository.findByName(name).orElseThrow(() -> new RuntimeException("Role USER not found"));
    }
}
