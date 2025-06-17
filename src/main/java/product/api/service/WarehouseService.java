package product.api.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import product.api.dto.WarehouseDTO;
import product.api.entity.Warehouse;
import product.api.exception.NotFoundException;
import product.api.repository.WarehouseRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class WarehouseService {

    private final WarehouseRepository warehouseRepository;

    public WarehouseService(WarehouseRepository warehouseRepository) {
        this.warehouseRepository = warehouseRepository;
    }

    public Optional<Warehouse> getWarehouseById(Long id) {
        return warehouseRepository.findById(id);
    }

    public List<Warehouse> getAllWarehouses() {
        return warehouseRepository.findAll();
    }

    public Warehouse createWarehouse(Warehouse warehouse) {
        return warehouseRepository.save(warehouse);
    }
    public Warehouse findById(Long id){
        return warehouseRepository.findById(id).orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND,"Warehouse Not Found"));
    }

    public Warehouse updateWarehouse(Long id, WarehouseDTO warehouseDTO) {
        Warehouse warehouse = findById(id);

        warehouse.setName(warehouseDTO.getName());
        warehouse.setLocation(warehouseDTO.getLocation());
        warehouse.setCapacity(warehouseDTO.getCapacity());
        warehouse.setUpdatedAt(LocalDateTime.now());

        return warehouseRepository.save(warehouse);
    }

    public void deleteWarehouse(Long id) {
        warehouseRepository.deleteById(id);
    }

    public Warehouse findWarehouse(Long id) {
        return warehouseRepository.findById(id).orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND,"Warehouse Not Found"));
    }
}
