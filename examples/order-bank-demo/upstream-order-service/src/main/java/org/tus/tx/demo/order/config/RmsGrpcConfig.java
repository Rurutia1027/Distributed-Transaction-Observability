package org.tus.tx.demo.order.config;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.tus.tx.rms.twophase.v1.ReliableMessageServiceGrpc;

import java.util.concurrent.TimeUnit;

@Configuration
public class RmsGrpcConfig {

    @Bean(destroyMethod = "shutdown")
    public ManagedChannel rmsChannel(RmsProperties props) {
        return ManagedChannelBuilder
                .forAddress(props.getHost(), props.getPort())
                .usePlaintext()
                .build();
    }

    @Bean
    public ReliableMessageServiceGrpc.ReliableMessageServiceBlockingStub rmsStub(
            ManagedChannel rmsChannel, RmsProperties props) {
        return ReliableMessageServiceGrpc.newBlockingStub(rmsChannel)
                .withDeadlineAfter(props.getClient().getDeadlineSeconds(), TimeUnit.SECONDS);
    }
}
