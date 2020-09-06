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
package com.photowey.http.rpc.client.parser;

import com.photowey.http.rpc.client.annotation.AnnotationParserMarker;
import com.photowey.http.rpc.client.properties.HRpcClientProperties;
import com.photowey.http.rpc.core.annotation.HttpPost;
import com.photowey.http.rpc.core.enums.HostTypeEnum;
import com.photowey.http.rpc.core.model.RemoteInfo;
import org.springframework.context.ApplicationContextAware;

import java.lang.reflect.Method;

/**
 * The HttpPost AnnotationParser
 *
 * @author WcJun
 * @date 2020/08/08
 * @since 1.0.0
 */
@AnnotationParserMarker
public class HttpPostAnnotationParser extends AbstractAnnotationParser implements AnnotationParser<HttpPost>, ApplicationContextAware {

    @Override
    public boolean supports(Method target) {
        return target.isAnnotationPresent(HttpPost.class);
    }

    @Override
    @Deprecated
    public RemoteInfo parse(HttpPost httpPost) {
        String protocol = httpPost.protocol();
        String host = httpPost.host();
        String uri = httpPost.uri();
        uri = uri.replaceAll("^/*", "");

        return new RemoteInfo(protocol, host, uri);
    }

    @Override
    public RemoteInfo parse(HttpPost httpPost, Method target, HRpcClientProperties properties) {
        String protocol = httpPost.protocol();
        String host = httpPost.host();
        HostTypeEnum hostType = httpPost.hostType();
        host = this.determineHost(host, hostType, target, properties);
        String uri = httpPost.uri();
        uri = uri.replaceAll("^/*", "");

        return new RemoteInfo(protocol, host, uri);
    }
}
