package product.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import product.api.response.Response;
import product.api.service.InventoryService;

@RestController
@RequestMapping("/Inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('VIEW_INVENTORY')")
    public ResponseEntity<Response> getProductsByWarehouse(
            @RequestParam Long warehouseId,
            @RequestParam(required = false) String status) {
        return ResponseEntity.ok(Response.ok(inventoryService.getProductsByWarehouse(warehouseId, status)));
    }

    @GetMapping("/low-stock")
    @PreAuthorize("hasAuthority('VIEW_INVENTORY')")
    public ResponseEntity<Response> getLowStockProducts() {
        return ResponseEntity.ok(Response.ok(inventoryService.getLowStockProducts()));
    }
}
