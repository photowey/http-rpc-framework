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
import com.photowey.http.rpc.core.annotation.HttpDelete;
import com.photowey.http.rpc.core.annotation.HttpGet;
import com.photowey.http.rpc.core.annotation.HttpPatch;
import com.photowey.http.rpc.core.model.RemoteInfo;

import java.lang.reflect.Method;

/**
 * The HttpPatch AnnotationParser
 *
 * @author WcJun
 * @date 2020/08/08
 * @since 1.0.0
 */
@AnnotationParserMarker
public class HttpPatchAnnotationParser implements AnnotationParser<HttpPatch> {

    @Override
    public boolean supports(Method target) {
        return target.isAnnotationPresent(HttpPatch.class);
    }

    @Override
    public RemoteInfo parse(HttpPatch httpPatch) {
        String protocol = httpPatch.protocol();
        String host = httpPatch.host();
        String uri = httpPatch.uri();
        uri = uri.replaceAll("^/*", "");

        return new RemoteInfo(protocol, host, uri);
    }
}
