package com.power.nettydemo.clientServer;

import com.power.nettydemo.handler.NettyClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NettyClient {
    public static void main(String[] args) {
        NettyClient nettyClient = new NettyClient();
        nettyClient.connect("localhost", 8889);
    }

    public void connect(String hostname,int port) {
        // 处理TCP连接请求
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            // 用于引导和绑定服务器
            Bootstrap bootstrap = new Bootstrap();
            //将上面的线程组加入到 bootstrap 中
            bootstrap.group(group)
                    //将通道设置为异步的通道
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline()
                                    .addLast(new NettyClientHandler());
                        }
                    })
                    .option(ChannelOption.TCP_NODELAY,true);

            // 连接到远程节点，阻塞等待直到连接完成。
            ChannelFuture future = bootstrap.connect(hostname, port).sync();
            // 阻塞，直到 channel 关闭。
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // 关闭线程池并释放所有资源。
            group.shutdownGracefully();
        }
    }
}
