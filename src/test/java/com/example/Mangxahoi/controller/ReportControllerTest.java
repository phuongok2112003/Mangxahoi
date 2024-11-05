package com.example.Mangxahoi.controller;

import com.example.Mangxahoi.services.ReportService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ReportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReportService reportService;


    @Test
    @WithMockUser
    void getWeeklyReport_ShouldReturnFile_WhenSuccessful() throws Exception {

        byte[] mockReportData = "Sample report data".getBytes();

        // Mock ReportService's generateReport() to return mock data
        Mockito.when(reportService.generateReport()).thenReturn(mockReportData);

        // Perform request and assert response
       mockMvc.perform(get("/report")
                       .contentType(MediaType.APPLICATION_OCTET_STREAM)
                       .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=weekly_report.xlsx")
               )
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
    }

    @Test
    @WithMockUser
    void getWeeklyReport_ShouldReturnInternalServerError_WhenIOExceptionOccurs() throws Exception {
        // Mock ReportService to throw an IOException
        Mockito.when(reportService.generateReport()).thenThrow(new IOException("File generation failed"));

        // Perform request and assert response status as INTERNAL_SERVER_ERROR
        mockMvc.perform(get("/report"))
                .andExpect(status().isInternalServerError())
                .andDo(print());
    }
}
