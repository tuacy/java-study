package com.tuacy.study.nio;

import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @name: SelectorTest
 * @author: tuacy.
 * @date: 2019/7/27.
 * @version: 1.0
 * @Description:
 */
public class SelectorTest {

    @Test
    public void tcpClient() {
        try {
            SocketChannel socketChannel = SocketChannel.open();
            // 连接
            socketChannel.connect(new InetSocketAddress("192.168.5.14", 6800));
            ByteBuffer writeBuffer = ByteBuffer.allocate(32);
            ByteBuffer readBuffer = ByteBuffer.allocate(32);
            writeBuffer.put("hello".getBytes());
            writeBuffer.flip();
            while (true) {
                writeBuffer.rewind();
                socketChannel.write(writeBuffer);
                readBuffer.clear();
                socketChannel.read(readBuffer);
                readBuffer.flip();
                byte[] b = new byte[readBuffer.limit()];
                int bufferReceiveIndex = 0;
                while (readBuffer.hasRemaining()) {
                    b[bufferReceiveIndex++] = readBuffer.get();
                }
                System.out.println("received : " + new String(b));
            }
        } catch (Exception e) {
            // ignore
        }
    }


    @Test
    public void tcpServer() {

        try {
            // 创建一个ServerSocketChannel通道
            ServerSocketChannel serverChannel = ServerSocketChannel.open();
            // 绑定6800端口
            serverChannel.bind(new InetSocketAddress("192.168.5.14", 6800));
            // 设置非阻塞
            serverChannel.configureBlocking(false);
            // Selector创建
            Selector selector = Selector.open();
            // 注册 channel，并且指定感兴趣的事件是 Accept
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);
            ByteBuffer readBuff = ByteBuffer.allocate(1024);
            ByteBuffer writeBuff = ByteBuffer.allocate(1024);
            writeBuff.put("received".getBytes());
            writeBuff.flip();
            while (true) {
                if (selector.select() > 0) {
                    Set<SelectionKey> readyKeys = selector.selectedKeys();
                    Iterator<SelectionKey> readyKeyIterator = readyKeys.iterator();
                    while (readyKeyIterator.hasNext()) {
                        SelectionKey key = readyKeyIterator.next();
                        readyKeyIterator.remove();

                        if (key.isAcceptable()) {
                            // 连接
                            ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
                            SocketChannel socketChannel = serverSocketChannel.accept();
                            socketChannel.configureBlocking(false);
                            // 我们又给注册到Selector里面去了，声明这个channel只对读操作感兴趣。
                            socketChannel.register(selector, SelectionKey.OP_READ);
                        } else if (key.isReadable()) {
                            // 读
                            SocketChannel socketChannel = (SocketChannel) key.channel();
                            readBuff.clear();
                            socketChannel.read(readBuff);
                            readBuff.flip();
                            byte[] b = new byte[readBuff.limit()];
                            int bufferReceiveIndex = 0;
                            while (readBuff.hasRemaining()) {
                                b[bufferReceiveIndex++] = readBuff.get();
                            }
                            System.out.println("received : " + new String(b));
                            // 修改selector对channel感兴趣的事件
                            key.interestOps(SelectionKey.OP_WRITE);
                        } else if (key.isWritable()) {
                            // 写
                            writeBuff.rewind();
                            SocketChannel socketChannel = (SocketChannel) key.channel();
                            socketChannel.write(writeBuff);
                            // 修改selector对channel感兴趣的事件
                            key.interestOps(SelectionKey.OP_READ);
                        }
                    }
                }
            }

        } catch (IOException e) {
            // ignore
        }

    }

}
