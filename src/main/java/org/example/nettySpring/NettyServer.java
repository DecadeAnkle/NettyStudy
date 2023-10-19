package org.example.nettySpring;

import com.alibaba.fastjson2.JSON;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@PropertySource({"classpath:nettyServer.properties"})
public class NettyServer {
    @Value("${netty.server.port}")
    private int port;
    @Value("${netty.server.soBacklog}")
    private int soBacklog;
    @Value("${netty.server.soKeepAlive}")
    private boolean soKeepAlive;


    public void run() throws Exception {
        log.info("Netty server is prepared to start port :{} soBacklog : {} soKeepAlive : {}",
                port, soBacklog, soKeepAlive);
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try{
            ServerBootstrap strap = new ServerBootstrap();
            strap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            //添加handler
                            ch.pipeline().addLast(new TextWebSocketFrameHandler());

                        }
                    })
                    .option(ChannelOption.SO_BACKLOG,soBacklog)
                    .childOption(ChannelOption.SO_KEEPALIVE,soKeepAlive);
            // 绑定端口，开始接收进来的连接
            ChannelFuture cf = strap.bind(port).sync();
            log.info(NettyServer.class.getName() + " started and listen on " + cf.channel().localAddress()+":"+port);
            // 等待服务器  socket 关闭 。
            cf.channel().closeFuture().sync();
        }catch (Exception e){
            log.error("NettyServer exception",e);
        } finally{
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}
