package org.tus.demo.order.rms;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.tus.tx.rms.twophase.v1.ReliableMessageServiceGrpc;

@Configuration
public class RmsGrpcConfig {

    @Bean
    public ManagedChannel rmsManagedChannel(
            @Value("${rms.grpc.host}") String host,
            @Value("${rms.grpc.port}") int port) {
        return ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();
    }

    @Bean
    public ReliableMessageServiceGrpc.ReliableMessageServiceBlockingStub rmsTwoPhaseStub(ManagedChannel rmsManagedChannel) {
        return ReliableMessageServiceGrpc.newBlockingStub(rmsManagedChannel);
    }
}
