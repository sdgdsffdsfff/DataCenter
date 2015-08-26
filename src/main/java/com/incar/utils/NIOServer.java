package com.incar.utils;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * NIO服务端
 * @author 柳明
 */
public class NIOServer {
    //通道管理器
    private Selector selector;
    Logger log=Logger.getLogger(NIOServer.class);
    /**
     * 获得一个ServerSocket通道，并对该通道做一些初始化的工作
     * @param port  绑定的端口号
     * @throws IOException
     */
    public void initServer(int port) throws IOException {
        // 获得一个ServerSocket通道
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        log.info("打开Socket通道");
        // 设置通道为非阻塞
        serverChannel.configureBlocking(false);
        log.info("Socket通道设置为非阻塞模式");
        // 将该通道对应的ServerSocket绑定到port端口
        serverChannel.socket().bind(new InetSocketAddress(port));
        log.info("Socket通道绑定到"+port+"端口");
        // 获得一个通道管理器
        this.selector = Selector.open();
        log.info("打开通道管理器");
        //将通道管理器和该通道绑定，并为该通道注册SelectionKey.OP_ACCEPT事件,注册该事件后，
        //当该事件到达时，selector.select()会返回，如果该事件没到达selector.select()会一直阻塞。
        serverChannel.register(selector,SelectionKey.OP_ACCEPT);
        log.info("在Socket通道中绑定通道管理器，注册了SelectionKey.OP_ACCEPT事件");

    }

    /**
     * 采用轮询的方式监听selector上是否有需要处理的事件，如果有，则进行处理
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public void listen() throws IOException {
        log.info("服务端启动成功，开始监听");
        // 轮询访问selector
        do{
            //当注册的事件到达时，方法返回；否则,该方法会一直阻塞
            log.info("等待连接...");
            selector.select();
            log.info("连接成功...");
            // 获得selector中选中的项的迭代器，选中的项为注册的事件
            Iterator<SelectionKey> ite = this.selector.selectedKeys().iterator();
            System.out.println("很多很多"+this.selector.selectedKeys().size());
            while (ite.hasNext()) {
                SelectionKey key = ite.next();
                // 删除已选的key,以防重复处理
                ite.remove();
                // 客户端请求连接事件
                if (key.isAcceptable()) {
                    ServerSocketChannel server = (ServerSocketChannel) key.channel();

                    // 获得和客户端连接的通道
                    SocketChannel channel = server.accept();
                    System.out.println(channel.socket().getInetAddress().getHostAddress());
                    // 设置成非阻塞
                    channel.configureBlocking(false);
                    //在这里可以给客户端发送信息
                    //channel.write(ByteBuffer.wrap("向客户端发送了一条信息".getBytes("GBK")));
                    //在和客户端连接成功之后，为了可以接收到客户端的信息，需要给通道设置读的权限。
                    channel.register(this.selector, SelectionKey.OP_READ);
                    // 获得了可读的事件
                } else if (key.isReadable()) {
                    read(key);
                }
            }
        }
        while(true);
    }
    /**
     * 处理读取客户端发来的信息 的事件
     * @param key   参数
     * @throws IOException
     */
    public void read(SelectionKey key) throws IOException{
        // 服务器可读取消息:得到事件发生的Socket通道
        SocketChannel channel = (SocketChannel) key.channel();
        // 创建读取的缓冲区
        ByteBuffer buffer = ByteBuffer.allocate(50);

        int count=channel.read(buffer);
        if(count>0) {
            byte[] data = buffer.array();
            for(byte i:data){
                System.out.printf("%x", i);
                System.out.print(" ");
            }
            System.out.println();
            String msg = new String(data,"GBK").trim();
            System.out.println("服务端收到"+channel.socket().getInetAddress().getHostAddress()+"信息：" + msg);
            ByteBuffer outBuffer = ByteBuffer.wrap(msg.getBytes("GBK"));
            byte[] abc=outBuffer.array();
            for(byte i:abc){
                System.out.printf("%x",i);
                System.out.print(" ");
            }
            channel.write(outBuffer);// 将消息回送给客户端
        }
        else{
            log.info("客户端关闭连接...");
            channel.close();
        }

    }

    /**
     * 启动服务端测试
     * @throws IOException
     */
    /*public static void main(String[] args) throws IOException {
        NIOServer server = new NIOServer();
        server.initServer(8000);
        server.listen();

        *//*String a="你好";
        byte[] b=a.getBytes("GBK");
        for(byte i:b){
            System.out.printf("%x",i);
            System.out.print(" ");
        }

        String c=new String(b,"GBK");
        System.out.println(c);*//*
    }*/

}
