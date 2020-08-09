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
package com.photowey.http.rpc.client.parameter;

import com.photowey.http.rpc.client.binding.MethodSignature;
import org.springframework.core.Ordered;

import java.lang.reflect.Parameter;

/**
 * ParameterProcessor
 *
 * @author WcJun
 * @date 2020/08/08
 * @since 1.0.0
 */
public interface ParameterProcessor extends Ordered {

    void handleParameter(Parameter parameter, Integer paramIndex, MethodSignature methodSignature);
}
