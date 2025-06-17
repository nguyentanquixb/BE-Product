package product.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import product.api.dto.SalesOrderRequest;
import product.api.response.Response;
import product.api.service.SalesOrderService;
import product.api.utils.ResponseUtil;

@RestController
@RequestMapping("/sales-orders")
public class SalesOrderController {

    private final SalesOrderService salesOrderService;

    public SalesOrderController(SalesOrderService salesOrderService) {
        this.salesOrderService = salesOrderService;
    }

    @PostMapping
    public ResponseEntity<Response> createSalesOrder(@RequestBody SalesOrderRequest request) {
        salesOrderService.createSalesOrder(request);
        return ResponseUtil.buildResponse(HttpStatus.CREATED, request);
    }
}
