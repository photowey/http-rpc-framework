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
package com.photowey.http.rpc.core.model;

import com.photowey.http.rpc.core.constant.HRpcConstants;
import com.photowey.http.rpc.core.util.StringFormatUtils;

import java.io.Serializable;

/**
 * The Remote info
 *
 * @author WcJun
 * @date 2020/08/08
 * @since 1.0.0
 */
public class RemoteInfo implements Serializable {
    private String protocol;
    private String host;
    private String uri;

    public RemoteInfo() {
    }

    public RemoteInfo(String protocol, String host, String uri) {
        this.protocol = protocol;
        this.host = host;
        this.uri = uri;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    /**
     * Build URI
     *
     * @return the full URI
     */
    public String buildURI() {
        return StringFormatUtils.format(HRpcConstants.TEMPLATE_URI, this.getProtocol(), this.getHost(), "");
    }
}
