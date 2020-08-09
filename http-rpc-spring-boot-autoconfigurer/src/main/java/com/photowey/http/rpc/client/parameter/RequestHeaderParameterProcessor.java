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
import com.photowey.http.rpc.core.enums.TargetEnum;
import com.photowey.http.rpc.core.util.HRpcConstants;
import com.photowey.http.rpc.core.util.HRpcUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestHeader;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.util.*;

/**
 * {@literal @}PathVariable Annotation ParameterProcessor
 *
 * @author WcJun
 * @date 2020/08/08
 * @see org.springframework.web.bind.annotation.RequestHeader
 * @since 1.0.0
 */
@ParameterProcessorMarker
public class RequestHeaderParameterProcessor implements ParameterProcessor {

    private static final Class<RequestHeader> ANNOTATION = RequestHeader.class;

    @Override
    public int getOrder() {
        return 2;
    }

    @Override
    public void handleParameter(Parameter parameter, Integer paramIndex, MethodSignature methodSignature) {
        this.handleParameter(parameter, paramIndex, methodSignature, ANNOTATION);
    }

    private <T extends Annotation> void handleParameter(Parameter parameter, Integer paramIndex, MethodSignature methodSignature, Class<T> clazz) {
        Annotation[] annotations = parameter.getAnnotations();
        Set<Annotation> annotationSets = new HashSet<>(Arrays.asList(annotations));
        List<Annotation> targets = HRpcUtils.toTarget(annotationSets, clazz);
        if (!CollectionUtils.isEmpty(targets)) {
            for (Annotation annotation : targets) {
                String alias = ANNOTATION.cast(annotation).value();
                String name = parameter.getName();
                // Use parameter name as alisa
                if (null == alias || "".equals(alias.trim())) {
                    alias = name;
                    methodSignature.getNoAlisaHeaderIndex().put(alias, paramIndex);
                } else {
                    if (name.contains(HRpcConstants.COMPILE_ARG_PREFIX) && null != HRpcUtils.extraction(name, TargetEnum.NUMBER)) {
                        // arg + index
                        this.handleAlisa(paramIndex, methodSignature, alias, false);
                    } else {
                        this.handleAlisa(paramIndex, methodSignature, alias, true);
                    }
                }
            }
        }
    }

    private void handleAlisa(Integer paramIndex, MethodSignature methodSignature, String alias, boolean checkAlisa) {
        if (checkAlisa) {
            Map<String, String> aliasMap = methodSignature.getAliasMap();
            String aliasCache = aliasMap.get(alias);
            if (null != aliasCache) {
                throw new RuntimeException("the request header index:[" + paramIndex + "]'s  param haded used!");
            }
            aliasMap.put(alias, alias);
        }
        methodSignature.getHeaders().put(alias, paramIndex);
    }
}
