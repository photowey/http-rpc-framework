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
import com.photowey.http.rpc.core.util.HRpcUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestParam;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.util.*;

/**
 * {@literal @}RequestParam Annotation ParameterProcessor
 *
 * @author WcJun
 * @date 2020/08/08
 * @see org.springframework.web.bind.annotation.RequestParam
 * @since 1.0.0
 */
@ParameterProcessorMarker
public class RequestParamParameterProcessor implements ParameterProcessor {

    @Override
    public int getOrder() {
        return 0;
    }

    private static final Class<RequestParam> ANNOTATION = RequestParam.class;

    private static final Logger log = LoggerFactory.getLogger(RequestParamParameterProcessor.class);

    @Override
    public void handleParameter(Parameter parameter, Integer paramIndex, MethodSignature methodSignature) {
        this.handleParameter(parameter, paramIndex, methodSignature, ANNOTATION);
    }

    private <T extends Annotation> void handleParameter(Parameter parameter, Integer paramIndex, MethodSignature methodSignature, Class<T> clazz) {
        Annotation[] annotations = parameter.getAnnotations();
        Set<Annotation> annotationSets = new HashSet<>(Arrays.asList(annotations));
        List<Annotation> targets = HRpcUtils.toTarget(annotationSets, clazz);
        if (log.isDebugEnabled()) {
            log.info("the request params annotation order:[{}], size is:[{}]", this.getOrder(), targets.size());
        }
        // handle @RequestParam
        if (!CollectionUtils.isEmpty(targets)) {
            for (Annotation annotation : targets) {
                String alias = ANNOTATION.cast(annotation).value();
                String name = parameter.getName();
                Class<?> type = parameter.getType();
                if (null == alias || "".equals(alias.trim())) {
                    // check @RequestParam's value -> use parameter name as alisa if necessary
                    // check Map.class
                    if (type.equals(Map.class)) {
                        methodSignature.getQueryMapIndex().add(paramIndex);
                        if (log.isDebugEnabled()) {
                            log.info("the request param type is MAP the index:[{}]", paramIndex);
                        }
                    } else {
                        alias = name;
                        methodSignature.getNoAliasParamIndex().put(alias, paramIndex);
                    }
                } else {
                    // arg + index
                    if (name.contains("arg") && null != HRpcUtils.extraction(name, TargetEnum.NUMBER)) {
                        // arg + index
                        // handle alisa and parameter mapping
                        this.handleAlias(paramIndex, methodSignature, alias, false);
                    } else {
                        this.handleAlias(paramIndex, methodSignature, alias, true);
                    }
                }
            }
        }
    }

    private void handleAlias(Integer paramIndex, MethodSignature methodSignature, String alias, boolean checkAlisa) {
        if (checkAlisa) {
            // Alisa cache -> @RequestParam @PathVariable @RequestHeader
            // Only check if the alias is set
            Map<String, String> aliasMap = methodSignature.getAliasMap();
            String aliasCache = aliasMap.get(alias);
            if (null != aliasCache) {
                throw new HRpcException("the request-param index:[{}]'s param haded used!", paramIndex);
            }
            aliasMap.put(alias, alias);
        } else {
            // Use the parameter name as an alias without verification
        }

        Map<String, Integer> queries = methodSignature.getQueries();
        queries.put(alias, paramIndex);
        String url = methodSignature.getUrl();
        StringBuilder builder = this.populateQuery(alias, alias, url);

        methodSignature.setUrl(builder.toString());
    }

    private StringBuilder populateQuery(String alias, String value, String url) {
        StringBuilder builder = new StringBuilder(url);
        // url = http://localhost:8080/api/v1/hello/world
        if (!url.contains("?")) {
            // url = http://localhost:8080/api/v1/hello/world ?
            builder.append("?");
        } else {
            // url = http://localhost:8080/api/v1/hello/world ? a = b &
            builder.append("&");
        }

        // http://localhost:8080/api/v1/hello/world ? a = b & c = { alias }
        builder.append(alias).append("=").append("{").append(value).append("}");
        return builder;
    }
}
