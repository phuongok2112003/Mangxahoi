package com.example.Mangxahoi.controller;

import com.example.Mangxahoi.services.ReportService;
import com.example.Mangxahoi.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/report")
@Slf4j
public class ReportController {
    private  final ReportService reportService;

    @GetMapping("")
    public ResponseEntity<byte[]> getWeeklyReport() {
        try {
            byte[] reportData = reportService.generateReport();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "weekly_report of "+SecurityUtils.getCurrentUser().getUsername()+".xlsx");
            headers.setContentLength(reportData.length);

            return new ResponseEntity<>(reportData, headers, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
