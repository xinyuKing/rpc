package com.xinyu.rpc.server.boot.netty;

import com.xinyu.rpc.netty.codec.FrameDecoder;
import com.xinyu.rpc.netty.codec.FrameEncoder;
import com.xinyu.rpc.netty.codec.RpcRequestDecoder;
import com.xinyu.rpc.netty.codec.RpcResponseEncoder;
import com.xinyu.rpc.netty.handler.RpcRequestHandler;
import com.xinyu.rpc.server.boot.RpcServer;
import com.xinyu.rpc.server.config.RpcServerConfiguration;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.NettyRuntime;
import io.netty.util.concurrent.DefaultThreadFactory;
import io.netty.util.concurrent.EventExecutorGroup;
import io.netty.util.concurrent.UnorderedThreadPoolEventExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;


@Component
@Slf4j
public class NettyServer implements RpcServer {

    @Autowired
    private RpcServerConfiguration serverConfiguration;

    @Override
    public void start() {
        NioEventLoopGroup boss = new NioEventLoopGroup(1, new DefaultThreadFactory("boss"));
        NioEventLoopGroup worker = new NioEventLoopGroup(0, new DefaultThreadFactory("worker"));
        EventExecutorGroup business = new UnorderedThreadPoolEventExecutor(NettyRuntime.availableProcessors() * 2,
                new DefaultThreadFactory("business"));
        RpcRequestHandler rpcRequestHandler = new RpcRequestHandler();
        try {
            ServerBootstrap serverBootstrap=new ServerBootstrap();
            serverBootstrap.group(boss,worker)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG,2048)
                    .childOption(ChannelOption.TCP_NODELAY,true)
                    .childOption(ChannelOption.SO_KEEPALIVE,true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel channel) throws Exception {
                            ChannelPipeline pipeline = channel.pipeline();
                            //编码
                            pipeline.addLast("firstEncode",new FrameEncoder());
                            pipeline.addLast("secondEncode",new RpcResponseEncoder());
                            // 解码
                            pipeline.addLast("firstDecode",new FrameDecoder());
                            pipeline.addLast("secondDecode",new RpcRequestDecoder());
                            pipeline.addLast(business,"requestHandler",rpcRequestHandler);
                        }
                    });
            ChannelFuture future = serverBootstrap.bind(new InetSocketAddress(serverConfiguration.getRpcPort())).sync();

            // release
            future.channel().closeFuture().addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    // release
                    boss.shutdownGracefully();
                    worker.shutdownGracefully();
                    business.shutdownGracefully();

                }
            });
            log.info("rpc服务端启动成功");
        } catch (InterruptedException e) {
            e.printStackTrace();
            boss.shutdownGracefully();
            worker.shutdownGracefully();
            business.shutdownGracefully();
        }
    }
}
