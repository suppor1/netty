package oio.trans;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author k.jiang
 * 2020/4/21 下午10:14
 * Description Bio 网络编程
 */
public class OioServer {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(9000);
            for (;;) {
                Socket socket = serverSocket.accept();
                System.out.println("Accept New connect: " + socket);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        OutputStream outputStream;
                        try {
                            outputStream = socket.getOutputStream();
                            outputStream.write("I am server".getBytes());
                            outputStream.flush();
                            socket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                socket.close();
                            } catch (IOException e) {
                                //do nothing
                            }
                        }
                    }
                }).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
