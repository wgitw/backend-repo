package com.madeyepeople.pocketpt.global.common;

import lombok.Data;


public class ScrollPaginationFile extends ScrollPagination{
    public ScrollPaginationFile(int pageSize, int pageNum, int totalRecord) {
        super(pageSize, pageNum, totalRecord);
    }

    @Override
    void calculateStartNum() {
        startNum = (int) (pageSize * pageNum);
    }
}
