package com.madeyepeople.pocketpt.global.common;

import lombok.Data;

@Data
public class Pagination {
    private int pageSize;		// 한 페이지에 보일 게시물 사이즈
    private int totalPage;		// 총 페이지 갯수
    private int pageNum;		// 현재시작 페이지(current)
    private int pageNavSize;	// 페이지 보여줄 단위(사이즈)
    private long totalRecord;	// 총 게시물(레코드) 갯수
    private long startRowIndex;	// 게시물의 시작번호(거꾸로시작)
    private int firstPageNo;	// 페이지의 시작번호
    private int lastPageNo;		// 페이지의 마지막번호

    private boolean hasPreviousPageNav;	// 이전페이지
    private boolean hasNextPageNav;		// 다음페이지
    private boolean hasFirstPageNav;	// 맨 첫번째 페이지
    private boolean hasLastPageNav;		// 맨 마지막 페이지

    public Pagination(int pageNum, long totalRecord, int pageSize,  int pageNavSize) {
        this.pageNum = pageNum;
        this.totalRecord = totalRecord;
        this.pageSize = pageSize;
        this.pageNavSize = pageNavSize;

        if (this.pageNum < 1) {
            this.pageNum=1;
        }

        if (this.pageNavSize < 1) {
            this.pageSize=10;
        }
        this.totalRecord = totalRecord;
        calculation();
    }

    private void calculation() {
        /* 페이지 수*/
        this.totalPage = (int) (((this.totalRecord - 1) / this.pageSize) + 1);
        if (this.pageNum > this.totalPage)
            this.pageNum = this.totalPage;

        /* 현재 페이지번호로 페이지 링크 블럭의 시작페이지번호와 마지막페이지번호 취득 */
        this.firstPageNo = (((int) Math.ceil((float)this.pageNum/(float)this.pageNavSize))-1)*this.pageNavSize+1;
        this.lastPageNo = this.firstPageNo + this.pageNavSize-1;
        if (this.lastPageNo > this.totalPage)
            this.lastPageNo = this.totalPage;

        this.startRowIndex = this.totalRecord - ((this.pageNum-1) * this.pageSize);

        hasPreviousPageNav =  this.firstPageNo == 1 ? false : true;
        hasNextPageNav =(this.lastPageNo * this.pageSize) < this.totalRecord;

        hasFirstPageNav =  (this.pageNum > 1 && this.pageNavSize < this.pageNum);
        hasLastPageNav = (this.pageNum < this.totalPage && this.lastPageNo + 1 < this.totalPage );
    }
}
