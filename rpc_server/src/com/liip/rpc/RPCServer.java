package com.liip.rpc;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author zhouke
 * @create 2018-07-16 下午3:53
 **/

public class RPCServer {

    private int serverPort;

    Map<String,Class<?>> serverRegistry = Collections.synchronizedMap(new HashMap<>());

    ThreadPoolExecutor pool = new ThreadPoolExecutor(5,30,200, TimeUnit.MILLISECONDS,new ArrayBlockingQueue<Runnable>(10));

    public RPCServer(int port) {
        this.serverPort = port;
    }

    public void start() throws IOException {

        ServerSocket serverSocket = new ServerSocket();
        serverSocket.bind(new InetSocketAddress(serverPort));
        System.out.println("服务启动中......");

        try {
            while (true) {
                pool.execute(new RpcTask(serverSocket.accept()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(serverSocket!=null) {
                serverSocket.close();
            }
        }
    }

    public void registryService(Class<?> serviceInterface,Class<?> serviceImpl) {
        serverRegistry.put(serviceInterface.getName(),serviceImpl);

    }

    public void stop() {
        pool.shutdown();
    }

    private class RpcTask implements Runnable {

        private final Socket socket;

        public RpcTask(Socket socket) {
            this.socket = socket;
        }
        @Override
        public void run() {
            ObjectInputStream deSerializer = null;
            ObjectOutputStream serializer = null;

            try {
                deSerializer = new ObjectInputStream(socket.getInputStream());
                String interfaceName = deSerializer.readUTF();
                String methodName = deSerializer.readUTF();
                Class<?>[] parameterTypes = (Class<?>[]) deSerializer.readObject();
                Object[] parameters = (Object[]) deSerializer.readObject();

                Class<?> instance = serverRegistry.get(interfaceName);
                Method method = instance.getDeclaredMethod(methodName,parameterTypes);
                Object result = method.invoke(instance.newInstance(),parameters);
                serializer = new ObjectOutputStream(socket.getOutputStream());
                serializer.writeObject(result);


            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (deSerializer!=null) {
                    try {
                        deSerializer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (serializer!=null) {
                    try {
                        serializer.flush();
                        serializer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (socket!=null) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
