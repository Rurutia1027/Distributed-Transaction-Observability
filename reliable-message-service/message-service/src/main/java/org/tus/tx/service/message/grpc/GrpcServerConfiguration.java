package org.tus.tx.service.message.grpc;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrpcServerConfiguration {

    @Bean
    public Server grpcServer(
            TransactionMessageGrpcService transactionMessageGrpcService,
            @Value("${grpc.server.port:9090}") int port
    ) {
        return ServerBuilder.forPort(port)
                .addService(transactionMessageGrpcService)
                .build();
    }

    @Bean
    public InitializingBean startGrpc(Server grpcServer) {
        return grpcServer::start;
    }

    @Bean
    public DisposableBean stopGrpc(Server grpcServer) {
        return () -> {
            grpcServer.shutdown();
            grpcServer.awaitTermination();
        };
    }
}

