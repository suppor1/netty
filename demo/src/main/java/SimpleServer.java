import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author k.jiang
 * 2020/4/22 下午7:41
 * Description Bootstrap 启动分析
 */
public class SimpleServer {
    public static void main(String[] args){
        NioEventLoopGroup boss = new NioEventLoopGroup(1);
        NioEventLoopGroup worker = new NioEventLoopGroup();

        ServerBootstrap s = new ServerBootstrap();

        s.group(boss,worker)
                .channel(NioServerSocketChannel.class)
                .handler(new SimpleHandler())
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {

                    }
                });
        ChannelFuture channelFuture;
        try {
            channelFuture = s.bind(9000).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }

    private static class SimpleHandler extends ChannelInboundHandlerAdapter {
        @Override
        public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
            System.out.println("channelRegistered");
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            System.out.println("channelActive");
        }

        @Override
        public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
            System.out.println("handlerAdded");
        }
    }
}
