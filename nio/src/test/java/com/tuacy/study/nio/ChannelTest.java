package com.tuacy.study.nio;

import com.google.common.util.concurrent.Uninterruptibles;
import org.junit.Test;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.DatagramChannel;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.concurrent.TimeUnit;

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

    /**
     * UDP 服务端
     */
    @Test
    public void datagramChannelService() {
        try {
            // 获取通道
            DatagramChannel datagramChannel = DatagramChannel.open();
            // 绑定端口8989,作为UDP服务端
            datagramChannel.bind(new InetSocketAddress(8989));
            // 分配Buffer,用于收发数据
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            while (true) {
                buffer.clear();
                // 等待接受客户端发送数据
                SocketAddress socketAddress = datagramChannel.receive(buffer);
                if (socketAddress != null) {
                    buffer.flip();
                    byte[] b = new byte[buffer.limit()];
                    int bufferReceiveIndex = 0;
                    while (buffer.hasRemaining()) {
                        b[bufferReceiveIndex++] = buffer.get();
                    }
                    System.out.println("收到客户端消息 " + socketAddress.toString() + ":" + new String(b, StandardCharsets.UTF_8));
                    // 接收到消息后给发送方回应
                    sendDataBack(socketAddress, datagramChannel);
                }
            }

        } catch (IOException e) {
            // ignore
        }
    }

    /**
     * 给socketAddress地址发送消息
     */
    private void sendDataBack(SocketAddress socketAddress, DatagramChannel datagramChannel) throws IOException {
        String message = "send back";
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.put(message.getBytes(StandardCharsets.UTF_8));
        buffer.flip();
        datagramChannel.send(buffer, socketAddress);
    }

    /**
     * UDP connect() 在特定的地址上收发消息
     */
    @Test
    public void datagramChannelConnect() {
        try {
            // 获取通道
            DatagramChannel datagramChannel = DatagramChannel.open();
            // 连接到特定的地址，time-a.nist.gov 获取时间。只在这个地址间收发消息 write,read 方法
            datagramChannel.connect(new InetSocketAddress("time-a.nist.gov", 37));
            ByteBuffer buffer = ByteBuffer.allocate(8);
            buffer.order(ByteOrder.BIG_ENDIAN);
            buffer.put((byte) 0);
            buffer.flip();
            // 发送数据到 time-a.nist.gov
            datagramChannel.write(buffer);
            buffer.clear();
            // 前四个字节补0
            buffer.putInt(0);
            // 从 time-a.nist.gov 读取数据
            datagramChannel.read(buffer);
            buffer.flip();
            // convert seconds since 1900 to a java.util.Date
            long secondsSince1900 = buffer.getLong();
            long differenceBetweenEpochs = 2208988800L;
            long secondsSince1970 = secondsSince1900 - differenceBetweenEpochs;
            long msSince1970 = secondsSince1970 * 1000;
            Date time = new Date(msSince1970);
            // 打印时间
            System.out.println(time);
        } catch (Exception e) {
            // ignore
        }
    }

    // UDP客户端
    @Test
    public void datagramChannelClient() {
        try {
            final DatagramChannel channel = DatagramChannel.open();
            // 开一个线程一直接收UDP服务端发送过来的消息
            new Thread(() -> {
                try {
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    while (true) {
                        buffer.clear();
                        SocketAddress socketAddress = channel.receive(buffer);
                        if (socketAddress != null) {
                            buffer.flip();
                            byte[] b = new byte[buffer.limit()];
                            int bufferReceiveIndex = 0;
                            while (buffer.hasRemaining()) {
                                b[bufferReceiveIndex++] = buffer.get();
                            }
                            System.out.println("收到消息 " + socketAddress.toString() + ":" + new String(b, StandardCharsets.UTF_8));
                        }
                    }
                } catch (Exception e) {
                    // ignore
                }

            }).start();

            int messageIndex = 0;
            // 控制台输入数据，然后发送给指定的地址
            while (true) {
                // 5S发送一次数据
                Uninterruptibles.sleepUninterruptibly(5, TimeUnit.SECONDS);
                sendMessage(channel, new InetSocketAddress("192.168.5.14", 8989), String.valueOf(messageIndex++));
            }

        } catch (IOException e) {
            // ignore
        }
    }


    private void sendMessage(DatagramChannel channel, InetSocketAddress address, String mes) throws IOException {
        if (mes == null || mes.isEmpty()) {
            return;
        }
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.clear();
        buffer.put(mes.getBytes(StandardCharsets.UTF_8));
        buffer.flip();
        channel.send(buffer, address);
    }


    /**
     * TCP客户端,阻塞模式
     */
    @Test
    public void socketChannelClient() {
        try {
            SocketChannel channel = SocketChannel.open();
            // 这里使用的是阻塞模式
            channel.connect(new InetSocketAddress("192.168.5.14", 6800));
            // KEEP ALIVE setOption()函数的使用,一定要在连接成功之后设置
            channel.setOption(StandardSocketOptions.SO_KEEPALIVE, true);
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            while (true) {
                buffer.clear();
                int readLength = channel.read(buffer);
                if (readLength >= 0) {
                    buffer.flip();
                    byte[] b = new byte[buffer.limit()];
                    int bufferReceiveIndex = 0;
                    while (buffer.hasRemaining()) {
                        b[bufferReceiveIndex++] = buffer.get();
                    }
                    System.out.println("收到消息 " + ":" + new String(b, StandardCharsets.UTF_8));
                    // 把收到的消息又发送回去
                    buffer.clear();
                    buffer.put(b);
                    buffer.flip();
                    channel.write(buffer);
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * TCP服务端 -- 阻塞模式
     */
    @Test
    public void socketChannelServer() {
        try {
            ServerSocketChannel channel = ServerSocketChannel.open();
            channel.bind(new InetSocketAddress("192.168.5.14", 6800));
            while (true) {
                // 接收客户端的连接,之后拿到的就是SocketChannel了，之后都是基于SocketChannel做相应的操作
                SocketChannel clientSocketChannel = channel.accept();
                clientSocketChannel.setOption(StandardSocketOptions.SO_KEEPALIVE, true);
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                buffer.clear();
                buffer.put("hello".getBytes(StandardCharsets.UTF_8));
                buffer.flip();
                // 给客户端发送消息
                clientSocketChannel.write(buffer);
                // 在收下客户端的消息
                buffer.clear();
                int readLength = clientSocketChannel.read(buffer);
                if (readLength >= 0) {
                    buffer.flip();
                    byte[] b = new byte[buffer.limit()];
                    int bufferReceiveIndex = 0;
                    while (buffer.hasRemaining()) {
                        b[bufferReceiveIndex++] = buffer.get();
                    }
                    System.out.println("收到消息 " + ":" + new String(b, StandardCharsets.UTF_8));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
