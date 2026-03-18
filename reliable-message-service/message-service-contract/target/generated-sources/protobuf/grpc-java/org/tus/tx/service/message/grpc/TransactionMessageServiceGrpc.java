package org.tus.tx.service.message.grpc;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.60.1)",
    comments = "Source: transaction_message.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class TransactionMessageServiceGrpc {

  private TransactionMessageServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "tus.tx.message.TransactionMessageService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<org.tus.tx.service.message.grpc.SaveMessageWaitingConfirmRequest,
      org.tus.tx.service.message.grpc.SaveMessageWaitingConfirmResponse> getSaveMessageWaitingConfirmMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "SaveMessageWaitingConfirm",
      requestType = org.tus.tx.service.message.grpc.SaveMessageWaitingConfirmRequest.class,
      responseType = org.tus.tx.service.message.grpc.SaveMessageWaitingConfirmResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.tus.tx.service.message.grpc.SaveMessageWaitingConfirmRequest,
      org.tus.tx.service.message.grpc.SaveMessageWaitingConfirmResponse> getSaveMessageWaitingConfirmMethod() {
    io.grpc.MethodDescriptor<org.tus.tx.service.message.grpc.SaveMessageWaitingConfirmRequest, org.tus.tx.service.message.grpc.SaveMessageWaitingConfirmResponse> getSaveMessageWaitingConfirmMethod;
    if ((getSaveMessageWaitingConfirmMethod = TransactionMessageServiceGrpc.getSaveMessageWaitingConfirmMethod) == null) {
      synchronized (TransactionMessageServiceGrpc.class) {
        if ((getSaveMessageWaitingConfirmMethod = TransactionMessageServiceGrpc.getSaveMessageWaitingConfirmMethod) == null) {
          TransactionMessageServiceGrpc.getSaveMessageWaitingConfirmMethod = getSaveMessageWaitingConfirmMethod =
              io.grpc.MethodDescriptor.<org.tus.tx.service.message.grpc.SaveMessageWaitingConfirmRequest, org.tus.tx.service.message.grpc.SaveMessageWaitingConfirmResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "SaveMessageWaitingConfirm"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.tus.tx.service.message.grpc.SaveMessageWaitingConfirmRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.tus.tx.service.message.grpc.SaveMessageWaitingConfirmResponse.getDefaultInstance()))
              .setSchemaDescriptor(new TransactionMessageServiceMethodDescriptorSupplier("SaveMessageWaitingConfirm"))
              .build();
        }
      }
    }
    return getSaveMessageWaitingConfirmMethod;
  }

  private static volatile io.grpc.MethodDescriptor<org.tus.tx.service.message.grpc.ConfirmAndSendRequest,
      org.tus.tx.service.message.grpc.ConfirmAndSendResponse> getConfirmAndSendMessageMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ConfirmAndSendMessage",
      requestType = org.tus.tx.service.message.grpc.ConfirmAndSendRequest.class,
      responseType = org.tus.tx.service.message.grpc.ConfirmAndSendResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.tus.tx.service.message.grpc.ConfirmAndSendRequest,
      org.tus.tx.service.message.grpc.ConfirmAndSendResponse> getConfirmAndSendMessageMethod() {
    io.grpc.MethodDescriptor<org.tus.tx.service.message.grpc.ConfirmAndSendRequest, org.tus.tx.service.message.grpc.ConfirmAndSendResponse> getConfirmAndSendMessageMethod;
    if ((getConfirmAndSendMessageMethod = TransactionMessageServiceGrpc.getConfirmAndSendMessageMethod) == null) {
      synchronized (TransactionMessageServiceGrpc.class) {
        if ((getConfirmAndSendMessageMethod = TransactionMessageServiceGrpc.getConfirmAndSendMessageMethod) == null) {
          TransactionMessageServiceGrpc.getConfirmAndSendMessageMethod = getConfirmAndSendMessageMethod =
              io.grpc.MethodDescriptor.<org.tus.tx.service.message.grpc.ConfirmAndSendRequest, org.tus.tx.service.message.grpc.ConfirmAndSendResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "ConfirmAndSendMessage"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.tus.tx.service.message.grpc.ConfirmAndSendRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.tus.tx.service.message.grpc.ConfirmAndSendResponse.getDefaultInstance()))
              .setSchemaDescriptor(new TransactionMessageServiceMethodDescriptorSupplier("ConfirmAndSendMessage"))
              .build();
        }
      }
    }
    return getConfirmAndSendMessageMethod;
  }

  private static volatile io.grpc.MethodDescriptor<org.tus.tx.service.message.grpc.SaveAndSendRequest,
      org.tus.tx.service.message.grpc.SaveAndSendResponse> getSaveAndSendMessageMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "SaveAndSendMessage",
      requestType = org.tus.tx.service.message.grpc.SaveAndSendRequest.class,
      responseType = org.tus.tx.service.message.grpc.SaveAndSendResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.tus.tx.service.message.grpc.SaveAndSendRequest,
      org.tus.tx.service.message.grpc.SaveAndSendResponse> getSaveAndSendMessageMethod() {
    io.grpc.MethodDescriptor<org.tus.tx.service.message.grpc.SaveAndSendRequest, org.tus.tx.service.message.grpc.SaveAndSendResponse> getSaveAndSendMessageMethod;
    if ((getSaveAndSendMessageMethod = TransactionMessageServiceGrpc.getSaveAndSendMessageMethod) == null) {
      synchronized (TransactionMessageServiceGrpc.class) {
        if ((getSaveAndSendMessageMethod = TransactionMessageServiceGrpc.getSaveAndSendMessageMethod) == null) {
          TransactionMessageServiceGrpc.getSaveAndSendMessageMethod = getSaveAndSendMessageMethod =
              io.grpc.MethodDescriptor.<org.tus.tx.service.message.grpc.SaveAndSendRequest, org.tus.tx.service.message.grpc.SaveAndSendResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "SaveAndSendMessage"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.tus.tx.service.message.grpc.SaveAndSendRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.tus.tx.service.message.grpc.SaveAndSendResponse.getDefaultInstance()))
              .setSchemaDescriptor(new TransactionMessageServiceMethodDescriptorSupplier("SaveAndSendMessage"))
              .build();
        }
      }
    }
    return getSaveAndSendMessageMethod;
  }

  private static volatile io.grpc.MethodDescriptor<org.tus.tx.service.message.grpc.DirectSendRequest,
      org.tus.tx.service.message.grpc.DirectSendResponse> getDirectSendMessageMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "DirectSendMessage",
      requestType = org.tus.tx.service.message.grpc.DirectSendRequest.class,
      responseType = org.tus.tx.service.message.grpc.DirectSendResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.tus.tx.service.message.grpc.DirectSendRequest,
      org.tus.tx.service.message.grpc.DirectSendResponse> getDirectSendMessageMethod() {
    io.grpc.MethodDescriptor<org.tus.tx.service.message.grpc.DirectSendRequest, org.tus.tx.service.message.grpc.DirectSendResponse> getDirectSendMessageMethod;
    if ((getDirectSendMessageMethod = TransactionMessageServiceGrpc.getDirectSendMessageMethod) == null) {
      synchronized (TransactionMessageServiceGrpc.class) {
        if ((getDirectSendMessageMethod = TransactionMessageServiceGrpc.getDirectSendMessageMethod) == null) {
          TransactionMessageServiceGrpc.getDirectSendMessageMethod = getDirectSendMessageMethod =
              io.grpc.MethodDescriptor.<org.tus.tx.service.message.grpc.DirectSendRequest, org.tus.tx.service.message.grpc.DirectSendResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "DirectSendMessage"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.tus.tx.service.message.grpc.DirectSendRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.tus.tx.service.message.grpc.DirectSendResponse.getDefaultInstance()))
              .setSchemaDescriptor(new TransactionMessageServiceMethodDescriptorSupplier("DirectSendMessage"))
              .build();
        }
      }
    }
    return getDirectSendMessageMethod;
  }

  private static volatile io.grpc.MethodDescriptor<org.tus.tx.service.message.grpc.ReSendMessageRequest,
      org.tus.tx.service.message.grpc.ReSendMessageResponse> getReSendMessageMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ReSendMessage",
      requestType = org.tus.tx.service.message.grpc.ReSendMessageRequest.class,
      responseType = org.tus.tx.service.message.grpc.ReSendMessageResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.tus.tx.service.message.grpc.ReSendMessageRequest,
      org.tus.tx.service.message.grpc.ReSendMessageResponse> getReSendMessageMethod() {
    io.grpc.MethodDescriptor<org.tus.tx.service.message.grpc.ReSendMessageRequest, org.tus.tx.service.message.grpc.ReSendMessageResponse> getReSendMessageMethod;
    if ((getReSendMessageMethod = TransactionMessageServiceGrpc.getReSendMessageMethod) == null) {
      synchronized (TransactionMessageServiceGrpc.class) {
        if ((getReSendMessageMethod = TransactionMessageServiceGrpc.getReSendMessageMethod) == null) {
          TransactionMessageServiceGrpc.getReSendMessageMethod = getReSendMessageMethod =
              io.grpc.MethodDescriptor.<org.tus.tx.service.message.grpc.ReSendMessageRequest, org.tus.tx.service.message.grpc.ReSendMessageResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "ReSendMessage"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.tus.tx.service.message.grpc.ReSendMessageRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.tus.tx.service.message.grpc.ReSendMessageResponse.getDefaultInstance()))
              .setSchemaDescriptor(new TransactionMessageServiceMethodDescriptorSupplier("ReSendMessage"))
              .build();
        }
      }
    }
    return getReSendMessageMethod;
  }

  private static volatile io.grpc.MethodDescriptor<org.tus.tx.service.message.grpc.ReSendByMessageIdRequest,
      org.tus.tx.service.message.grpc.ReSendByMessageIdResponse> getReSendMessageByMessageIdMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ReSendMessageByMessageId",
      requestType = org.tus.tx.service.message.grpc.ReSendByMessageIdRequest.class,
      responseType = org.tus.tx.service.message.grpc.ReSendByMessageIdResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.tus.tx.service.message.grpc.ReSendByMessageIdRequest,
      org.tus.tx.service.message.grpc.ReSendByMessageIdResponse> getReSendMessageByMessageIdMethod() {
    io.grpc.MethodDescriptor<org.tus.tx.service.message.grpc.ReSendByMessageIdRequest, org.tus.tx.service.message.grpc.ReSendByMessageIdResponse> getReSendMessageByMessageIdMethod;
    if ((getReSendMessageByMessageIdMethod = TransactionMessageServiceGrpc.getReSendMessageByMessageIdMethod) == null) {
      synchronized (TransactionMessageServiceGrpc.class) {
        if ((getReSendMessageByMessageIdMethod = TransactionMessageServiceGrpc.getReSendMessageByMessageIdMethod) == null) {
          TransactionMessageServiceGrpc.getReSendMessageByMessageIdMethod = getReSendMessageByMessageIdMethod =
              io.grpc.MethodDescriptor.<org.tus.tx.service.message.grpc.ReSendByMessageIdRequest, org.tus.tx.service.message.grpc.ReSendByMessageIdResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "ReSendMessageByMessageId"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.tus.tx.service.message.grpc.ReSendByMessageIdRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.tus.tx.service.message.grpc.ReSendByMessageIdResponse.getDefaultInstance()))
              .setSchemaDescriptor(new TransactionMessageServiceMethodDescriptorSupplier("ReSendMessageByMessageId"))
              .build();
        }
      }
    }
    return getReSendMessageByMessageIdMethod;
  }

  private static volatile io.grpc.MethodDescriptor<org.tus.tx.service.message.grpc.SetMessageToAlreadyDeadRequest,
      org.tus.tx.service.message.grpc.SetMessageToAlreadyDeadResponse> getSetMessageToAlreadyDeadMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "SetMessageToAlreadyDead",
      requestType = org.tus.tx.service.message.grpc.SetMessageToAlreadyDeadRequest.class,
      responseType = org.tus.tx.service.message.grpc.SetMessageToAlreadyDeadResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.tus.tx.service.message.grpc.SetMessageToAlreadyDeadRequest,
      org.tus.tx.service.message.grpc.SetMessageToAlreadyDeadResponse> getSetMessageToAlreadyDeadMethod() {
    io.grpc.MethodDescriptor<org.tus.tx.service.message.grpc.SetMessageToAlreadyDeadRequest, org.tus.tx.service.message.grpc.SetMessageToAlreadyDeadResponse> getSetMessageToAlreadyDeadMethod;
    if ((getSetMessageToAlreadyDeadMethod = TransactionMessageServiceGrpc.getSetMessageToAlreadyDeadMethod) == null) {
      synchronized (TransactionMessageServiceGrpc.class) {
        if ((getSetMessageToAlreadyDeadMethod = TransactionMessageServiceGrpc.getSetMessageToAlreadyDeadMethod) == null) {
          TransactionMessageServiceGrpc.getSetMessageToAlreadyDeadMethod = getSetMessageToAlreadyDeadMethod =
              io.grpc.MethodDescriptor.<org.tus.tx.service.message.grpc.SetMessageToAlreadyDeadRequest, org.tus.tx.service.message.grpc.SetMessageToAlreadyDeadResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "SetMessageToAlreadyDead"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.tus.tx.service.message.grpc.SetMessageToAlreadyDeadRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.tus.tx.service.message.grpc.SetMessageToAlreadyDeadResponse.getDefaultInstance()))
              .setSchemaDescriptor(new TransactionMessageServiceMethodDescriptorSupplier("SetMessageToAlreadyDead"))
              .build();
        }
      }
    }
    return getSetMessageToAlreadyDeadMethod;
  }

  private static volatile io.grpc.MethodDescriptor<org.tus.tx.service.message.grpc.DeleteMessageRequest,
      org.tus.tx.service.message.grpc.DeleteMessageResponse> getDeleteMessageByMessageIdMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "DeleteMessageByMessageId",
      requestType = org.tus.tx.service.message.grpc.DeleteMessageRequest.class,
      responseType = org.tus.tx.service.message.grpc.DeleteMessageResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.tus.tx.service.message.grpc.DeleteMessageRequest,
      org.tus.tx.service.message.grpc.DeleteMessageResponse> getDeleteMessageByMessageIdMethod() {
    io.grpc.MethodDescriptor<org.tus.tx.service.message.grpc.DeleteMessageRequest, org.tus.tx.service.message.grpc.DeleteMessageResponse> getDeleteMessageByMessageIdMethod;
    if ((getDeleteMessageByMessageIdMethod = TransactionMessageServiceGrpc.getDeleteMessageByMessageIdMethod) == null) {
      synchronized (TransactionMessageServiceGrpc.class) {
        if ((getDeleteMessageByMessageIdMethod = TransactionMessageServiceGrpc.getDeleteMessageByMessageIdMethod) == null) {
          TransactionMessageServiceGrpc.getDeleteMessageByMessageIdMethod = getDeleteMessageByMessageIdMethod =
              io.grpc.MethodDescriptor.<org.tus.tx.service.message.grpc.DeleteMessageRequest, org.tus.tx.service.message.grpc.DeleteMessageResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "DeleteMessageByMessageId"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.tus.tx.service.message.grpc.DeleteMessageRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.tus.tx.service.message.grpc.DeleteMessageResponse.getDefaultInstance()))
              .setSchemaDescriptor(new TransactionMessageServiceMethodDescriptorSupplier("DeleteMessageByMessageId"))
              .build();
        }
      }
    }
    return getDeleteMessageByMessageIdMethod;
  }

  private static volatile io.grpc.MethodDescriptor<org.tus.tx.service.message.grpc.ReSendAllDeadRequest,
      org.tus.tx.service.message.grpc.ReSendAllDeadResponse> getReSendAllDeadMessageByQueueNameMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ReSendAllDeadMessageByQueueName",
      requestType = org.tus.tx.service.message.grpc.ReSendAllDeadRequest.class,
      responseType = org.tus.tx.service.message.grpc.ReSendAllDeadResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.tus.tx.service.message.grpc.ReSendAllDeadRequest,
      org.tus.tx.service.message.grpc.ReSendAllDeadResponse> getReSendAllDeadMessageByQueueNameMethod() {
    io.grpc.MethodDescriptor<org.tus.tx.service.message.grpc.ReSendAllDeadRequest, org.tus.tx.service.message.grpc.ReSendAllDeadResponse> getReSendAllDeadMessageByQueueNameMethod;
    if ((getReSendAllDeadMessageByQueueNameMethod = TransactionMessageServiceGrpc.getReSendAllDeadMessageByQueueNameMethod) == null) {
      synchronized (TransactionMessageServiceGrpc.class) {
        if ((getReSendAllDeadMessageByQueueNameMethod = TransactionMessageServiceGrpc.getReSendAllDeadMessageByQueueNameMethod) == null) {
          TransactionMessageServiceGrpc.getReSendAllDeadMessageByQueueNameMethod = getReSendAllDeadMessageByQueueNameMethod =
              io.grpc.MethodDescriptor.<org.tus.tx.service.message.grpc.ReSendAllDeadRequest, org.tus.tx.service.message.grpc.ReSendAllDeadResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "ReSendAllDeadMessageByQueueName"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.tus.tx.service.message.grpc.ReSendAllDeadRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.tus.tx.service.message.grpc.ReSendAllDeadResponse.getDefaultInstance()))
              .setSchemaDescriptor(new TransactionMessageServiceMethodDescriptorSupplier("ReSendAllDeadMessageByQueueName"))
              .build();
        }
      }
    }
    return getReSendAllDeadMessageByQueueNameMethod;
  }

  private static volatile io.grpc.MethodDescriptor<org.tus.tx.service.message.grpc.ListPageRequest,
      org.tus.tx.service.message.grpc.ListPageResponse> getListPageMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ListPage",
      requestType = org.tus.tx.service.message.grpc.ListPageRequest.class,
      responseType = org.tus.tx.service.message.grpc.ListPageResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.tus.tx.service.message.grpc.ListPageRequest,
      org.tus.tx.service.message.grpc.ListPageResponse> getListPageMethod() {
    io.grpc.MethodDescriptor<org.tus.tx.service.message.grpc.ListPageRequest, org.tus.tx.service.message.grpc.ListPageResponse> getListPageMethod;
    if ((getListPageMethod = TransactionMessageServiceGrpc.getListPageMethod) == null) {
      synchronized (TransactionMessageServiceGrpc.class) {
        if ((getListPageMethod = TransactionMessageServiceGrpc.getListPageMethod) == null) {
          TransactionMessageServiceGrpc.getListPageMethod = getListPageMethod =
              io.grpc.MethodDescriptor.<org.tus.tx.service.message.grpc.ListPageRequest, org.tus.tx.service.message.grpc.ListPageResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "ListPage"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.tus.tx.service.message.grpc.ListPageRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.tus.tx.service.message.grpc.ListPageResponse.getDefaultInstance()))
              .setSchemaDescriptor(new TransactionMessageServiceMethodDescriptorSupplier("ListPage"))
              .build();
        }
      }
    }
    return getListPageMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static TransactionMessageServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<TransactionMessageServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<TransactionMessageServiceStub>() {
        @java.lang.Override
        public TransactionMessageServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new TransactionMessageServiceStub(channel, callOptions);
        }
      };
    return TransactionMessageServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static TransactionMessageServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<TransactionMessageServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<TransactionMessageServiceBlockingStub>() {
        @java.lang.Override
        public TransactionMessageServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new TransactionMessageServiceBlockingStub(channel, callOptions);
        }
      };
    return TransactionMessageServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static TransactionMessageServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<TransactionMessageServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<TransactionMessageServiceFutureStub>() {
        @java.lang.Override
        public TransactionMessageServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new TransactionMessageServiceFutureStub(channel, callOptions);
        }
      };
    return TransactionMessageServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void saveMessageWaitingConfirm(org.tus.tx.service.message.grpc.SaveMessageWaitingConfirmRequest request,
        io.grpc.stub.StreamObserver<org.tus.tx.service.message.grpc.SaveMessageWaitingConfirmResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getSaveMessageWaitingConfirmMethod(), responseObserver);
    }

    /**
     */
    default void confirmAndSendMessage(org.tus.tx.service.message.grpc.ConfirmAndSendRequest request,
        io.grpc.stub.StreamObserver<org.tus.tx.service.message.grpc.ConfirmAndSendResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getConfirmAndSendMessageMethod(), responseObserver);
    }

    /**
     */
    default void saveAndSendMessage(org.tus.tx.service.message.grpc.SaveAndSendRequest request,
        io.grpc.stub.StreamObserver<org.tus.tx.service.message.grpc.SaveAndSendResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getSaveAndSendMessageMethod(), responseObserver);
    }

    /**
     */
    default void directSendMessage(org.tus.tx.service.message.grpc.DirectSendRequest request,
        io.grpc.stub.StreamObserver<org.tus.tx.service.message.grpc.DirectSendResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getDirectSendMessageMethod(), responseObserver);
    }

    /**
     */
    default void reSendMessage(org.tus.tx.service.message.grpc.ReSendMessageRequest request,
        io.grpc.stub.StreamObserver<org.tus.tx.service.message.grpc.ReSendMessageResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getReSendMessageMethod(), responseObserver);
    }

    /**
     */
    default void reSendMessageByMessageId(org.tus.tx.service.message.grpc.ReSendByMessageIdRequest request,
        io.grpc.stub.StreamObserver<org.tus.tx.service.message.grpc.ReSendByMessageIdResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getReSendMessageByMessageIdMethod(), responseObserver);
    }

    /**
     */
    default void setMessageToAlreadyDead(org.tus.tx.service.message.grpc.SetMessageToAlreadyDeadRequest request,
        io.grpc.stub.StreamObserver<org.tus.tx.service.message.grpc.SetMessageToAlreadyDeadResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getSetMessageToAlreadyDeadMethod(), responseObserver);
    }

    /**
     */
    default void deleteMessageByMessageId(org.tus.tx.service.message.grpc.DeleteMessageRequest request,
        io.grpc.stub.StreamObserver<org.tus.tx.service.message.grpc.DeleteMessageResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getDeleteMessageByMessageIdMethod(), responseObserver);
    }

    /**
     */
    default void reSendAllDeadMessageByQueueName(org.tus.tx.service.message.grpc.ReSendAllDeadRequest request,
        io.grpc.stub.StreamObserver<org.tus.tx.service.message.grpc.ReSendAllDeadResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getReSendAllDeadMessageByQueueNameMethod(), responseObserver);
    }

    /**
     */
    default void listPage(org.tus.tx.service.message.grpc.ListPageRequest request,
        io.grpc.stub.StreamObserver<org.tus.tx.service.message.grpc.ListPageResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getListPageMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service TransactionMessageService.
   */
  public static abstract class TransactionMessageServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return TransactionMessageServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service TransactionMessageService.
   */
  public static final class TransactionMessageServiceStub
      extends io.grpc.stub.AbstractAsyncStub<TransactionMessageServiceStub> {
    private TransactionMessageServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected TransactionMessageServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new TransactionMessageServiceStub(channel, callOptions);
    }

    /**
     */
    public void saveMessageWaitingConfirm(org.tus.tx.service.message.grpc.SaveMessageWaitingConfirmRequest request,
        io.grpc.stub.StreamObserver<org.tus.tx.service.message.grpc.SaveMessageWaitingConfirmResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getSaveMessageWaitingConfirmMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void confirmAndSendMessage(org.tus.tx.service.message.grpc.ConfirmAndSendRequest request,
        io.grpc.stub.StreamObserver<org.tus.tx.service.message.grpc.ConfirmAndSendResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getConfirmAndSendMessageMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void saveAndSendMessage(org.tus.tx.service.message.grpc.SaveAndSendRequest request,
        io.grpc.stub.StreamObserver<org.tus.tx.service.message.grpc.SaveAndSendResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getSaveAndSendMessageMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void directSendMessage(org.tus.tx.service.message.grpc.DirectSendRequest request,
        io.grpc.stub.StreamObserver<org.tus.tx.service.message.grpc.DirectSendResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getDirectSendMessageMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void reSendMessage(org.tus.tx.service.message.grpc.ReSendMessageRequest request,
        io.grpc.stub.StreamObserver<org.tus.tx.service.message.grpc.ReSendMessageResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getReSendMessageMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void reSendMessageByMessageId(org.tus.tx.service.message.grpc.ReSendByMessageIdRequest request,
        io.grpc.stub.StreamObserver<org.tus.tx.service.message.grpc.ReSendByMessageIdResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getReSendMessageByMessageIdMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void setMessageToAlreadyDead(org.tus.tx.service.message.grpc.SetMessageToAlreadyDeadRequest request,
        io.grpc.stub.StreamObserver<org.tus.tx.service.message.grpc.SetMessageToAlreadyDeadResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getSetMessageToAlreadyDeadMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void deleteMessageByMessageId(org.tus.tx.service.message.grpc.DeleteMessageRequest request,
        io.grpc.stub.StreamObserver<org.tus.tx.service.message.grpc.DeleteMessageResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getDeleteMessageByMessageIdMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void reSendAllDeadMessageByQueueName(org.tus.tx.service.message.grpc.ReSendAllDeadRequest request,
        io.grpc.stub.StreamObserver<org.tus.tx.service.message.grpc.ReSendAllDeadResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getReSendAllDeadMessageByQueueNameMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void listPage(org.tus.tx.service.message.grpc.ListPageRequest request,
        io.grpc.stub.StreamObserver<org.tus.tx.service.message.grpc.ListPageResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getListPageMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service TransactionMessageService.
   */
  public static final class TransactionMessageServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<TransactionMessageServiceBlockingStub> {
    private TransactionMessageServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected TransactionMessageServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new TransactionMessageServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public org.tus.tx.service.message.grpc.SaveMessageWaitingConfirmResponse saveMessageWaitingConfirm(org.tus.tx.service.message.grpc.SaveMessageWaitingConfirmRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getSaveMessageWaitingConfirmMethod(), getCallOptions(), request);
    }

    /**
     */
    public org.tus.tx.service.message.grpc.ConfirmAndSendResponse confirmAndSendMessage(org.tus.tx.service.message.grpc.ConfirmAndSendRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getConfirmAndSendMessageMethod(), getCallOptions(), request);
    }

    /**
     */
    public org.tus.tx.service.message.grpc.SaveAndSendResponse saveAndSendMessage(org.tus.tx.service.message.grpc.SaveAndSendRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getSaveAndSendMessageMethod(), getCallOptions(), request);
    }

    /**
     */
    public org.tus.tx.service.message.grpc.DirectSendResponse directSendMessage(org.tus.tx.service.message.grpc.DirectSendRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getDirectSendMessageMethod(), getCallOptions(), request);
    }

    /**
     */
    public org.tus.tx.service.message.grpc.ReSendMessageResponse reSendMessage(org.tus.tx.service.message.grpc.ReSendMessageRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getReSendMessageMethod(), getCallOptions(), request);
    }

    /**
     */
    public org.tus.tx.service.message.grpc.ReSendByMessageIdResponse reSendMessageByMessageId(org.tus.tx.service.message.grpc.ReSendByMessageIdRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getReSendMessageByMessageIdMethod(), getCallOptions(), request);
    }

    /**
     */
    public org.tus.tx.service.message.grpc.SetMessageToAlreadyDeadResponse setMessageToAlreadyDead(org.tus.tx.service.message.grpc.SetMessageToAlreadyDeadRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getSetMessageToAlreadyDeadMethod(), getCallOptions(), request);
    }

    /**
     */
    public org.tus.tx.service.message.grpc.DeleteMessageResponse deleteMessageByMessageId(org.tus.tx.service.message.grpc.DeleteMessageRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getDeleteMessageByMessageIdMethod(), getCallOptions(), request);
    }

    /**
     */
    public org.tus.tx.service.message.grpc.ReSendAllDeadResponse reSendAllDeadMessageByQueueName(org.tus.tx.service.message.grpc.ReSendAllDeadRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getReSendAllDeadMessageByQueueNameMethod(), getCallOptions(), request);
    }

    /**
     */
    public org.tus.tx.service.message.grpc.ListPageResponse listPage(org.tus.tx.service.message.grpc.ListPageRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getListPageMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service TransactionMessageService.
   */
  public static final class TransactionMessageServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<TransactionMessageServiceFutureStub> {
    private TransactionMessageServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected TransactionMessageServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new TransactionMessageServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<org.tus.tx.service.message.grpc.SaveMessageWaitingConfirmResponse> saveMessageWaitingConfirm(
        org.tus.tx.service.message.grpc.SaveMessageWaitingConfirmRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getSaveMessageWaitingConfirmMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<org.tus.tx.service.message.grpc.ConfirmAndSendResponse> confirmAndSendMessage(
        org.tus.tx.service.message.grpc.ConfirmAndSendRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getConfirmAndSendMessageMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<org.tus.tx.service.message.grpc.SaveAndSendResponse> saveAndSendMessage(
        org.tus.tx.service.message.grpc.SaveAndSendRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getSaveAndSendMessageMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<org.tus.tx.service.message.grpc.DirectSendResponse> directSendMessage(
        org.tus.tx.service.message.grpc.DirectSendRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getDirectSendMessageMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<org.tus.tx.service.message.grpc.ReSendMessageResponse> reSendMessage(
        org.tus.tx.service.message.grpc.ReSendMessageRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getReSendMessageMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<org.tus.tx.service.message.grpc.ReSendByMessageIdResponse> reSendMessageByMessageId(
        org.tus.tx.service.message.grpc.ReSendByMessageIdRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getReSendMessageByMessageIdMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<org.tus.tx.service.message.grpc.SetMessageToAlreadyDeadResponse> setMessageToAlreadyDead(
        org.tus.tx.service.message.grpc.SetMessageToAlreadyDeadRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getSetMessageToAlreadyDeadMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<org.tus.tx.service.message.grpc.DeleteMessageResponse> deleteMessageByMessageId(
        org.tus.tx.service.message.grpc.DeleteMessageRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getDeleteMessageByMessageIdMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<org.tus.tx.service.message.grpc.ReSendAllDeadResponse> reSendAllDeadMessageByQueueName(
        org.tus.tx.service.message.grpc.ReSendAllDeadRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getReSendAllDeadMessageByQueueNameMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<org.tus.tx.service.message.grpc.ListPageResponse> listPage(
        org.tus.tx.service.message.grpc.ListPageRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getListPageMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_SAVE_MESSAGE_WAITING_CONFIRM = 0;
  private static final int METHODID_CONFIRM_AND_SEND_MESSAGE = 1;
  private static final int METHODID_SAVE_AND_SEND_MESSAGE = 2;
  private static final int METHODID_DIRECT_SEND_MESSAGE = 3;
  private static final int METHODID_RE_SEND_MESSAGE = 4;
  private static final int METHODID_RE_SEND_MESSAGE_BY_MESSAGE_ID = 5;
  private static final int METHODID_SET_MESSAGE_TO_ALREADY_DEAD = 6;
  private static final int METHODID_DELETE_MESSAGE_BY_MESSAGE_ID = 7;
  private static final int METHODID_RE_SEND_ALL_DEAD_MESSAGE_BY_QUEUE_NAME = 8;
  private static final int METHODID_LIST_PAGE = 9;

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
        case METHODID_SAVE_MESSAGE_WAITING_CONFIRM:
          serviceImpl.saveMessageWaitingConfirm((org.tus.tx.service.message.grpc.SaveMessageWaitingConfirmRequest) request,
              (io.grpc.stub.StreamObserver<org.tus.tx.service.message.grpc.SaveMessageWaitingConfirmResponse>) responseObserver);
          break;
        case METHODID_CONFIRM_AND_SEND_MESSAGE:
          serviceImpl.confirmAndSendMessage((org.tus.tx.service.message.grpc.ConfirmAndSendRequest) request,
              (io.grpc.stub.StreamObserver<org.tus.tx.service.message.grpc.ConfirmAndSendResponse>) responseObserver);
          break;
        case METHODID_SAVE_AND_SEND_MESSAGE:
          serviceImpl.saveAndSendMessage((org.tus.tx.service.message.grpc.SaveAndSendRequest) request,
              (io.grpc.stub.StreamObserver<org.tus.tx.service.message.grpc.SaveAndSendResponse>) responseObserver);
          break;
        case METHODID_DIRECT_SEND_MESSAGE:
          serviceImpl.directSendMessage((org.tus.tx.service.message.grpc.DirectSendRequest) request,
              (io.grpc.stub.StreamObserver<org.tus.tx.service.message.grpc.DirectSendResponse>) responseObserver);
          break;
        case METHODID_RE_SEND_MESSAGE:
          serviceImpl.reSendMessage((org.tus.tx.service.message.grpc.ReSendMessageRequest) request,
              (io.grpc.stub.StreamObserver<org.tus.tx.service.message.grpc.ReSendMessageResponse>) responseObserver);
          break;
        case METHODID_RE_SEND_MESSAGE_BY_MESSAGE_ID:
          serviceImpl.reSendMessageByMessageId((org.tus.tx.service.message.grpc.ReSendByMessageIdRequest) request,
              (io.grpc.stub.StreamObserver<org.tus.tx.service.message.grpc.ReSendByMessageIdResponse>) responseObserver);
          break;
        case METHODID_SET_MESSAGE_TO_ALREADY_DEAD:
          serviceImpl.setMessageToAlreadyDead((org.tus.tx.service.message.grpc.SetMessageToAlreadyDeadRequest) request,
              (io.grpc.stub.StreamObserver<org.tus.tx.service.message.grpc.SetMessageToAlreadyDeadResponse>) responseObserver);
          break;
        case METHODID_DELETE_MESSAGE_BY_MESSAGE_ID:
          serviceImpl.deleteMessageByMessageId((org.tus.tx.service.message.grpc.DeleteMessageRequest) request,
              (io.grpc.stub.StreamObserver<org.tus.tx.service.message.grpc.DeleteMessageResponse>) responseObserver);
          break;
        case METHODID_RE_SEND_ALL_DEAD_MESSAGE_BY_QUEUE_NAME:
          serviceImpl.reSendAllDeadMessageByQueueName((org.tus.tx.service.message.grpc.ReSendAllDeadRequest) request,
              (io.grpc.stub.StreamObserver<org.tus.tx.service.message.grpc.ReSendAllDeadResponse>) responseObserver);
          break;
        case METHODID_LIST_PAGE:
          serviceImpl.listPage((org.tus.tx.service.message.grpc.ListPageRequest) request,
              (io.grpc.stub.StreamObserver<org.tus.tx.service.message.grpc.ListPageResponse>) responseObserver);
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
          getSaveMessageWaitingConfirmMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              org.tus.tx.service.message.grpc.SaveMessageWaitingConfirmRequest,
              org.tus.tx.service.message.grpc.SaveMessageWaitingConfirmResponse>(
                service, METHODID_SAVE_MESSAGE_WAITING_CONFIRM)))
        .addMethod(
          getConfirmAndSendMessageMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              org.tus.tx.service.message.grpc.ConfirmAndSendRequest,
              org.tus.tx.service.message.grpc.ConfirmAndSendResponse>(
                service, METHODID_CONFIRM_AND_SEND_MESSAGE)))
        .addMethod(
          getSaveAndSendMessageMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              org.tus.tx.service.message.grpc.SaveAndSendRequest,
              org.tus.tx.service.message.grpc.SaveAndSendResponse>(
                service, METHODID_SAVE_AND_SEND_MESSAGE)))
        .addMethod(
          getDirectSendMessageMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              org.tus.tx.service.message.grpc.DirectSendRequest,
              org.tus.tx.service.message.grpc.DirectSendResponse>(
                service, METHODID_DIRECT_SEND_MESSAGE)))
        .addMethod(
          getReSendMessageMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              org.tus.tx.service.message.grpc.ReSendMessageRequest,
              org.tus.tx.service.message.grpc.ReSendMessageResponse>(
                service, METHODID_RE_SEND_MESSAGE)))
        .addMethod(
          getReSendMessageByMessageIdMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              org.tus.tx.service.message.grpc.ReSendByMessageIdRequest,
              org.tus.tx.service.message.grpc.ReSendByMessageIdResponse>(
                service, METHODID_RE_SEND_MESSAGE_BY_MESSAGE_ID)))
        .addMethod(
          getSetMessageToAlreadyDeadMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              org.tus.tx.service.message.grpc.SetMessageToAlreadyDeadRequest,
              org.tus.tx.service.message.grpc.SetMessageToAlreadyDeadResponse>(
                service, METHODID_SET_MESSAGE_TO_ALREADY_DEAD)))
        .addMethod(
          getDeleteMessageByMessageIdMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              org.tus.tx.service.message.grpc.DeleteMessageRequest,
              org.tus.tx.service.message.grpc.DeleteMessageResponse>(
                service, METHODID_DELETE_MESSAGE_BY_MESSAGE_ID)))
        .addMethod(
          getReSendAllDeadMessageByQueueNameMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              org.tus.tx.service.message.grpc.ReSendAllDeadRequest,
              org.tus.tx.service.message.grpc.ReSendAllDeadResponse>(
                service, METHODID_RE_SEND_ALL_DEAD_MESSAGE_BY_QUEUE_NAME)))
        .addMethod(
          getListPageMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              org.tus.tx.service.message.grpc.ListPageRequest,
              org.tus.tx.service.message.grpc.ListPageResponse>(
                service, METHODID_LIST_PAGE)))
        .build();
  }

  private static abstract class TransactionMessageServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    TransactionMessageServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return org.tus.tx.service.message.grpc.TransactionMessage.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("TransactionMessageService");
    }
  }

  private static final class TransactionMessageServiceFileDescriptorSupplier
      extends TransactionMessageServiceBaseDescriptorSupplier {
    TransactionMessageServiceFileDescriptorSupplier() {}
  }

  private static final class TransactionMessageServiceMethodDescriptorSupplier
      extends TransactionMessageServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    TransactionMessageServiceMethodDescriptorSupplier(java.lang.String methodName) {
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
      synchronized (TransactionMessageServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new TransactionMessageServiceFileDescriptorSupplier())
              .addMethod(getSaveMessageWaitingConfirmMethod())
              .addMethod(getConfirmAndSendMessageMethod())
              .addMethod(getSaveAndSendMessageMethod())
              .addMethod(getDirectSendMessageMethod())
              .addMethod(getReSendMessageMethod())
              .addMethod(getReSendMessageByMessageIdMethod())
              .addMethod(getSetMessageToAlreadyDeadMethod())
              .addMethod(getDeleteMessageByMessageIdMethod())
              .addMethod(getReSendAllDeadMessageByQueueNameMethod())
              .addMethod(getListPageMethod())
              .build();
        }
      }
    }
    return result;
  }
}
