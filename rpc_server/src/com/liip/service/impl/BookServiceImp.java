package com.liip.service.impl;

import com.liip.entity.BookInfo;
import com.liip.service.BookService;

/**
 * @author zhouke
 * @create 2018-07-16 下午3:43
 **/

public class BookServiceImp implements BookService {
    @Override
    public String addBook(BookInfo book) {
        return book.toString();
    }
}
