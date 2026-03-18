package org.tus.tx.service.message.grpc;

import io.grpc.Server;
import io.grpc.netty.shaded.io.grpc.netty.NettyServerBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Configuration
@Slf4j
public class GrpcServerConfig {

    @Value("${grpc.server.port:9090}")
    private int grpcPort;

    @Bean
    public Server grpcServer(TransactionMessageGrpcServiceImpl transactionMessageGrpcService) throws IOException {
        Server server = NettyServerBuilder.forPort(grpcPort)
                .addService(TransactionMessageServiceGrpc.bindService(transactionMessageGrpcService))
                .build()
                .start();
        log.info("gRPC server started, listening on port {}", grpcPort);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.info("Shutting down gRPC server");
            try {
                if (server != null) {
                    server.shutdown().awaitTermination(30, TimeUnit.SECONDS);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                server.shutdownNow();
            }
        }));
        return server;
    }

    /**
     * Keep Spring Boot process alive for gRPC-only service.
     */
    @Bean
    public CommandLineRunner grpcServerAwaiter(Server grpcServer) {
        return args -> {
            try {
                grpcServer.awaitTermination();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        };
    }
}
