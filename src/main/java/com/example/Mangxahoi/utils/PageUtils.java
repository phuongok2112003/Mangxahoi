package com.example.Mangxahoi.utils;


import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@AllArgsConstructor
public class PageUtils {



    public static int getPageIndex(Integer pageIndex) {
        return pageIndex != null && pageIndex > 0 ? pageIndex - 1 : 1;
    }

    public static int getPageSize(Integer pageSize) {
        return pageSize != null && pageSize > 0 ? pageSize : 10;
    }

    public static Pageable getPageable(Integer pageIndex, Integer pageSize) {
        pageIndex = getPageIndex(pageIndex);
        pageSize = getPageSize(pageSize);
        return PageRequest.of(pageIndex, pageSize);
    }

    public static Pageable getPageableWithSort(Integer pageIndex, Integer pageSize, Sort sort) {
        pageIndex = getPageIndex(pageIndex);
        pageSize = getPageSize(pageSize);
        return PageRequest.of(pageIndex, pageSize, sort);
    }
}
