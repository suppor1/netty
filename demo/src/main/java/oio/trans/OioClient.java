package oio.trans;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @author k.jiang
 * 2020/4/21 下午10:20
 * Description Bio socket client
 */
public class OioClient {
    public static void main(String[] args) {
        Socket socket = new Socket();
        try {
            socket.connect(new InetSocketAddress("localhost",9000));
            InputStreamReader in = new InputStreamReader(socket.getInputStream());
            while (in.ready()) {
                char[] chars = new char[in.read()];
                in.read(chars);
                System.out.println(new String(chars));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
