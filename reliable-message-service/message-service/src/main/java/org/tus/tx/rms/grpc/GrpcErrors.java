package org.tus.tx.rms.grpc;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;

final class GrpcErrors {

    private GrpcErrors() {
    }

    static <T> void toObserver(StreamObserver<T> observer, RuntimeException e) {
        observer.onError(toStatus(e).asRuntimeException());
    }

    static Status toStatus(RuntimeException e) {
        String msg = e.getMessage() != null ? e.getMessage() : e.getClass().getSimpleName();
        if (e instanceof IllegalArgumentException) {
            if (msg.contains("not found")) {
                return Status.NOT_FOUND.withDescription(msg).withCause(e);
            }
            if (msg.contains("already exists")) {
                return Status.ALREADY_EXISTS.withDescription(msg).withCause(e);
            }
            return Status.INVALID_ARGUMENT.withDescription(msg).withCause(e);
        }
        if (e instanceof IllegalStateException) {
            return Status.FAILED_PRECONDITION.withDescription(msg).withCause(e);
        }
        return Status.INTERNAL.withDescription(msg).withCause(e);
    }
}
