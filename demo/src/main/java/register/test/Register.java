package register.test;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author k.jiang
 * 2020/4/26 下午6:07
 * Description 可以端重新注册
 */
public class Register {
    private AtomicInteger id = new AtomicInteger();
    private Map<Integer, EventLoop> users = new ConcurrentHashMap<>();

    public static void main(String[] args) throws Exception{
        NioEventLoopGroup boss = new NioEventLoopGroup(1);
        NioEventLoopGroup words = new NioEventLoopGroup(100);
        Register register = new Register();
        register.boundServerAndRun(boss,words, NioServerSocketChannel.class);
        new Client().connect();
        Thread.sleep(1000);
        new Client().connect().send();
    }

    private void boundServerAndRun(EventLoopGroup boss, EventLoopGroup words, Class<? extends ServerChannel> serverChannel) {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(boss,words)
                .channel(serverChannel)
                .childOption(ChannelOption.SO_KEEPALIVE,true)
                .childHandler(getChannelInitialization());

        try {
            serverBootstrap.bind(8080).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private ChannelHandler getChannelInitialization() {
        return new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addLast(new SimpleChannelInboundHandler<ByteBuf>() {
                    @Override
                    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
                        System.out.println("Receive message from: " + ctx.channel());
                    }

                    @Override
                    public void channelActive(ChannelHandlerContext ctx) throws Exception {
                        System.out.println("Server channel Active Channel : " + ctx.channel());
                        EventLoop eventLoop = users.get(id.get());
                        if (eventLoop == null) {
                            System.out.println("create new user " + ctx.channel().eventLoop());
                            users.put(id.incrementAndGet(),ctx.channel().eventLoop());
                        } else {
                            System.out.println("Register channel to " + eventLoop + "from" + ctx.channel().eventLoop());
                            registerChannel(ctx, eventLoop, handler -> completeRegister());
                        }
                    }
                });
            }
        };
    }

    private void registerChannel(ChannelHandlerContext ctx, EventLoop eventLoop, final ChannelFutureListener completeHandler) {
        ChannelFuture future = ctx.deregister();
        future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                eventLoop.register(future.channel()).addListener(completeHandler);
            }
        });
    }

    private void completeRegister() {
        System.out.println("Server register complete");
    }


    private static class Client {

        private Channel channel;

        public Client connect() throws Exception{
            EventLoopGroup eventLoopGroup = new NioEventLoopGroup();

            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventLoopGroup)
                    .option(ChannelOption.SO_KEEPALIVE,true)
                    .channel(NioSocketChannel.class)
                    .handler(new SimpleChannelInboundHandler<SocketChannel>() {
                        @Override
                        protected void channelRead0(ChannelHandlerContext ctx, SocketChannel msg) throws Exception {

                        }
                    });
            this.channel = bootstrap.connect(new InetSocketAddress("localhost",8080)).sync().channel();
            return this;
        }

        public Client send() {
            channel.writeAndFlush(Unpooled.copiedBuffer("Client".getBytes()));
            return this;
        }
    }
}
