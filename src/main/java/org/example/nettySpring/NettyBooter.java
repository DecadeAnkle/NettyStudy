package org.example.nettySpring;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

//@Component
@Slf4j
public class NettyBooter implements ApplicationListener<ContextRefreshedEvent> {

    private final NettyServer nettyServer;

    public NettyBooter(NettyServer nettyServer) {
        this.nettyServer = nettyServer;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        try {
            nettyServer.run();
        } catch (Exception e) {
            log.info("Failed to start nettyServer",e);
        }
    }
}
