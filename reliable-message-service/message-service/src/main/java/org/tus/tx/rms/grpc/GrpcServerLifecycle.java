package org.tus.tx.rms.grpc;

import io.grpc.Server;
import io.grpc.netty.shaded.io.grpc.netty.NettyServerBuilder;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class GrpcServerLifecycle {

    private final ReliableMessageGrpcService reliableMessageGrpcService;
    private final ObjectProvider<OutboxRuntimeGrpcService> outboxRuntimeGrpcService;

    @Value("${grpc.server.port:9090}")
    private int port;

    private Server server;

    @PostConstruct
    public void start() throws IOException {
        NettyServerBuilder builder = NettyServerBuilder.forPort(port)
                .addService(reliableMessageGrpcService);
        outboxRuntimeGrpcService.ifAvailable(builder::addService);
        server = builder.build().start();
        log.info("gRPC server started on port {} (outbox gRPC: {})", port,
                outboxRuntimeGrpcService.getIfAvailable() != null);
    }

    @PreDestroy
    public void stop() throws InterruptedException {
        if (server == null) {
            return;
        }
        server.shutdown();
        if (!server.awaitTermination(5, TimeUnit.SECONDS)) {
            server.shutdownNow();
        }
        log.info("gRPC server stopped");
    }
}
