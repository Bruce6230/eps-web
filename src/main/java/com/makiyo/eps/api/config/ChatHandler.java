package com.makiyo.eps.api.config;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;

public class ChatHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    // 用于记录和管理所有客户端的ChannelGroup
    private static ChannelGroup clients = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        // 获取客户端传输过来的消息
        String content = msg.text();
        System.out.println("接收到的数据：" + content);

        for(Channel channel : clients) {
            // 不能直接writeAndFlush收到的content字符串信息，必须封装到frame载体中输出
            channel.writeAndFlush(new TextWebSocketFrame("系统消息：" + content));
        }
        // clients.writeAndFlush(new TextWebSocketFrame("系统消息：" + content));

    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        // 当客户端连接服务端之后，获取客户端的channel，并且放到ChannelGroup中去进行管理
        clients.add(ctx.channel());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        // 这步是多余的，当断开连接时候ChannelGroup会自动移除对应的channel
        clients.remove(ctx.channel());
        System.out.println(ctx.channel().id().asLongText());
    }
}
