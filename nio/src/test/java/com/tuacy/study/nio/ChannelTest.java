package com.tuacy.study.nio;

import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

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

}
