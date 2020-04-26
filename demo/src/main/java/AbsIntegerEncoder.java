import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

/**
 * @author k.jiang
 * 2020/4/18 下午10:31
 * Description out 测试
 */
public class AbsIntegerEncoder extends MessageToMessageEncoder<ByteBuf> {

    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        while (msg.readableBytes() >= 4) {
            int tmp = Math.abs(msg.readInt());
            out.add(tmp);
        }
    }
}
