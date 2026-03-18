package org.tus.tx.service.message.grpc.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.tus.tx.service.message.grpc.ListPageRequest;
import org.tus.tx.service.message.grpc.ListPageResponse;
import org.tus.tx.service.message.grpc.TransactionMessageServiceGrpc;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * Minimal gRPC client wrapper for TransactionMessageService.
 * <p>
 * This is intended to be used by service callers instead of a Dubbo-style Java interface.
 */
public final class TransactionMessageGrpcClient implements AutoCloseable {

    private final ManagedChannel channel;
    private final TransactionMessageServiceGrpc.TransactionMessageServiceBlockingStub blockingStub;

    private TransactionMessageGrpcClient(ManagedChannel channel) {
        this.channel = Objects.requireNonNull(channel, "channel");
        this.blockingStub = TransactionMessageServiceGrpc.newBlockingStub(channel);
    }

    public static TransactionMessageGrpcClient forAddress(String host, int port) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port)
                // TODO: enable TLS in production (use forAddress+useTransportSecurity and proper certs)
                .usePlaintext()
                .build();
        return new TransactionMessageGrpcClient(channel);
    }

    public ListPageResponse listPage(int start, int pageSize, String sortBy, boolean asc) {
        ListPageRequest req = ListPageRequest.newBuilder()
                .setStart(start)
                .setPageSize(pageSize)
                .setSortBy(sortBy == null ? "" : sortBy)
                .setAsc(asc)
                .build();
        return blockingStub.listPage(req);
    }

    @Override
    public void close() {
        channel.shutdown();
        try {
            channel.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            channel.shutdownNow();
        }
    }
}

