package com.tuacy.study.nio;

import org.junit.Test;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * @name: ChannelTest
 * @author: tuacy.
 * @date: 2019/7/23.
 * @version: 1.0
 * @Description:
 */
public class ChannelTest {

    @Test
    public void fileChannelRead() {

        try {
            // 开启FileChannel
            RandomAccessFile aFile = new RandomAccessFile("D:\\job\\git\\java-study\\nio\\src\\main\\resources\\fileChanel.txt", "rw");
            FileChannel inChannel = aFile.getChannel();
            ByteBuffer buf = ByteBuffer.allocate(48);
            // 从FileChannel通道读取数据到缓冲区ByteBuffer
            int bytesRead = inChannel.read(buf);
            while (bytesRead != -1) {
                System.out.println("读取到的数据长度 " + bytesRead);
                buf.flip();
                while (buf.hasRemaining()) {
                    System.out.print((char) buf.get());
                }
                buf.clear();
                // 继续读取文件信息
                bytesRead = inChannel.read(buf);
            }
            aFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void fileChannelWrite() {

        try {
            // 开启FileChannel
            RandomAccessFile aFile = new RandomAccessFile("D:\\job\\git\\java-study\\nio\\src\\main\\resources\\fileChanelWrite.txt", "rw");
            FileChannel inChannel = aFile.getChannel();
            ByteBuffer buf = ByteBuffer.allocate(48);
            byte[] forWrite = "需要写入的字符串。".getBytes(StandardCharsets.UTF_8);
            buf.put(forWrite, 0, forWrite.length);
            buf.flip();
            // 写入数据
            while (buf.hasRemaining()) {
                inChannel.write(buf);
            }
            aFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void datagramChannelServiceTest() {
        try {
            // 获取通道
            DatagramChannel datagramChannel = DatagramChannel.open();
            // 绑定端口,作为UDP服务端
            datagramChannel.bind(new InetSocketAddress(8989));
            // 分配Buffer,用于收发数据
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            while (true) {
                // 清空Buffer
                buffer.clear();
                // 接受客户端发送数据
                SocketAddress socketAddress = datagramChannel.receive(buffer);
                if (socketAddress != null) {
                    buffer.flip();
                    byte[] b = new byte[buffer.limit()];
                    int bufferReceiveIndex = 0;
                    while (buffer.hasRemaining()) {
                        b[bufferReceiveIndex++] = buffer.get();
                    }
                    System.out.println("receive remote " + socketAddress.toString() + ":" + new String(b, StandardCharsets.UTF_8));
                    //接收到消息后给发送方回应
                    sendDataBack(socketAddress, datagramChannel);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) throws IOException {
        try {
            final DatagramChannel channel = DatagramChannel.open();
            //接收消息线程
            new Thread(new Runnable() {
                @Override
                public void run() {
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    byte b[];
                    while(true) {
                        buffer.clear();
                        SocketAddress socketAddress = null;
                        try {
                            socketAddress = channel.receive(buffer);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (socketAddress != null) {
                            int position = buffer.position();
                            b = new byte[position];
                            buffer.flip();
                            for(int i=0; i<position; ++i) {
                                b[i] = buffer.get();
                            }
                            System.out.println("receive remote " +  socketAddress.toString() + ":"  + new String(b, StandardCharsets.UTF_8));
                        }
                    }
                }
            }).start();;

            //发送控制台输入消息
            while (true) {
                Scanner sc = new Scanner(System.in);
                String next = sc.next();
                try {
                    sendMessage(channel, next);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendDataBack(SocketAddress socketAddress, DatagramChannel datagramChannel) throws IOException {
        String message = "你好";
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.put(message.getBytes(StandardCharsets.UTF_8));
        buffer.flip();
        datagramChannel.send(buffer, socketAddress);
    }

    public static void sendMessage(DatagramChannel channel, String mes) throws IOException {
        if (mes == null || mes.isEmpty()) {
            return;
        }
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.clear();
        buffer.put(mes.getBytes("UTF-8"));
        buffer.flip();
        System.out.println("send msg:" + mes);
        int send = channel.send(buffer, new InetSocketAddress("localhost",8989));
    }

}
