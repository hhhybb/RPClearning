package com.liip.entity;

import java.io.Serializable;

/**
 * @author zhouke
 * @create 2018-07-16 下午3:40
 **/

public class BookInfo implements Serializable {
    private int bookId;
    private String bookName;

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    @Override
    public String toString() {
        return "BookInfo{" +
                "bookId=" + bookId +
                ", bookName='" + bookName + '\'' +
                '}';
    }
}
