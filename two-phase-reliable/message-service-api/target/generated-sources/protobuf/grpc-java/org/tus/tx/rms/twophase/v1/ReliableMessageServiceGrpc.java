package org.tus.tx.rms.twophase.v1;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 * <pre>
 * Two-Phase Reliable Messaging -- see docs/api-contract-two-phase.md
 * </pre>
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.60.1)",
    comments = "Source: rms_twophase_v1.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class ReliableMessageServiceGrpc {

  private ReliableMessageServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "rms.twophase.v1.ReliableMessageService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<org.tus.tx.rms.twophase.v1.PrepareMessageRequest,
      org.tus.tx.rms.twophase.v1.MessageAck> getPrepareMessageMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "PrepareMessage",
      requestType = org.tus.tx.rms.twophase.v1.PrepareMessageRequest.class,
      responseType = org.tus.tx.rms.twophase.v1.MessageAck.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.tus.tx.rms.twophase.v1.PrepareMessageRequest,
      org.tus.tx.rms.twophase.v1.MessageAck> getPrepareMessageMethod() {
    io.grpc.MethodDescriptor<org.tus.tx.rms.twophase.v1.PrepareMessageRequest, org.tus.tx.rms.twophase.v1.MessageAck> getPrepareMessageMethod;
    if ((getPrepareMessageMethod = ReliableMessageServiceGrpc.getPrepareMessageMethod) == null) {
      synchronized (ReliableMessageServiceGrpc.class) {
        if ((getPrepareMessageMethod = ReliableMessageServiceGrpc.getPrepareMessageMethod) == null) {
          ReliableMessageServiceGrpc.getPrepareMessageMethod = getPrepareMessageMethod =
              io.grpc.MethodDescriptor.<org.tus.tx.rms.twophase.v1.PrepareMessageRequest, org.tus.tx.rms.twophase.v1.MessageAck>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "PrepareMessage"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.tus.tx.rms.twophase.v1.PrepareMessageRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.tus.tx.rms.twophase.v1.MessageAck.getDefaultInstance()))
              .setSchemaDescriptor(new ReliableMessageServiceMethodDescriptorSupplier("PrepareMessage"))
              .build();
        }
      }
    }
    return getPrepareMessageMethod;
  }

  private static volatile io.grpc.MethodDescriptor<org.tus.tx.rms.twophase.v1.CommitMessageRequest,
      org.tus.tx.rms.twophase.v1.MessageAck> getCommitMessageMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "CommitMessage",
      requestType = org.tus.tx.rms.twophase.v1.CommitMessageRequest.class,
      responseType = org.tus.tx.rms.twophase.v1.MessageAck.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.tus.tx.rms.twophase.v1.CommitMessageRequest,
      org.tus.tx.rms.twophase.v1.MessageAck> getCommitMessageMethod() {
    io.grpc.MethodDescriptor<org.tus.tx.rms.twophase.v1.CommitMessageRequest, org.tus.tx.rms.twophase.v1.MessageAck> getCommitMessageMethod;
    if ((getCommitMessageMethod = ReliableMessageServiceGrpc.getCommitMessageMethod) == null) {
      synchronized (ReliableMessageServiceGrpc.class) {
        if ((getCommitMessageMethod = ReliableMessageServiceGrpc.getCommitMessageMethod) == null) {
          ReliableMessageServiceGrpc.getCommitMessageMethod = getCommitMessageMethod =
              io.grpc.MethodDescriptor.<org.tus.tx.rms.twophase.v1.CommitMessageRequest, org.tus.tx.rms.twophase.v1.MessageAck>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "CommitMessage"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.tus.tx.rms.twophase.v1.CommitMessageRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.tus.tx.rms.twophase.v1.MessageAck.getDefaultInstance()))
              .setSchemaDescriptor(new ReliableMessageServiceMethodDescriptorSupplier("CommitMessage"))
              .build();
        }
      }
    }
    return getCommitMessageMethod;
  }

  private static volatile io.grpc.MethodDescriptor<org.tus.tx.rms.twophase.v1.RollbackMessageRequest,
      org.tus.tx.rms.twophase.v1.MessageAck> getRollbackMessageMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "RollbackMessage",
      requestType = org.tus.tx.rms.twophase.v1.RollbackMessageRequest.class,
      responseType = org.tus.tx.rms.twophase.v1.MessageAck.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.tus.tx.rms.twophase.v1.RollbackMessageRequest,
      org.tus.tx.rms.twophase.v1.MessageAck> getRollbackMessageMethod() {
    io.grpc.MethodDescriptor<org.tus.tx.rms.twophase.v1.RollbackMessageRequest, org.tus.tx.rms.twophase.v1.MessageAck> getRollbackMessageMethod;
    if ((getRollbackMessageMethod = ReliableMessageServiceGrpc.getRollbackMessageMethod) == null) {
      synchronized (ReliableMessageServiceGrpc.class) {
        if ((getRollbackMessageMethod = ReliableMessageServiceGrpc.getRollbackMessageMethod) == null) {
          ReliableMessageServiceGrpc.getRollbackMessageMethod = getRollbackMessageMethod =
              io.grpc.MethodDescriptor.<org.tus.tx.rms.twophase.v1.RollbackMessageRequest, org.tus.tx.rms.twophase.v1.MessageAck>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "RollbackMessage"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.tus.tx.rms.twophase.v1.RollbackMessageRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.tus.tx.rms.twophase.v1.MessageAck.getDefaultInstance()))
              .setSchemaDescriptor(new ReliableMessageServiceMethodDescriptorSupplier("RollbackMessage"))
              .build();
        }
      }
    }
    return getRollbackMessageMethod;
  }

  private static volatile io.grpc.MethodDescriptor<org.tus.tx.rms.twophase.v1.GetMessageStatusRequest,
      org.tus.tx.rms.twophase.v1.MessageStatusResponse> getGetMessageStatusMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetMessageStatus",
      requestType = org.tus.tx.rms.twophase.v1.GetMessageStatusRequest.class,
      responseType = org.tus.tx.rms.twophase.v1.MessageStatusResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.tus.tx.rms.twophase.v1.GetMessageStatusRequest,
      org.tus.tx.rms.twophase.v1.MessageStatusResponse> getGetMessageStatusMethod() {
    io.grpc.MethodDescriptor<org.tus.tx.rms.twophase.v1.GetMessageStatusRequest, org.tus.tx.rms.twophase.v1.MessageStatusResponse> getGetMessageStatusMethod;
    if ((getGetMessageStatusMethod = ReliableMessageServiceGrpc.getGetMessageStatusMethod) == null) {
      synchronized (ReliableMessageServiceGrpc.class) {
        if ((getGetMessageStatusMethod = ReliableMessageServiceGrpc.getGetMessageStatusMethod) == null) {
          ReliableMessageServiceGrpc.getGetMessageStatusMethod = getGetMessageStatusMethod =
              io.grpc.MethodDescriptor.<org.tus.tx.rms.twophase.v1.GetMessageStatusRequest, org.tus.tx.rms.twophase.v1.MessageStatusResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetMessageStatus"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.tus.tx.rms.twophase.v1.GetMessageStatusRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.tus.tx.rms.twophase.v1.MessageStatusResponse.getDefaultInstance()))
              .setSchemaDescriptor(new ReliableMessageServiceMethodDescriptorSupplier("GetMessageStatus"))
              .build();
        }
      }
    }
    return getGetMessageStatusMethod;
  }

  private static volatile io.grpc.MethodDescriptor<org.tus.tx.rms.twophase.v1.ReportConsumeResultRequest,
      org.tus.tx.rms.twophase.v1.MessageAck> getReportConsumeResultMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ReportConsumeResult",
      requestType = org.tus.tx.rms.twophase.v1.ReportConsumeResultRequest.class,
      responseType = org.tus.tx.rms.twophase.v1.MessageAck.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.tus.tx.rms.twophase.v1.ReportConsumeResultRequest,
      org.tus.tx.rms.twophase.v1.MessageAck> getReportConsumeResultMethod() {
    io.grpc.MethodDescriptor<org.tus.tx.rms.twophase.v1.ReportConsumeResultRequest, org.tus.tx.rms.twophase.v1.MessageAck> getReportConsumeResultMethod;
    if ((getReportConsumeResultMethod = ReliableMessageServiceGrpc.getReportConsumeResultMethod) == null) {
      synchronized (ReliableMessageServiceGrpc.class) {
        if ((getReportConsumeResultMethod = ReliableMessageServiceGrpc.getReportConsumeResultMethod) == null) {
          ReliableMessageServiceGrpc.getReportConsumeResultMethod = getReportConsumeResultMethod =
              io.grpc.MethodDescriptor.<org.tus.tx.rms.twophase.v1.ReportConsumeResultRequest, org.tus.tx.rms.twophase.v1.MessageAck>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "ReportConsumeResult"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.tus.tx.rms.twophase.v1.ReportConsumeResultRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.tus.tx.rms.twophase.v1.MessageAck.getDefaultInstance()))
              .setSchemaDescriptor(new ReliableMessageServiceMethodDescriptorSupplier("ReportConsumeResult"))
              .build();
        }
      }
    }
    return getReportConsumeResultMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static ReliableMessageServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ReliableMessageServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ReliableMessageServiceStub>() {
        @java.lang.Override
        public ReliableMessageServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ReliableMessageServiceStub(channel, callOptions);
        }
      };
    return ReliableMessageServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static ReliableMessageServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ReliableMessageServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ReliableMessageServiceBlockingStub>() {
        @java.lang.Override
        public ReliableMessageServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ReliableMessageServiceBlockingStub(channel, callOptions);
        }
      };
    return ReliableMessageServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static ReliableMessageServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ReliableMessageServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ReliableMessageServiceFutureStub>() {
        @java.lang.Override
        public ReliableMessageServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ReliableMessageServiceFutureStub(channel, callOptions);
        }
      };
    return ReliableMessageServiceFutureStub.newStub(factory, channel);
  }

  /**
   * <pre>
   * Two-Phase Reliable Messaging -- see docs/api-contract-two-phase.md
   * </pre>
   */
  public interface AsyncService {

    /**
     */
    default void prepareMessage(org.tus.tx.rms.twophase.v1.PrepareMessageRequest request,
        io.grpc.stub.StreamObserver<org.tus.tx.rms.twophase.v1.MessageAck> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getPrepareMessageMethod(), responseObserver);
    }

    /**
     */
    default void commitMessage(org.tus.tx.rms.twophase.v1.CommitMessageRequest request,
        io.grpc.stub.StreamObserver<org.tus.tx.rms.twophase.v1.MessageAck> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getCommitMessageMethod(), responseObserver);
    }

    /**
     */
    default void rollbackMessage(org.tus.tx.rms.twophase.v1.RollbackMessageRequest request,
        io.grpc.stub.StreamObserver<org.tus.tx.rms.twophase.v1.MessageAck> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getRollbackMessageMethod(), responseObserver);
    }

    /**
     */
    default void getMessageStatus(org.tus.tx.rms.twophase.v1.GetMessageStatusRequest request,
        io.grpc.stub.StreamObserver<org.tus.tx.rms.twophase.v1.MessageStatusResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetMessageStatusMethod(), responseObserver);
    }

    /**
     */
    default void reportConsumeResult(org.tus.tx.rms.twophase.v1.ReportConsumeResultRequest request,
        io.grpc.stub.StreamObserver<org.tus.tx.rms.twophase.v1.MessageAck> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getReportConsumeResultMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service ReliableMessageService.
   * <pre>
   * Two-Phase Reliable Messaging -- see docs/api-contract-two-phase.md
   * </pre>
   */
  public static abstract class ReliableMessageServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return ReliableMessageServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service ReliableMessageService.
   * <pre>
   * Two-Phase Reliable Messaging -- see docs/api-contract-two-phase.md
   * </pre>
   */
  public static final class ReliableMessageServiceStub
      extends io.grpc.stub.AbstractAsyncStub<ReliableMessageServiceStub> {
    private ReliableMessageServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ReliableMessageServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ReliableMessageServiceStub(channel, callOptions);
    }

    /**
     */
    public void prepareMessage(org.tus.tx.rms.twophase.v1.PrepareMessageRequest request,
        io.grpc.stub.StreamObserver<org.tus.tx.rms.twophase.v1.MessageAck> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getPrepareMessageMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void commitMessage(org.tus.tx.rms.twophase.v1.CommitMessageRequest request,
        io.grpc.stub.StreamObserver<org.tus.tx.rms.twophase.v1.MessageAck> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getCommitMessageMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void rollbackMessage(org.tus.tx.rms.twophase.v1.RollbackMessageRequest request,
        io.grpc.stub.StreamObserver<org.tus.tx.rms.twophase.v1.MessageAck> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getRollbackMessageMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getMessageStatus(org.tus.tx.rms.twophase.v1.GetMessageStatusRequest request,
        io.grpc.stub.StreamObserver<org.tus.tx.rms.twophase.v1.MessageStatusResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetMessageStatusMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void reportConsumeResult(org.tus.tx.rms.twophase.v1.ReportConsumeResultRequest request,
        io.grpc.stub.StreamObserver<org.tus.tx.rms.twophase.v1.MessageAck> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getReportConsumeResultMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service ReliableMessageService.
   * <pre>
   * Two-Phase Reliable Messaging -- see docs/api-contract-two-phase.md
   * </pre>
   */
  public static final class ReliableMessageServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<ReliableMessageServiceBlockingStub> {
    private ReliableMessageServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ReliableMessageServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ReliableMessageServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public org.tus.tx.rms.twophase.v1.MessageAck prepareMessage(org.tus.tx.rms.twophase.v1.PrepareMessageRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getPrepareMessageMethod(), getCallOptions(), request);
    }

    /**
     */
    public org.tus.tx.rms.twophase.v1.MessageAck commitMessage(org.tus.tx.rms.twophase.v1.CommitMessageRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getCommitMessageMethod(), getCallOptions(), request);
    }

    /**
     */
    public org.tus.tx.rms.twophase.v1.MessageAck rollbackMessage(org.tus.tx.rms.twophase.v1.RollbackMessageRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getRollbackMessageMethod(), getCallOptions(), request);
    }

    /**
     */
    public org.tus.tx.rms.twophase.v1.MessageStatusResponse getMessageStatus(org.tus.tx.rms.twophase.v1.GetMessageStatusRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetMessageStatusMethod(), getCallOptions(), request);
    }

    /**
     */
    public org.tus.tx.rms.twophase.v1.MessageAck reportConsumeResult(org.tus.tx.rms.twophase.v1.ReportConsumeResultRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getReportConsumeResultMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service ReliableMessageService.
   * <pre>
   * Two-Phase Reliable Messaging -- see docs/api-contract-two-phase.md
   * </pre>
   */
  public static final class ReliableMessageServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<ReliableMessageServiceFutureStub> {
    private ReliableMessageServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ReliableMessageServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ReliableMessageServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<org.tus.tx.rms.twophase.v1.MessageAck> prepareMessage(
        org.tus.tx.rms.twophase.v1.PrepareMessageRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getPrepareMessageMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<org.tus.tx.rms.twophase.v1.MessageAck> commitMessage(
        org.tus.tx.rms.twophase.v1.CommitMessageRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getCommitMessageMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<org.tus.tx.rms.twophase.v1.MessageAck> rollbackMessage(
        org.tus.tx.rms.twophase.v1.RollbackMessageRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getRollbackMessageMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<org.tus.tx.rms.twophase.v1.MessageStatusResponse> getMessageStatus(
        org.tus.tx.rms.twophase.v1.GetMessageStatusRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetMessageStatusMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<org.tus.tx.rms.twophase.v1.MessageAck> reportConsumeResult(
        org.tus.tx.rms.twophase.v1.ReportConsumeResultRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getReportConsumeResultMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_PREPARE_MESSAGE = 0;
  private static final int METHODID_COMMIT_MESSAGE = 1;
  private static final int METHODID_ROLLBACK_MESSAGE = 2;
  private static final int METHODID_GET_MESSAGE_STATUS = 3;
  private static final int METHODID_REPORT_CONSUME_RESULT = 4;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AsyncService serviceImpl;
    private final int methodId;

    MethodHandlers(AsyncService serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_PREPARE_MESSAGE:
          serviceImpl.prepareMessage((org.tus.tx.rms.twophase.v1.PrepareMessageRequest) request,
              (io.grpc.stub.StreamObserver<org.tus.tx.rms.twophase.v1.MessageAck>) responseObserver);
          break;
        case METHODID_COMMIT_MESSAGE:
          serviceImpl.commitMessage((org.tus.tx.rms.twophase.v1.CommitMessageRequest) request,
              (io.grpc.stub.StreamObserver<org.tus.tx.rms.twophase.v1.MessageAck>) responseObserver);
          break;
        case METHODID_ROLLBACK_MESSAGE:
          serviceImpl.rollbackMessage((org.tus.tx.rms.twophase.v1.RollbackMessageRequest) request,
              (io.grpc.stub.StreamObserver<org.tus.tx.rms.twophase.v1.MessageAck>) responseObserver);
          break;
        case METHODID_GET_MESSAGE_STATUS:
          serviceImpl.getMessageStatus((org.tus.tx.rms.twophase.v1.GetMessageStatusRequest) request,
              (io.grpc.stub.StreamObserver<org.tus.tx.rms.twophase.v1.MessageStatusResponse>) responseObserver);
          break;
        case METHODID_REPORT_CONSUME_RESULT:
          serviceImpl.reportConsumeResult((org.tus.tx.rms.twophase.v1.ReportConsumeResultRequest) request,
              (io.grpc.stub.StreamObserver<org.tus.tx.rms.twophase.v1.MessageAck>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getPrepareMessageMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              org.tus.tx.rms.twophase.v1.PrepareMessageRequest,
              org.tus.tx.rms.twophase.v1.MessageAck>(
                service, METHODID_PREPARE_MESSAGE)))
        .addMethod(
          getCommitMessageMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              org.tus.tx.rms.twophase.v1.CommitMessageRequest,
              org.tus.tx.rms.twophase.v1.MessageAck>(
                service, METHODID_COMMIT_MESSAGE)))
        .addMethod(
          getRollbackMessageMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              org.tus.tx.rms.twophase.v1.RollbackMessageRequest,
              org.tus.tx.rms.twophase.v1.MessageAck>(
                service, METHODID_ROLLBACK_MESSAGE)))
        .addMethod(
          getGetMessageStatusMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              org.tus.tx.rms.twophase.v1.GetMessageStatusRequest,
              org.tus.tx.rms.twophase.v1.MessageStatusResponse>(
                service, METHODID_GET_MESSAGE_STATUS)))
        .addMethod(
          getReportConsumeResultMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              org.tus.tx.rms.twophase.v1.ReportConsumeResultRequest,
              org.tus.tx.rms.twophase.v1.MessageAck>(
                service, METHODID_REPORT_CONSUME_RESULT)))
        .build();
  }

  private static abstract class ReliableMessageServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    ReliableMessageServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return org.tus.tx.rms.twophase.v1.RelicableMessageProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("ReliableMessageService");
    }
  }

  private static final class ReliableMessageServiceFileDescriptorSupplier
      extends ReliableMessageServiceBaseDescriptorSupplier {
    ReliableMessageServiceFileDescriptorSupplier() {}
  }

  private static final class ReliableMessageServiceMethodDescriptorSupplier
      extends ReliableMessageServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    ReliableMessageServiceMethodDescriptorSupplier(java.lang.String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (ReliableMessageServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new ReliableMessageServiceFileDescriptorSupplier())
              .addMethod(getPrepareMessageMethod())
              .addMethod(getCommitMessageMethod())
              .addMethod(getRollbackMessageMethod())
              .addMethod(getGetMessageStatusMethod())
              .addMethod(getReportConsumeResultMethod())
              .build();
        }
      }
    }
    return result;
  }
}
