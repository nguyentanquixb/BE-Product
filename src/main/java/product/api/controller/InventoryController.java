package product.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import product.api.response.Response;
import product.api.service.InventoryService;
import product.api.utils.ResponseUtil;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('VIEW_INVENTORY ')")
    public ResponseEntity<Response> getProductsByWarehouse(
            @RequestParam Long warehouseId,
            @RequestParam(required = false) String status) {
        return ResponseUtil.buildResponse(HttpStatus.OK, inventoryService.getProductsByWarehouse(warehouseId, status));
    }

    @GetMapping("/low-stock")
    @PreAuthorize("hasAuthority('VIEW_LOW_STOCK')")
    public ResponseEntity<Response> getLowStockProducts() {
        return ResponseUtil.buildResponse(HttpStatus.OK, inventoryService.getLowStockProducts());
    }
}
