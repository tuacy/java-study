package com.tuacy.study.nio;

import org.junit.Test;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;

/**
 * @name: BufferTest
 * @author: tuacy.
 * @date: 2019/7/24.
 * @version: 1.0
 * @Description:
 */
public class BufferTest {

    @Test
    public void byteBufferTest() {
        // 创建一个ByteBuffer实例
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        // 清空
        buffer.clear();
        // 写入数据
        byte[] putByteArray = "hello word!".getBytes(StandardCharsets.UTF_8);
        buffer.put(putByteArray);
        // 切换到读模式
        buffer.flip();
        // 把数据读取出来
        buffer.slice();
        while (buffer.hasRemaining()) {
            System.out.print((char) buffer.get());
        }
        System.out.println();
        // 重新读
        buffer.rewind();
        // ps: 这个时候buffer.limit()就是数组元素的个数
        byte[] retByte = new byte[buffer.limit()];
        buffer.get(retByte);
        System.out.println(new String(retByte, StandardCharsets.UTF_8));
    }

    @Test
    public void charBufferTest() {
        // 创建一个ByteBuffer实例
        CharBuffer buffer = CharBuffer.allocate(1024);
        // 清空
        buffer.clear();
        // 写入数据
        char[] putArray = "hello word!".toCharArray();
        buffer.put(putArray);
        // 切换到读模式
        buffer.flip();
        // 把数据读取出来
        buffer.slice();
        while (buffer.hasRemaining()) {
            System.out.print(buffer.get());
        }
        System.out.println();
        // 重新读
        buffer.rewind();
        // ps: 这个时候buffer.limit()就是数组元素的个数
        char[] retByte = new char[buffer.limit()];
        buffer.get(retByte);
        System.out.println(String.valueOf(retByte));
    }

}
