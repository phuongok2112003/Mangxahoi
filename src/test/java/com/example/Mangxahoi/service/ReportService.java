package com.example.Mangxahoi.service;

import com.example.Mangxahoi.entity.UserEntity;
import com.example.Mangxahoi.repository.CommentRepository;
import com.example.Mangxahoi.repository.FriendRepository;
import com.example.Mangxahoi.repository.LikeRepository;
import com.example.Mangxahoi.repository.PostRepository;
import com.example.Mangxahoi.services.Impl.ReportServiceIml;
import com.example.Mangxahoi.utils.DateUtil;
import com.example.Mangxahoi.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.Instant;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
@RequiredArgsConstructor
@Slf4j
public class ReportService {

    @Mock
    private PostRepository postRepository;

    @Mock
    private FriendRepository friendRepository;

    @Mock
    private LikeRepository likeRepository;

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private ReportServiceIml reportService;

    private UserEntity mockUser;

    @BeforeEach
    void setUp() {
        mockUser = new UserEntity();
        mockUser.setId(1L);
    }

    @Test
    void testGenerateReport() throws IOException {
        Instant startDate = Instant.parse("2024-01-01T00:00:00Z");
        Instant endDate = Instant.parse("2024-01-07T23:59:59Z");

        try (MockedStatic<DateUtil> mockedDateUtil = mockStatic(DateUtil.class);
             MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {

            // Mock DateUtil and SecurityUtils
            mockedDateUtil.when(DateUtil::getStartOfWeek).thenReturn(startDate);
            mockedDateUtil.when(DateUtil::getEndOfWeek).thenReturn(endDate);
            mockedSecurityUtils.when(SecurityUtils::getCurrentUser).thenReturn(mockUser);

            // Mock repository method calls
            when(postRepository.countByUserIdAndCreatedAtBetween(mockUser.getId(), startDate, endDate)).thenReturn(10L);
            when(friendRepository.countNewFriends(mockUser.getId(), startDate, endDate)).thenReturn(5L);
            when(likeRepository.countByUserIdAndCreatedAtBetween(mockUser.getId(), startDate, endDate)).thenReturn(20L);
            when(commentRepository.countByUserIdAndCreatedAtBetween(mockUser.getId(), startDate, endDate)).thenReturn(15L);

            // Call the method to test
            byte[] reportData = reportService.generateReport();

            // Verify the byte array is not empty and represents a valid Excel workbook
            assertNotNull(reportData);
            assertTrue(reportData.length > 0);

            // Validate the generated Excel content
            try (ByteArrayInputStream bis = new ByteArrayInputStream(reportData);
                 XSSFWorkbook workbook = new XSSFWorkbook(bis)) {
                assertEquals(1, workbook.getNumberOfSheets());

                // Verify data in the sheet
                var sheet = workbook.getSheetAt(0);
                assertEquals("Report Item", sheet.getRow(0).getCell(0).getStringCellValue());
                assertEquals("Value", sheet.getRow(0).getCell(1).getStringCellValue());
                assertEquals("Số bài đã viết tuần qua", sheet.getRow(1).getCell(0).getStringCellValue());
                assertEquals(10, (int) sheet.getRow(1).getCell(1).getNumericCellValue());
                assertEquals("Số bạn bè mới tuần qua", sheet.getRow(2).getCell(0).getStringCellValue());
                assertEquals(5, (int) sheet.getRow(2).getCell(1).getNumericCellValue());
                assertEquals("Số lượt like mới tuần qua", sheet.getRow(3).getCell(0).getStringCellValue());
                assertEquals(20, (int) sheet.getRow(3).getCell(1).getNumericCellValue());
                assertEquals("Số comment mới tuần qua", sheet.getRow(4).getCell(0).getStringCellValue());
                assertEquals(15, (int) sheet.getRow(4).getCell(1).getNumericCellValue());
            }
        }
    }
}