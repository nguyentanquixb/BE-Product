package product.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import product.api.dto.WarehouseDTO;
import product.api.entity.Warehouse;
import product.api.response.Response;
import product.api.response.WarehouseResponse;
import product.api.service.WarehouseService;
import product.api.utils.ResponseUtil;

import java.util.List;

@RestController
@RequestMapping("/warehouses")
public class WarehouseController {

    private final WarehouseService warehouseService;

    public WarehouseController(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('VIEW_WAREHOUSE')")
    public ResponseEntity<Response> getAllWarehouses() {
        List<Warehouse> warehouses = warehouseService.getAllWarehouses();
        List<WarehouseResponse>  warehouseResponseList = warehouses.stream().map(WarehouseResponse::convertWarehouse).toList();

        return ResponseUtil.buildResponse(HttpStatus.OK, warehouseResponseList);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('VIEW_WAREHOUSE')")
    public ResponseEntity<Response> getWarehouseById(@PathVariable Long id) {
        Warehouse  warehouse = warehouseService.findWarehouse(id);
        WarehouseResponse warehouseResponse = WarehouseResponse.convertWarehouse(warehouse);
        return ResponseUtil.buildResponse(HttpStatus.OK, warehouseResponse);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('CREATE_WAREHOUSE')")
    public ResponseEntity<Response> createWarehouse(@RequestBody Warehouse warehouse) {
        return ResponseUtil.buildResponse(HttpStatus.OK, warehouseService.createWarehouse(warehouse));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('UPDATE_WAREHOUSE')")
    public ResponseEntity<Response> updateWarehouse(@PathVariable Long id, @RequestBody WarehouseDTO warehouseDTO) {
        return ResponseUtil.buildResponse(HttpStatus.OK, warehouseService.updateWarehouse(id, warehouseDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('DELETE_WAREHOUSE')")
    public ResponseEntity<Response> deleteWarehouse(@PathVariable Long id) {
        warehouseService.deleteWarehouse(id);
        return ResponseUtil.buildResponse(HttpStatus.OK, id);
    }
}

