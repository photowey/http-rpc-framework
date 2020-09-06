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
package com.photowey.http.rpc.core.enums;

/**
 * retrieve the host info type
 *
 * @author WcJun
 * @date 2020/09/06
 * @since 1.1.0
 */
public enum HostTypeEnum {
    /**
     * use the static type, read info from the annotations
     * {@code @HttpXxx.host()}
     *
     * @since 1.1.0
     */
    STATIC,
    /**
     * use the dynamic type,read info from the config info
     *
     * @see * com.sun.xml.internal.ws.transport.http.client.HttpResponseProperties
     * @since 1.1.0
     */
    DYNAMIC
}
