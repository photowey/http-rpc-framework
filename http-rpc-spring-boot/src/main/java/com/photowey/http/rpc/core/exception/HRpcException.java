/*
 * Copyright © 2020 photowey (photowey@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.photowey.http.rpc.core.exception;

import com.photowey.http.rpc.core.enums.HRpcStatus;
import com.photowey.http.rpc.core.util.StringFormatUtils;

/**
 * HRpcException
 *
 * @author WcJun
 * @date 2020/08/08
 * @since 1.0.0
 */
public class HRpcException extends RuntimeException {

    public int status;

    private static final long serialVersionUID = -2267091897489169518L;

    public HRpcException() {
        super();
        this.status = HRpcStatus.FAILURE.toValue();
    }

    public HRpcException(String message) {
        super(message);
        this.status = HRpcStatus.FAILURE.toValue();
    }

    public HRpcException(String message, Object... params) {
        super(StringFormatUtils.format(message, params));
        this.status = HRpcStatus.FAILURE.toValue();
    }

    public HRpcException(int status, String message) {
        super(message);
        this.status = status;
    }

    public HRpcException(String message, Throwable cause) {
        super(message, cause);
        this.status = HRpcStatus.FAILURE.toValue();
    }

    public HRpcException(String message, Throwable cause, Object... params) {
        super(StringFormatUtils.format(message, params), cause);
        this.status = HRpcStatus.FAILURE.toValue();
    }

    public HRpcException(int status, String message, Throwable cause) {
        super(message, cause);
        this.status = status;
    }

    public HRpcException(int status, String message, Throwable cause, Object... params) {
        super(StringFormatUtils.format(message, params), cause);
        this.status = status;
    }

    // =============================================================================

    public HRpcException(Throwable cause) {
        super(cause);
        this.status = HRpcStatus.FAILURE.toValue();
    }

    public HRpcException(int status, Throwable cause) {
        super(cause);
        this.status = status;
    }

    public HRpcException(String message, Throwable cause,
                         boolean enableSuppression,
                         boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.status = HRpcStatus.FAILURE.toValue();
    }

    public HRpcException(String message, Throwable cause,
                         boolean enableSuppression,
                         boolean writableStackTrace, Object... params) {
        super(StringFormatUtils.format(message, params), cause, enableSuppression, writableStackTrace);
        this.status = HRpcStatus.FAILURE.toValue();
    }

    public HRpcException(int status, String message, Throwable cause,
                         boolean enableSuppression,
                         boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.status = status;
    }

    public HRpcException(int status, String message, Throwable cause,
                         boolean enableSuppression,
                         boolean writableStackTrace, Object... params) {
        super(StringFormatUtils.format(message, params), cause, enableSuppression, writableStackTrace);
        this.status = status;
    }
}
