package server.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;

/**
 * @author k.jiang
 * 2020/4/16 下午11:12
 * Description echo client
 */
public class EchoClient {
    private String address;
    private int port;

    public static void main(String[] args) throws Exception {
        EchoClient echoClient = new EchoClient("localhost",9000);
        echoClient.start();
    }

    private void start() throws Exception{
        NioEventLoopGroup nioEventLoopGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(nioEventLoopGroup)
                    .channel(NioSocketChannel.class)
                    .remoteAddress(new InetSocketAddress(address,port))
                    .handler(new ChannelInitializer<SocketChannel>(){
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new EchoClientHandler());
                        }
                    });
            ChannelFuture channelFuture = bootstrap.connect().sync();
            channelFuture.channel().closeFuture().sync();
        } finally {
            nioEventLoopGroup.shutdownGracefully().sync();
        }
    }

    public EchoClient(String address, int port) {
        this.address = address;
        this.port = port;
    }

    private class EchoClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            ctx.writeAndFlush(Unpooled.copiedBuffer("Netty rocks!", CharsetUtil.UTF_8));
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
           cause.printStackTrace();
           ctx.close();
        }

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
            System.out.println("Client received :" + ByteBufUtil.hexDump(msg.readBytes(msg.readableBytes())));
            System.out.println("Client received: " + msg.toString(CharsetUtil.UTF_8));
        }
    }
}
