package product.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import product.api.dto.InventoryReportDTO;
import product.api.dto.TransactionReportDTO;
import product.api.response.Response;
import product.api.service.ReportService;
import product.api.utils.ResponseUtil;

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
    public ResponseEntity<Response> getInventoryReport() {
        List<InventoryReportDTO> report = reportService.getInventoryReport();
        return ResponseUtil.buildResponse(HttpStatus.OK, report);
    }

    @GetMapping("/transactions")
    public ResponseEntity<Response> getTransactionReport(
            @RequestParam LocalDate dateFrom,
            @RequestParam LocalDate dateTo) {
        List<TransactionReportDTO> report = reportService.getTransactionReport(dateFrom, dateTo);
        return ResponseUtil.buildResponse(HttpStatus.OK, report);
    }
}

