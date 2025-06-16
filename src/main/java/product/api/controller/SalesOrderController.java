package product.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import product.api.dto.SalesOrderRequest;
import product.api.response.Response;
import product.api.response.SalesOrderResponse;
import product.api.service.SalesOrderService;

@RestController
@RequestMapping("/sales-orders")
public class SalesOrderController {

    private final SalesOrderService salesOrderService;

    public SalesOrderController(SalesOrderService salesOrderService) {
        this.salesOrderService = salesOrderService;
    }

    @PostMapping
    public ResponseEntity<String> createSalesOrder(@RequestBody SalesOrderRequest request) {
        salesOrderService.createSalesOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("SalesOrder created");
    }
}
