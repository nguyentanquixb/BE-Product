package product.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import product.api.dto.WarehouseDTO;
import product.api.entity.Warehouse;
import product.api.response.Response;
import product.api.response.WarehouseResponse;
import product.api.service.WarehouseService;

import java.util.List;

@RestController
@RequestMapping("/warehouses")
public class WarehouseController {

    private final WarehouseService warehouseService;

    public WarehouseController(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    @GetMapping
    public ResponseEntity<Response> getAllWarehouses() {
        List<Warehouse> warehouses = warehouseService.getAllWarehouses();
        List<WarehouseResponse>  warehouseResponseList = warehouses.stream().map(WarehouseResponse::convertWarehouse).toList();

        return ResponseEntity.ok(Response.ok(warehouseResponseList));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> getWarehouseById(@PathVariable Long id) {
        Warehouse  warehouse = warehouseService.findWarehouse(id);
        WarehouseResponse warehouseResponse = WarehouseResponse.convertWarehouse(warehouse);
        return ResponseEntity.ok(Response.ok(warehouseResponse));
    }

    @PostMapping
    public ResponseEntity<Warehouse> createWarehouse(@RequestBody Warehouse warehouse) {
        return ResponseEntity.status(HttpStatus.CREATED).body(warehouseService.createWarehouse(warehouse));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Warehouse> updateWarehouse(@PathVariable Long id, @RequestBody WarehouseDTO warehouseDTO) {
        return ResponseEntity.ok(warehouseService.updateWarehouse(id, warehouseDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteWarehouse(@PathVariable Long id) {
        warehouseService.deleteWarehouse(id);
        return ResponseEntity.ok("warehouses have been deleted");
    }
}

