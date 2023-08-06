package com.madeyepeople.pocketpt.global.common;

import lombok.Data;

@Data
public abstract class ScrollPagination {
    protected int pageSize; // 한 페이지 당 가져올 데이터 개수
    protected int pageNum; // 페이지 번호
    protected int startNum; // 시작 데이터 num
    protected int totalRecord;	// 총 게시물(레코드) 갯수

    protected boolean hasPreviousPage;	// 이전페이지
    protected boolean hasNextPage; // 다음페이지

    public ScrollPagination(int pageSize, int pageNum, int totalRecord) {
        this.pageSize = pageSize;
        this.pageNum = pageNum;
        this.totalRecord = totalRecord;

        calculateStartNum();
        calculatePageStatus();
    }

//    // 페이지 정보 계산 ASC
//    private void calculateStartNum() {
//        startNum = (pageNum - 1) * pageSize + 1;
//    }

    // 페이지 정보 계산 DESC
    abstract void calculateStartNum();

    private void calculatePageStatus() {
        hasPreviousPage = pageNum > 1;
        hasNextPage = (pageNum * pageSize) < totalRecord;
    }
}
