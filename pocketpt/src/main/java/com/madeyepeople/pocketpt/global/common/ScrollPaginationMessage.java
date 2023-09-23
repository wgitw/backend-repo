package com.madeyepeople.pocketpt.global.common;

public class ScrollPaginationMessage extends ScrollPagination {
    public ScrollPaginationMessage(int pageSize, int pageNum, int totalRecord) {
        super(pageSize, pageNum, totalRecord);
    }

    @Override
    void calculateStartNum() {
        startNum = (int) pageNum * pageSize;
    }
}
