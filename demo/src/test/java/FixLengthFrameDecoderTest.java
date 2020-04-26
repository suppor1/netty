import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author k.jiang
 * 2020/4/18 下午6:12
 * Description 使用Embedded 完成测试
 */
public class FixLengthFrameDecoderTest {
    @Test
    public void testFramesDecoded() {
        ByteBuf buf = Unpooled.buffer();
        for (int i = 0; i < 9; i++) {
            buf.writeByte(i);
        }

        buf.retain();

        ByteBuf byteBuf = buf.duplicate();
        EmbeddedChannel channel = new EmbeddedChannel(
                new FixLengthFrameDecoder(3));
        // write bytes
        Assert.assertTrue(channel.writeInbound(byteBuf));
        Assert.assertTrue(channel.finish());
        // read message
        Assert.assertEquals(buf.readBytes(3), channel.readInbound());
        Assert.assertEquals(buf.readBytes(3), channel.readInbound());
        Assert.assertEquals(buf.readBytes(3), channel.readInbound());
        Assert.assertNull(channel.readInbound());
    }

}
