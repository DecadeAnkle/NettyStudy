package org.example.timeServer;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * MessageToByteEncoder<UnixTime> 也是 ChannelOutboundHandlerAdapter
 */
public class TimeEncoder extends MessageToByteEncoder<UnixTime> {

    @Override
    public void encode(ChannelHandlerContext ctx, UnixTime time, ByteBuf out){
        out.writeInt((int)time.value());
    }
}
