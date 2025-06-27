package product.api.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import product.api.dto.InventoryTransactionRequest;
import product.api.entity.InventoryTransaction;
import product.api.response.InventoryTransactionResponse;
import product.api.response.Response;
import product.api.service.ExcelExportService;
import product.api.service.InventoryTransactionService;
import product.api.utils.ResponseUtil;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/inventory-transactions")
public class InventoryTransactionController {

    private final InventoryTransactionService inventoryTransactionService;

    private final ExcelExportService excelExportService;

    public InventoryTransactionController(InventoryTransactionService inventoryTransactionService, ExcelExportService excelExportService) {
        this.inventoryTransactionService = inventoryTransactionService;
        this.excelExportService = excelExportService;
    }

    @PostMapping
    public ResponseEntity<Response> recordTransaction(@RequestBody InventoryTransactionRequest request) {
        InventoryTransactionResponse transactionResponse = inventoryTransactionService.recordTransaction(request);
        return ResponseUtil.buildResponse(HttpStatus.OK, transactionResponse);
    }

    @GetMapping
    public ResponseEntity<Response> getTransactions(
            @RequestParam(required = false) Long productId,
            @RequestParam(required = false) Long warehouseId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<InventoryTransaction> transactions = inventoryTransactionService.getTransactions(productId, warehouseId,fromDate,toDate, pageable);
        Page<InventoryTransactionResponse> responsePage = transactions.map(InventoryTransactionResponse::convertTransaction);
        return ResponseUtil.buildResponse(HttpStatus.OK, responsePage);
    }

    @GetMapping("/export")
    public void exportToExcel(
            @RequestParam(required = false) Long productId,
            @RequestParam(required = false) Long warehouseId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            HttpServletResponse response
    ) throws IOException {
        List<InventoryTransaction> transactions = inventoryTransactionService.getTransactions(productId, warehouseId, fromDate, toDate);
        List<InventoryTransactionResponse> responseList = transactions.stream()
                .map(InventoryTransactionResponse::convertTransaction)
                .toList();

        excelExportService.exportInventoryTransaction(responseList, response);
    }

}
