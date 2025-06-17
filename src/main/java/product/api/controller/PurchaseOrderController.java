package product.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import product.api.dto.PurchaseOrderRequest;
import product.api.response.PurchaseOrderResponse;
import product.api.response.Response;
import product.api.service.PurchaseOrderService;
import product.api.utils.ResponseUtil;

@RestController
@RequestMapping("/purchase-orders")
public class PurchaseOrderController {

    private final PurchaseOrderService purchaseOrderService;

    public PurchaseOrderController(PurchaseOrderService purchaseOrderService) {
        this.purchaseOrderService = purchaseOrderService;
    }

    @PostMapping
    public ResponseEntity<Response> createPurchaseOrder(@RequestBody PurchaseOrderRequest request) {
        PurchaseOrderResponse purchaseOrderResponse = purchaseOrderService.createPurchaseOrder(request);
        return ResponseUtil.buildResponse(HttpStatus.CREATED, purchaseOrderResponse);
    }
}

