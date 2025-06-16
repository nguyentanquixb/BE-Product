package product.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import product.api.dto.InventoryReportDTO;
import product.api.dto.TransactionReportDTO;
import product.api.service.ReportService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/inventory")
    public ResponseEntity<List<InventoryReportDTO>> getInventoryReport() {
        List<InventoryReportDTO> report = reportService.getInventoryReport();
        return ResponseEntity.ok(report);
    }

    @GetMapping("/transactions")
    public ResponseEntity<List<TransactionReportDTO>> getTransactionReport(
            @RequestParam LocalDate dateFrom,
            @RequestParam LocalDate dateTo) {
        List<TransactionReportDTO> report = reportService.getTransactionReport(dateFrom, dateTo);
        return ResponseEntity.ok(report);
    }
}

