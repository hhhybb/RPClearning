import java.io.IOException;

/**
 * @author zhouke
 * @create 2018-07-17 下午1:48
 **/

public class BIOServerTest {
    public static void main(String[] args) {
        BIOServer bioServer = new BIOServer(12345);
        try {
            bioServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
