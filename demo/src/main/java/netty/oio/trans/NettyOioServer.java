package netty.oio.trans;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.oio.OioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.oio.OioServerSocketChannel;

import java.net.InetSocketAddress;

/**
 * @author k.jiang
 * 2020/4/21 下午11:12
 * Description netty oio server
 */
public class NettyOioServer {
    public static void main(String[] args) {
        OioEventLoopGroup eventLoopGroup = new OioEventLoopGroup();

        ByteBuf byteBuf = Unpooled.unreleasableBuffer(Unpooled.wrappedBuffer("Hi".getBytes()));
        ServerBootstrap s = new ServerBootstrap();
        s.group(eventLoopGroup)
                .localAddress(new InetSocketAddress(9000))
                .channel(OioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new ChannelInboundHandlerAdapter(){
                            @Override
                            public void channelActive(ChannelHandlerContext ctx) throws Exception {
                               ctx.channel().writeAndFlush(byteBuf).addListener(ChannelFutureListener.CLOSE);
                            }
                        });
                    }
                });
        try {
            ChannelFuture channelFuture = s.bind().sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
