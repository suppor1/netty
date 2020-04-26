import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.ByteProcessor;

import java.util.Random;

/**
 * @author k.jiang
 * 2020/4/22 下午10:29
 * Description Heap ByteBuf
 */
public class HeapByteBuf {
    private static final ByteBuf HEAP_BYTEBUF = Unpooled.buffer(1024);

    public static void main(String[] args) {

        byte[] bytes0 = "Hello".getBytes();
        ByteBuf byteBuf0 = Unpooled.wrappedBuffer(bytes0);
        byteBuf0.setByte(0,(byte)'C');
        System.out.println(new String(bytes0));
        System.out.println(new String(byteBuf0.array()));

        byte[] bytes1 = "Hello".getBytes();
        ByteBuf byteBuf1 = Unpooled.copiedBuffer(bytes1);
        byteBuf1.setByte(0,(byte)'c');
        System.out.println(new String(bytes1));
        System.out.println(new String(byteBuf1.array()));

        ByteBufAllocator.DEFAULT.compositeDirectBuffer();

        byte[] bytes = "Hello".getBytes();
       ByteBuf byteBuf = Unpooled.copiedBuffer(bytes);
       byteBuf.setByte(0,(byte)'A');

        System.out.println(new String(bytes));
        System.out.println(new String(byteBuf.array()));


//        headByteBuf();
//        copyByteBuf();
//        compositeByteBuf();
//
//        writeableByteBuf();
    }

    private static void writeableByteBuf() {
        ByteBuf byteBuf = Unpooled.buffer();
        while (byteBuf.writableBytes() >= 4) {
            byteBuf.writeInt(new Random().nextInt());

            byteBuf.forEachByte(ByteProcessor.FIND_LF);
            byteBuf.forEachByteDesc(ByteProcessor.FIND_LF);

            byteBuf.copy();
        }

        Unpooled.copiedBuffer("".getBytes());
    }

    private static void compositeByteBuf() {
        CompositeByteBuf compositeByteBuf = Unpooled.compositeBuffer();
        ByteBuf header = Unpooled.buffer();
        ByteBuf body = Unpooled.buffer();

        compositeByteBuf.addComponents(header,body);

        for (ByteBuf byteBuf : compositeByteBuf) {
            byteBuf.discardReadBytes();
            byteBuf.discardSomeReadBytes();
            byteBuf.readByte();
            byteBuf.readBytes(new byte[10]);
        }
    }

    private static void copyByteBuf() {
        ByteBuf directByteBuf = HEAP_BYTEBUF;
        if (!directByteBuf.hasArray()) {
            byte[] buff = new byte[directByteBuf.readableBytes()];
            directByteBuf.getBytes(directByteBuf.readerIndex(),buff);
        }
    }

    private static void headByteBuf() {

        //检查heapBuf 是否有数组
        System.out.println(HEAP_BYTEBUF.hasArray());

        System.out.println(HEAP_BYTEBUF.arrayOffset());
    }




}
