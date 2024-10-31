package com.example.Mangxahoi.services.Impl;

import com.example.Mangxahoi.entity.UserEntity;
import com.example.Mangxahoi.repository.CommentRepository;
import com.example.Mangxahoi.repository.FriendRepository;
import com.example.Mangxahoi.repository.LikeRepository;
import com.example.Mangxahoi.repository.PostRepository;
import com.example.Mangxahoi.services.ReportService;
import com.example.Mangxahoi.utils.DateUtil;
import com.example.Mangxahoi.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class ReportServiceIml implements ReportService {
    private final PostRepository postRepository;
    private final FriendRepository friendRepository;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;
    @Override
    public byte[] generateReport() throws IOException {
        Instant startDate = DateUtil.getStartOfWeek();
        Instant endDate = DateUtil.getEndOfWeek();

        UserEntity userEntity= SecurityUtils.getCurrentUser();
        long postsCount = postRepository.countByUserIdAndCreatedAtBetween(userEntity.getId(), startDate,endDate);
        long newFriendsCount = friendRepository.countNewFriends(userEntity.getId(), startDate,endDate);
        long likesCount = likeRepository.countByUserIdAndCreatedAtBetween(userEntity.getId(), startDate,endDate);
        long commentsCount = commentRepository.countByUserIdAndCreatedAtBetween(userEntity.getId(), startDate,endDate);

        // Tạo workbook và sheet
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("User Report");

        // Tạo header
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Report Item");
        headerRow.createCell(1).setCellValue("Value");

        // Điền dữ liệu vào file
        sheet.createRow(1).createCell(0).setCellValue("Số bài đã viết tuần qua");
        sheet.getRow(1).createCell(1).setCellValue(postsCount);

        sheet.createRow(2).createCell(0).setCellValue("Số bạn bè mới tuần qua");
        sheet.getRow(2).createCell(1).setCellValue(newFriendsCount);

        sheet.createRow(3).createCell(0).setCellValue("Số lượt like mới tuần qua");
        sheet.getRow(3).createCell(1).setCellValue(likesCount);

        sheet.createRow(4).createCell(0).setCellValue("Số comment mới tuần qua");
        sheet.getRow(4).createCell(1).setCellValue(commentsCount);

        // Ghi dữ liệu ra mảng byte để trả về response
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            workbook.write(outputStream);
            return outputStream.toByteArray();
        } finally {
            workbook.close();
        }
    }

}
