import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author zhouke
 * @create 2018-07-17 下午1:49
 **/

public class BIOServer {
    private int port;

    public BIOServer(int port) {
        this.port = port;
    }

    public void start() throws IOException {
        ServerSocket serverSocket = new ServerSocket();
        serverSocket.bind(new InetSocketAddress("localhost",port));
        System.out.println("BIO server start");
        Socket socket = null;
        // 创建线程池，使用ThreadPoolExecutor创建，不要使用Excutors,规避资源耗尽的风险，Excutors创建的线程池有几个最大值是Integer.Max_value.
        ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(5,10,200, TimeUnit.MILLISECONDS,new ArrayBlockingQueue<Runnable>(10));
        try {
            while (true) {
                socket = serverSocket.accept();
                poolExecutor.execute(new SocketHandler(socket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (socket!=null) {
                socket.close();
            }
            if (serverSocket!=null) {
                serverSocket.close();
            }
        }
    }

    private class SocketHandler implements Runnable {
        private Socket socket;
        private static final String line = "\r\n";

        SocketHandler(Socket socket) {
            this.socket = socket;
        }
        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName()+"在运行");
            BufferedReader br = null;
            PrintWriter pw = null;
            try {
                br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                pw = new PrintWriter(socket.getOutputStream());
                Thread.sleep(3);
                String input = null;
                while ((input = br.readLine())!=null && input.length()!=0) {
                    System.out.println(input);
                }

                System.out.println("返回客户端");
                StringBuffer responseContext = new StringBuffer();
                responseContext.append("<html><head></head><body>BIO Server</body></html>");

                StringBuffer responseHeader = new StringBuffer();
                responseHeader.append("HTTP/1.1 200 OK").append(line);
                responseHeader.append("Content-Type:text/html;charset=utf-8").append(line);
                responseHeader.append("Content-Length:").append(responseContext.toString().getBytes().length).append(line);
                responseHeader.append(line);
                responseHeader.append(responseContext);
                System.out.println(responseHeader.toString());
                Thread.sleep(5);
                pw.write(responseHeader.toString());
                pw.flush();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                if (br!=null) try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (pw!=null) {
                    pw.flush();
                    pw.close();
                }
            }

        }
    }

}
