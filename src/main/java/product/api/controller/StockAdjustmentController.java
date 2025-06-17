package product.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import product.api.dto.StockAdjustmentRequest;
import product.api.response.Response;
import product.api.response.StockAdjustmentResponse;
import product.api.service.StockAdjustmentService;
import product.api.utils.ResponseUtil;

import java.util.List;

@RestController
@RequestMapping("/stock-adjustments")
public class StockAdjustmentController {

    private final StockAdjustmentService stockAdjustmentService;

    public StockAdjustmentController(StockAdjustmentService stockAdjustmentService) {
        this.stockAdjustmentService = stockAdjustmentService;
    }

    @PostMapping
    public ResponseEntity<Response> createStockAdjustment(@RequestBody StockAdjustmentRequest request) {
        StockAdjustmentResponse response = stockAdjustmentService.createStockAdjustment(request);
        return ResponseUtil.buildResponse(HttpStatus.CREATED, response);
    }

    @GetMapping
    public ResponseEntity<Response> getStockAdjustments(@RequestParam Long productId) {
        List<StockAdjustmentResponse> adjustments = stockAdjustmentService.getStockAdjustmentsByProduct(productId);
        return ResponseUtil.buildResponse(HttpStatus.OK, adjustments);
    }
}
