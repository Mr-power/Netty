package com.power.nettydemo.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.util.CharsetUtil;

/**
 * netty服务端handler的处理是采用责任链的形式。
 * 默认情况下，channelHandler会把对它的方法调用转发给链中的下一个channelHandler。
 * 如果 exceptionCaught()方法没有被该链中的某处实现，
 * 那么所接收的异常会被传递到channelPipeline的尾端并被记录。
 *
 */
@ChannelHandler.Sharable // 标识一个 channelHandler 可以被多个 channel 安全地调用。
public class NettyServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    // 当有入站消息时该方法就会调用
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buffer = (ByteBuf)msg;
        System.out.println("服务器收到消息:" + buffer.toString(CharsetUtil.UTF_8));
        // 将接收到的消息写给发送者，而不冲刷出站消息。
        ctx.write(buffer);
    }

    @Override
    // channelRead消费完读取的数据的时候被触发
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        // 将未决消息冲刷到远程节点，并且关闭该 channel
        ChannelFuture channelFuture = ctx.writeAndFlush(Unpooled.EMPTY_BUFFER);
        channelFuture.addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    // 在读操作时处理异常
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // 打印异常栈，并关闭该 channel
        cause.printStackTrace();
        ctx.close();
    }
}