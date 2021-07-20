package com.power.nettydemo.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.util.CharsetUtil;

/**
 * SimpleChannelInboundHandler与ChannelInboundHandler：
 *
 * 客户端使用SimpleChannelInboundHandler的原因是它不需要考虑异步操作，
 * 当channelRead0方法执行完后，SimpleChannelInboundHandler就会释放指向保存该消息的ByteBuffer的内存。
 *
 * 服务端使用ChannelInboundHandler是因为它需要给客户端传送消息，而ctx.write()方法是异步的，
 * 可能channelRead()方法执行完了它还没有返回，所以为了避免这种情况便使用了ChannelInboundHandler。
 * channelReadComplete方法会在channelRead()消费完读取的数据的时候被触发，此时它会将输出冲刷到channel。
 */
@ChannelHandler.Sharable
public class NettyClientHandler extends SimpleChannelInboundHandler<ByteBuf> {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.copiedBuffer("netty 活跃", CharsetUtil.UTF_8));
    }

    // 记录已接收的消息存储
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        System.out.println("客户端接收消息：" + msg.toString(CharsetUtil.UTF_8));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}

