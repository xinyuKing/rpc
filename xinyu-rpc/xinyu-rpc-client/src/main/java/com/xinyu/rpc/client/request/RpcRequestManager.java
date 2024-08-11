package com.xinyu.rpc.client.request;

import com.xinyu.rpc.cache.ServiceProviderCache;
import com.xinyu.rpc.client.cluster.StrategyProvider;
import com.xinyu.rpc.data.RpcRequest;
import com.xinyu.rpc.data.RpcResponse;
import com.xinyu.rpc.enums.StatusEnum;
import com.xinyu.rpc.exception.RpcException;
import com.xinyu.rpc.netty.codec.*;
import com.xinyu.rpc.netty.handler.RpcResponseHandler;
import com.xinyu.rpc.netty.request.ChannelMapping;
import com.xinyu.rpc.netty.request.RequestPromise;
import com.xinyu.rpc.netty.request.RpcRequestHolder;
import com.xinyu.rpc.provider.ServiceProvider;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.FutureListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class RpcRequestManager {

    @Autowired
    private ServiceProviderCache providerCache;

    @Autowired
    private StrategyProvider strategyProvider;

    public RpcResponse sendRequest(RpcRequest rpcRequest) {
        List<ServiceProvider> serviceProviders = providerCache.get(rpcRequest.getClassName());
        if (serviceProviders.isEmpty()){
            log.error("{}接口没有可用的提供者", rpcRequest.getClassName());
            throw new RpcException(StatusEnum.NOT_FOUND_SERVICE_PROVINDER);
        }
        // 有提供者,需要找到一个提供者类服务当前请求----->TODO 负载均衡(已完成)
        //ServiceProvider serviceProvider = serviceProviders.get(0);
        ServiceProvider serviceProvider = strategyProvider.getStrategy().select(serviceProviders);

        // 发送请求
        return requestByNetty(rpcRequest,serviceProvider);
    }

    /**
     * 基于netty实现客户端
     * @param rpcRequest
     * @param serviceProvider
     * @return
     */
    private RpcResponse requestByNetty(RpcRequest rpcRequest, ServiceProvider serviceProvider) {
        Channel channel;
        // 判断该提供者的连接是否已经存在
        if(!RpcRequestHolder.channelExist(serviceProvider.getServerIp(),serviceProvider.getRpcPort())){
            log.info("与{}:{}建立连接",serviceProvider.getServerIp(),serviceProvider.getRpcPort());
            NioEventLoopGroup group = new NioEventLoopGroup(0, new DefaultThreadFactory("worker"));
            RpcResponseHandler rpcResponseHandler = new RpcResponseHandler();
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            // encode
                            pipeline.addLast("firstEncoder",new FrameEncoder());
                            // TODO 可以将RpcRequestEncoder,RpcResponseEncoder 合成一个泛型类
                            pipeline.addLast("secondEncoder",new RpcRequestEncoder());
                            // decode
                            pipeline.addLast("firstDecoder",new FrameDecoder());
                            // TODO 可以将RpcRequestDncoder,RpcResponseDncoder 合成一个泛型类
                            pipeline.addLast("secondDecoder",new RpcResponseDecoder());
                            pipeline.addLast("rpcResponseHandler",rpcResponseHandler);
                        }
                    });
            // 建立连接
            try {
                ChannelFuture future = bootstrap.connect(serviceProvider.getServerIp(), serviceProvider.getRpcPort()).sync();
                if (future.isSuccess()) {
                    RpcRequestHolder.addChannelMapping(new ChannelMapping(
                            serviceProvider.getServerIp(),
                            serviceProvider.getRpcPort(),
                            future.channel()
                    ));
                }
            } catch (InterruptedException e) {
                throw new RpcException("建立连接失败,msg=" + e.getMessage());
            }
        }

        try {
            channel = RpcRequestHolder.getChannel(serviceProvider.getServerIp(), serviceProvider.getRpcPort());
            // 构建请求promise
            RequestPromise requestPromise = new RequestPromise(channel.eventLoop());
            RpcRequestHolder.addRequestPromise(rpcRequest.getRequestId(), requestPromise);

            // 发送数据
            channel.writeAndFlush(rpcRequest);

            // 添加监听，等响应成功删除requestPromiseMap中相应的requestPromise
            requestPromise.addListener(new FutureListener<RpcResponse>() {
                @Override
                public void operationComplete(Future<RpcResponse> future) throws Exception {
                    RpcResponse response = future.get();
                    RpcRequestHolder.removeRequestPromise(response.getRequestId());
                }
            });
            // 等待响应结果
            RpcResponse rpcResponse = (RpcResponse) requestPromise.get(10, TimeUnit.SECONDS);
            if (rpcResponse == null) {
                return new RpcResponse();
            }
            return rpcResponse;
        } catch (Exception e) {
            log.error("error msg={}",e.getMessage());
            return RpcResponse.builder()
                    .requestId(rpcRequest.getRequestId())
                    .cause(e)
                    .build();
        }
    }
}
