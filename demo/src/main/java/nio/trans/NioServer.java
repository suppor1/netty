package nio.trans;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author k.jiang
 * 2020/4/21 下午10:33
 * Description nio serverchannel
 */
public class NioServer {
    public static void main(String[] args) {
        try {
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            ServerSocket serverSocket = serverSocketChannel.socket();
            serverSocket.bind(new InetSocketAddress(9000));
            Selector selector = Selector.open();

            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            for (;;) {
                selector.select();

                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> itr = selectionKeys.iterator();

                ByteBuffer byteBuffer = ByteBuffer.wrap("I Am Server\r\n".getBytes());

                while (itr.hasNext()) {
                    SelectionKey key = itr.next();
                    itr.remove();
                    if (key.isAcceptable()) {
                        ServerSocketChannel channel = (ServerSocketChannel) key.channel();
                        SocketChannel clientChannel = channel.accept();
                        System.out.println("New Connect: " + clientChannel);
                        clientChannel.configureBlocking(false);
                        clientChannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
                    }

                    if (key.isWritable()) {
                        SocketChannel  clientChannel = (SocketChannel) key.channel();
                        while ((clientChannel.write(byteBuffer)) == 0) {
                            break;
                        }
                        clientChannel.close();
                    }

                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
