package chat.websocket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;

/**
 * @author k.jiang
 * 2020/4/18 下午11:42
 * Description websocket handler
 */
public class TextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private ChannelGroup channels;

    public TextWebSocketFrameHandler(ChannelGroup channels) {
        this.channels = channels;
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        //websocket 握手完成
        if (evt == WebSocketServerProtocolHandler.ServerHandshakeStateEvent.HANDSHAKE_COMPLETE) {
            //删除ChannelPipeline中的HttpRequestHandler
            ctx.pipeline().remove(HttpRequestHandler.class);
            //写一个消息到ChannelGroup
            channels.writeAndFlush(new TextWebSocketFrame("Client " + ctx.channel()
                    + " joined"));
            //将Channel添加到ChannelGroup
            channels.add(ctx.channel());
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        //收到的消息通过group 转发
        channels.writeAndFlush(msg.retain());
    }
}
