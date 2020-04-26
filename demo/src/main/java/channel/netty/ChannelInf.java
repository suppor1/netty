package channel.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.EventExecutorGroup;

/**
 * @author k.jiang
 * 2020/4/23 上午12:57
 * Description TODO
 */
public class ChannelInf {
    public static void main(String[] args) {
        Channel channel = new NioSocketChannel();

        channel.pipeline().addLast(new InboundHandler());
    }


    private static class InboundHandler implements ChannelInboundHandler {
        @Override
        public void channelRegistered(ChannelHandlerContext ctx) throws Exception {

        }

        @Override
        public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {

        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {

        }

        @Override
        public void channelInactive(ChannelHandlerContext ctx) throws Exception {

        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        }

        @Override
        public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {

        }

        @Override
        public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {

        }

        @Override
        public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {

        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

        }

        @Override
        public void handlerAdded(ChannelHandlerContext ctx) throws Exception {

        }

        @Override
        public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {

        }
    }
}
