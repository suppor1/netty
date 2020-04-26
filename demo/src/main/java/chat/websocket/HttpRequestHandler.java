package chat.websocket;

import com.sun.javafx.runtime.SystemProperties;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedNioFile;

import java.io.RandomAccessFile;

/**
 * @author k.jiang
 * 2020/4/18 下午11:01
 * Description websocket 协议实现网页聊天
 */
public class HttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private String wsUri;

    public HttpRequestHandler(String wsUri) {
        this.wsUri = wsUri;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
        //如果连接请求是ws,则将请求转移到下一个handler
        if (msg.uri().equalsIgnoreCase(wsUri)) {
           ctx.fireChannelRead(msg.retain());
        } else {
            if (HttpUtil.is100ContinueExpected(msg)) {
                HttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,HttpResponseStatus.CONTINUE);
                ctx.writeAndFlush(response);
            }
            //响应一个html 页面给客户端
            String baseURI = System.getProperty("user.dir");
            baseURI = "/Users/jiangkai/IdeaProjects/netty/demo/src/main/resources";
            RandomAccessFile randomAccessFile = new RandomAccessFile(baseURI + "/index.html","r");

            HttpResponse httpResponse = new DefaultFullHttpResponse(msg.protocolVersion(),HttpResponseStatus.OK);
            httpResponse.headers().set(HttpHeaderNames.CONTENT_TYPE,"text/html; charset=UTF-8");

            boolean keepAlive = HttpUtil.isKeepAlive(msg);

            if (keepAlive) {
                httpResponse.headers().set(HttpHeaderNames.CONTENT_LENGTH,randomAccessFile.length());
                httpResponse.headers().set(HttpHeaderNames.CONNECTION,HttpHeaderValues.KEEP_ALIVE);
            }
            ctx.write(httpResponse);

            //如果不是https 请求，则将 index.html 写入客户点
            if (ctx.pipeline().get(SslHandler.class) == null) {
                ctx.write(new DefaultFileRegion(randomAccessFile.getChannel(),0,randomAccessFile.length()));
            } else {
                ctx.write(new ChunkedNioFile(randomAccessFile.getChannel()));
            }
            //标识响应内容结束并刷新通道
            ChannelFuture future = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
            if (!keepAlive) {
                //http 不活跃
                future.addListener(ChannelFutureListener.CLOSE);
            }

            randomAccessFile.close();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
       cause.printStackTrace();
       ctx.close();
    }
}
