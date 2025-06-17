package product.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import product.api.dto.InventoryTransactionRequest;
import product.api.response.InventoryTransactionResponse;
import product.api.response.Response;
import product.api.service.InventoryTransactionService;
import product.api.utils.ResponseUtil;

@RestController
@RequestMapping("/inventory-transactions")
public class InventoryTransactionController {

    private final InventoryTransactionService inventoryTransactionService;

    public InventoryTransactionController(InventoryTransactionService inventoryTransactionService) {
        this.inventoryTransactionService = inventoryTransactionService;
    }

    @PostMapping
    public ResponseEntity<Response> recordTransaction(@RequestBody InventoryTransactionRequest request) {
        InventoryTransactionResponse transactionResponse = inventoryTransactionService.recordTransaction(request);
        return ResponseUtil.buildResponse(HttpStatus.OK, transactionResponse);
    }
}
