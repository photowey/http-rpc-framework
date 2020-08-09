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
import com.photowey.http.rpc.client.binding.MethodSignature;
import com.photowey.http.rpc.core.enums.TargetEnum;
import com.photowey.http.rpc.core.exception.HRpcException;
import com.photowey.http.rpc.core.util.HRpcConstants;
import com.photowey.http.rpc.core.util.HRpcUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.util.*;

/**
 * {@literal @}PathVariable Annotation ParameterProcessor
 *
 * @author WcJun
 * @date 2020/08/08
 * @see org.springframework.web.bind.annotation.PathVariable
 * @since 1.0.0
 */
@ParameterProcessorMarker
public class PathVariableParameterProcessor implements ParameterProcessor {

    private static final Class<PathVariable> ANNOTATION = PathVariable.class;

    private static final Logger log = LoggerFactory.getLogger(PathVariableParameterProcessor.class);

    @Override
    public int getOrder() {
        return 1;
    }

    @Override
    public void handleParameter(Parameter parameter, Integer paramIndex, MethodSignature methodSignature) {
        this.handleParameter(parameter, paramIndex, methodSignature, ANNOTATION);
    }

    private <T extends Annotation> void handleParameter(Parameter parameter, Integer paramIndex, MethodSignature methodSignature, Class<T> clazz) {
        Annotation[] annotations = parameter.getAnnotations();

        Set<Annotation> annotationSets = new HashSet<>(Arrays.asList(annotations));
        List<Annotation> targets = HRpcUtils.toTarget(annotationSets, clazz);
        log.info("the path annotation order:[{}], size is:[{}]", this.getOrder(), targets.size());
        for (Annotation annotation : targets) {
            String alias = ANNOTATION.cast(annotation).value();
            String name = parameter.getName();
            // Use parameter name as alisa
            if (null == alias || "".equals(alias.trim())) {
                alias = name;
                methodSignature.getNoAlisaPathIndex().put(alias, paramIndex);
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

    private void handleAlisa(Integer paramIndex, MethodSignature methodSignature, String alias, boolean checkAlisa) {
        if (checkAlisa) {
            Map<String, String> aliasMap = methodSignature.getAliasMap();
            String aliasCache = aliasMap.get(alias);
            if (null != aliasCache) {
                throw new HRpcException("the path index:" + alias + "[" + paramIndex + "]'s param haded used!");
            }
            aliasMap.put(alias, alias);
        }
        methodSignature.getPathVariable().put(alias, paramIndex);
    }
}
