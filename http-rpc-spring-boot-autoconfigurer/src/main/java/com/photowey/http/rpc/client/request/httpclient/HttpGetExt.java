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
package com.photowey.http.rpc.client.request.httpclient;

import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;

import java.net.URI;

/**
 * HttpGet Ext
 * This Ext is for HttpGet as The HttpPort
 *
 * @author WcJun
 * @date 2020/08/22
 * @since 1.0.0
 */
public class HttpGetExt extends HttpEntityEnclosingRequestBase {
    public static final String METHOD_NAME = "GET";

    public HttpGetExt() {
    }

    public HttpGetExt(URI uri) {
        this.setURI(uri);
    }

    public HttpGetExt(String uri) {
        this.setURI(URI.create(uri));
    }

    public String getMethod() {
        return "GET";
    }
}
