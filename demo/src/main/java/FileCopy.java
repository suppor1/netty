import io.netty.channel.DefaultFileRegion;
import io.netty.channel.FileRegion;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

/**
 * @author k.jiang
 * 2020/4/17 下午2:41
 * Description 实现文件拷贝
 */
public class FileCopy {

    class CopyFileByStream {
        public void copyFile(String sourceFile, String destFile) throws Exception {
            FileInputStream fileInputStream = new FileInputStream(sourceFile);
            FileOutputStream fileOutputStream = new FileOutputStream(destFile);
            byte[] channel = new byte[1024];

            while (fileInputStream.read(channel) != -1) {
                fileOutputStream.write(channel);
            }
        }
    }

    class ZeroCopy {
        public void copyFile(String sourceFile, String destFile) throws Exception {
            RandomAccessFile sourceAccessFile = new RandomAccessFile(sourceFile,"r");
            FileChannel sourceChannel = sourceAccessFile.getChannel();

            RandomAccessFile destAccessFile = new RandomAccessFile(destFile,"r");
            FileChannel destChannel = destAccessFile.getChannel();

            long position = 0;
            long readable_length = sourceChannel.size();
            sourceChannel.transferTo(position,readable_length,destChannel);
        }
    }

    class NettyCopy {
        public void copyFile(String sourceFile, String destFile) throws Exception {
            RandomAccessFile sourceAccessFile = new RandomAccessFile(sourceFile,"r");
            FileChannel sourceChannel = sourceAccessFile.getChannel();

            RandomAccessFile destAccessFile = new RandomAccessFile(destFile,"r");
            FileChannel destChannel = destAccessFile.getChannel();

            long position = 0;
            long readable_length = sourceChannel.size();

            //file region 使用
            FileRegion fileRegion = new DefaultFileRegion(sourceChannel,position,readable_length);
            fileRegion.transferTo(destChannel,position);
        }
    }
}
