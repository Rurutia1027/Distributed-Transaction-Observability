package org.tus.demo.bank.rms;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.tus.tx.rms.twophase.v1.ReliableMessageServiceGrpc;

@Configuration
@ConditionalOnProperty(prefix = "rms.report", name = "enabled", havingValue = "true")
public class RmsReportConfig {
    @Bean
    public ManagedChannel rmsReportChannel(
            @Value("${rms.grpc.host}") String host,
            @Value("${rms.grpc.port}") int port) {
        return ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
    }

    @Bean
    public ReliableMessageServiceGrpc.ReliableMessageServiceBlockingStub rmsReportStub(ManagedChannel rmsReportChannel) {
        return ReliableMessageServiceGrpc.newBlockingStub(rmsReportChannel);
    }
}
