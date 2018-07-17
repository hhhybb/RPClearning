package com.liip.rpc;

import java.io.*;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @author zhouke
 * @create 2018-07-16 下午4:44
 **/

public class RPCClient {

    public <T> T getRemoteProxy(Class<?> interfaceClasses, InetSocketAddress addr) {
        return (T) Proxy.newProxyInstance(interfaceClasses.getClassLoader(), new Class<?>[]{interfaceClasses}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                Socket socket = null;
                ObjectOutputStream serializer = null;
                ObjectInputStream deSerializer = null;

                try {
                    socket = new Socket();
                    socket.connect(addr);
                    serializer = new ObjectOutputStream(socket.getOutputStream());
                    serializer.writeUTF(interfaceClasses.getName());
                    serializer.writeUTF(method.getName());
                    serializer.writeObject(method.getParameterTypes());
                    serializer.writeObject(args);

                    deSerializer = new ObjectInputStream(socket.getInputStream());
                    return deSerializer.readObject();

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (deSerializer!=null) {
                            deSerializer.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    try {
                        if (serializer != null) {
                            serializer.flush();
                            serializer.close();
                        }
                    } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    try {
                        if (socket != null) {
                            socket.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                return null;
            }
        });
    }
}
