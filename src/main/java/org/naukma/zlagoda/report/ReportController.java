package org.naukma.zlagoda.report;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/report")
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;

    @GetMapping("/{type}")
    @SneakyThrows
    public ResponseEntity<Resource> getReport(@PathVariable ReportType type) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline" + "; filename="
                        + URLEncoder.encode((type.name().charAt(0)
                        + type.name().toLowerCase().replace("_", " ").substring(1)) + ".pdf",
                        StandardCharsets.UTF_8).replace("+", "%20"))
                .body(reportService.report(type));
    }

    @GetMapping("/check/{id}")
    @SneakyThrows
    public ResponseEntity<Resource> getReport(@PathVariable Integer id) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline" + "; filename="
                        + URLEncoder.encode("check№" + id + ".pdf",
                        StandardCharsets.UTF_8).replace("+", "%20"))
                .body(reportService.report(id));
    }
}
