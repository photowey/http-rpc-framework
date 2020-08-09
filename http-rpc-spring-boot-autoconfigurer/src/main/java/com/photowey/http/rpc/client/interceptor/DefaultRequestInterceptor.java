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
package com.photowey.http.rpc.client.interceptor;

import com.photowey.http.rpc.client.context.RequestContext;
import org.springframework.core.Ordered;

/**
 * RequestInterceptor Root-Interface
 *
 * @author WcJun
 * @date 2020/08/08
 * @since 1.0.0
 */
public class DefaultRequestInterceptor implements RequestInterceptor {

    @Override
    public int getOrder() {
        return Integer.MAX_VALUE;
    }

    @Override
    public void intercept(RequestContext context) {

    }
}
