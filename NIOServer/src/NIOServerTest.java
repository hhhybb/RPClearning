import java.io.IOException;

/**
 * @author zhouke
 * @create 2018-07-17 下午5:41
 **/

public class NIOServerTest {
    public static void main(String[] args) {
        NIOServer nioServer = new NIOServer(12345);
        try {
            nioServer.initServer();
            nioServer.listen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
