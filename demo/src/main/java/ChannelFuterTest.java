import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

/**
 * @author k.jiang
 * 2020/4/19 下午10:02
 * Description channelFuterListener
 */
public class ChannelFuterTest {

    public static void main(String[] args) {
        NioSocketChannel channel = new NioSocketChannel();

        ChannelFuture channelFuture = channel.connect(new InetSocketAddress("",9000));

        channelFuture.addListener(new GenericFutureListener<Future<? super Void>>() {
            @Override
            public void operationComplete(Future<? super Void> future) throws Exception {
                if (future.isSuccess()) {
                    ByteBuf byteBuf = Unpooled.copiedBuffer("Hello", Charset.defaultCharset());
                    channel.writeAndFlush(byteBuf);
                } else {
                    Throwable th = future.cause();
                    th.printStackTrace();
                }
            }
        });
    }

}
