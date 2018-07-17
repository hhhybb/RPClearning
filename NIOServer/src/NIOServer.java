import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * @author zhouke
 * @create 2018-07-17 下午5:16
 **/

public class NIOServer {
    private int port;
    private Selector selector;

    public NIOServer(int port) {
        this.port = port;
    }

    public void initServer() throws IOException {
        ServerSocketChannel socketChannel = ServerSocketChannel.open();
        socketChannel.configureBlocking(false);
        socketChannel.bind(new InetSocketAddress("localhost",port));
        this.selector = Selector.open();
        socketChannel.register(selector, SelectionKey.OP_ACCEPT);

    }

    public void listen() throws IOException {
        System.out.println("NIO server start");
        while (true) {
            selector.select();
            Iterator<SelectionKey> iterator = this.selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();
                handler(key);
            }
        }
    }

    private void handler(SelectionKey key) throws IOException {
        if (key.isAcceptable()) {
            handlerAccept(key);
        } else if (key.isReadable()) {
            handlerReader(key);
        }
    }

    private void handlerReader(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        //不会阻塞
        int n = socketChannel.read(buffer);
        System.out.println(n);
        if (n > 0) {
            byte[] data = buffer.array();
            System.out.println("服务端收到信息:" + new String(data, 0, n));
            buffer.flip();
            socketChannel.write(buffer);
        } else {
            System.out.println("clinet is close");
            key.cancel();
        }

    }

    private void handlerAccept(SelectionKey key) throws IOException {
        ServerSocketChannel sever = (ServerSocketChannel) key.channel();
        SocketChannel channel = sever.accept();
        channel.configureBlocking(false);
        System.out.println("有客服端连接来了" + channel.toString());
        channel.register(this.selector, SelectionKey.OP_READ);
    }
}
