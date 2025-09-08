package com.example.history.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PageResponse<T> {
    private int page;

    private int size;

    private long totalElements;

    private int totalPages;

    private List<T> content;

    public PageResponse(List<T> content, int pageNumber, int pageSize, long totalElements) {
        this.page = pageNumber;
        this.size = pageSize;
        this.totalElements = totalElements;
        this.totalPages = (int) Math.ceil(totalElements / (double) size);
        this.content = content;
    }
}