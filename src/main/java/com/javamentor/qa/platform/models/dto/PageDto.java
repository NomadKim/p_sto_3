package com.javamentor.qa.platform.models.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PageDto<T> {

    private int currentPageNumber;
    private int totalPageCount;
    private int totalResultCount;
    private List<T> items;
    private int itemsOnPage;

    @Override
    public String toString() {
        return "PageDto{" +
                "currentPageNumber=" + currentPageNumber +
                ", totalPageCount=" + totalPageCount +
                ", totalResultCount=" + totalResultCount +
                ", items=" + items +
                ", itemsOnPage=" + itemsOnPage +
                '}';
    }
}