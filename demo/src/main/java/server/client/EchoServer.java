package server.client;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import java.net.InetSocketAddress;

/**
 * @author k.jiang
 * 2020/4/16 下午10:38
 * Description 服务端
 */
public class EchoServer {
    private int port;

    public EchoServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws Exception {
        EchoServer echoServer = new EchoServer(9000);
        echoServer.startServer();
    }

    private void startServer() throws Exception {
        NioEventLoopGroup boss = new NioEventLoopGroup(1);
        NioEventLoopGroup worker = new NioEventLoopGroup();
        ServerBootstrap  b = new ServerBootstrap();
        b.group(boss,worker)
        .channel(NioServerSocketChannel.class)
                .attr(AttributeKey.newInstance("he"),Integer.valueOf(10))
        .localAddress(new InetSocketAddress(port))
        .childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(new EchoServerHandler());
            }
        });
        try {
            ChannelFuture future = b.bind().sync();
            System.out.println(this.getClass().getName() + " started listen on " + future.channel().localAddress());
            future.channel().closeFuture().sync();
        } finally {
            boss.shutdownGracefully().sync();
            worker.shutdownGracefully().sync();
        }
    }

    //这个注解很重要：表示 这里对于所有的客户端连接来说，都会使用同一个EchoServerHandler
    @ChannelHandler.Sharable
    private class EchoServerHandler extends ChannelInboundHandlerAdapter {
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            System.out.println("server received message:" + msg);
            ctx.write(msg);
        }

        @Override
        public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
            ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            cause.printStackTrace();
            ctx.close();
        }
    }
}
