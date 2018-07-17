package com.liip.rpc;

import com.liip.entity.BookInfo;
import com.liip.service.BookService;

import java.net.InetSocketAddress;

/**
 * @author zhouke
 * @create 2018-07-16 下午5:26
 **/

public class RPCClientTest {
    public static void main(String[] args) {
        RPCClient client = new RPCClient();
        BookService bookService = client.getRemoteProxy(BookService.class,new InetSocketAddress("192.168.52.144",12345));
        BookInfo bookInfo = new BookInfo();
        bookInfo.setBookId(1);
        bookInfo.setBookName("java rpc");
        String result = bookService.addBook(bookInfo);
        System.out.println(result);
    }
}
