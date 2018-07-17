package com.liip.rpc;

import com.liip.service.BookService;
import com.liip.service.impl.BookServiceImp;

import java.io.IOException;

/**
 * @author zhouke
 * @create 2018-07-16 下午4:38
 **/

public class RPCServerTest {
    public static void main(String[] args) {
        RPCServer server = new RPCServer(12345);
        server.registryService(BookService.class, BookServiceImp.class);
        try {
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
            server.stop();
        }
    }
}
