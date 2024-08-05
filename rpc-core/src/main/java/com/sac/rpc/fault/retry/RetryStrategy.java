package com.sac.rpc.fault.retry;

import com.sac.rpc.model.RpcRequest;
import com.sac.rpc.model.RpcResponse;

import java.util.concurrent.Callable;

public interface RetryStrategy {

    RpcResponse doRetry(Callable<RpcResponse> callable) throws Exception;
}
