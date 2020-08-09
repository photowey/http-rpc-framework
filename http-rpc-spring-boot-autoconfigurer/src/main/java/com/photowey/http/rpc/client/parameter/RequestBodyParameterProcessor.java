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

import com.photowey.http.rpc.client.annotation.ParameterProcessorMarker;
import com.photowey.http.rpc.client.binding.ClientMethod;
import com.photowey.http.rpc.client.binding.MethodSignature;
import com.photowey.http.rpc.core.util.HRpcUtils;
import org.springframework.web.bind.annotation.RequestBody;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * {@literal @}RequestBody Annotation ParameterProcessor
 *
 * @author WcJun
 * @date 2020/08/08
 * @see org.springframework.web.bind.annotation.RequestBody
 * @since 1.0.0
 */
@ParameterProcessorMarker
public class RequestBodyParameterProcessor implements ParameterProcessor {

    private static final Class<RequestBody> ANNOTATION = RequestBody.class;

    @Override
    public int getOrder() {
        return 3;
    }

    @Override
    public void handleParameter(Parameter parameter, Integer paramIndex, MethodSignature methodSignature) {
        this.handleParameter(parameter, paramIndex, methodSignature, ANNOTATION);
    }

    private <T extends Annotation> void handleParameter(Parameter parameter, Integer paramIndex, MethodSignature methodSignature, Class<T> clazz) {
        Annotation[] annotations = parameter.getAnnotations();
        Set<Annotation> annotationSets = new HashSet<>(Arrays.asList(annotations));
        List<Annotation> targets = HRpcUtils.toTarget(annotationSets, clazz);
        if (HRpcUtils.isNotEmpty(targets)) {
            Class<?> requestBody = parameter.getType();
            methodSignature.setRequestBodyType(requestBody);
            methodSignature.setBodyIndex(paramIndex);
        }
    }
}
