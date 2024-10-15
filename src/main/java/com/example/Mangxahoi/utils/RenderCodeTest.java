package com.example.Mangxahoi.utils;

import java.util.Random;

public class RenderCodeTest {
    public static String setValue(){
        int length = 4;
        Random random = new Random();
        StringBuilder code = new StringBuilder();

        for (int i = 0; i < length; i++) {
            code.append(random.nextInt(10));  // Số ngẫu nhiên từ 0 đến 9
        }

        return code.toString();
    }
}
