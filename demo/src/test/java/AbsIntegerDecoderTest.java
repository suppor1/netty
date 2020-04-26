import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author k.jiang
 * 2020/4/18 下午10:34
 * Description Embedded 测试out
 */
public class AbsIntegerDecoderTest {
    @Test
    public void absIntegerOutTest() {
        ByteBuf inBuf = Unpooled.buffer();
        for (int i = 0; i < 10; i++) {
            inBuf.writeInt(-1 * i);
        }

        EmbeddedChannel channel = new EmbeddedChannel(new AbsIntegerEncoder());
        channel.writeOutbound(inBuf);
        channel.finish();

        for (int i = 0; i < 10; i++) {
            Assert.assertEquals((Integer)i, channel.readOutbound());
        }


    }
}
