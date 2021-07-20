package com.power.nettydemo.clientServer;

import com.power.nettydemo.handler.NettyServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NettyServer {
    public static void main(String[] args) {
        NettyServer nettyServer = new NettyServer();
        nettyServer.start(8889);
    }

    public void start(int port){
        // 处理TCP连接请求
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        // 处理I/O事件
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            // 用于引导和绑定服务器
            ServerBootstrap bootstrap = new ServerBootstrap();
            //将上面的线程组加入到 bootstrap 中
            bootstrap.group(bossGroup,workGroup)
                    //将通道设置为异步的通道
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            // 因为 NettyServerHandler 被标注为 @Sharable，所以可以使用相同的实例
                            socketChannel.pipeline().addLast(new NettyServerHandler());
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG,200)
                    .childOption(ChannelOption.SO_KEEPALIVE,true);
            // 异步绑定服务器，调用 sync() 方法阻塞等待直到绑定完成。
            ChannelFuture future = bootstrap.bind(port).sync();
            // 获取 channel 的 closeFuture，并且阻塞直到它完成。
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }
}
