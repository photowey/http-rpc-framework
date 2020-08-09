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
package com.photowey.http.rpc.core.util;

import com.photowey.http.rpc.core.enums.TargetEnum;
import org.springframework.util.Assert;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

/**
 * HRpcUtils
 *
 * @author WcJun
 * @date 2020/08/08
 * @since 1.0.0
 */
public class HRpcUtils {

    private HRpcUtils() {
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    public static String uuid() {
        return UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
    }

    public static <T> boolean isEmpty(T target) {
        if (target instanceof CharSequence) {
            return null == target || "".equals(target);
        } else {
            return null == target;
        }
    }

    public static <T> boolean isNotEmpty(T target) {
        return !isEmpty(target);
    }

    public static <T> boolean isEmpty(T[] target) {
        return null == target || 0 == target.length;
    }

    public static <T> boolean isNotEmpty(T[] target) {
        return !isEmpty(target);
    }

    public static <T> boolean isEmpty(List<T> target) {
        return null == target || 0 == target.size();
    }

    public static <T> boolean isNotEmpty(List<T> target) {
        return !isEmpty(target);
    }

    // ============================================= Request

    public static List<Annotation> toTarget(Set<Annotation> annotations, final Class<? extends Annotation> target) {

        List<Annotation> newList = new ArrayList<>();

        for (Annotation annotationSet : annotations) {
            Class<? extends Annotation> annotationType = annotationSet.annotationType();
            if (annotationType.equals(target)) {
                newList.add(annotationSet);
            }
        }

        return newList;
    }

    public static String extraction(String param, TargetEnum target) {
        // 非数值
        String regex = "[^0-9]";
        if (TargetEnum.ALPHABET.equals(target)) {
            // 非字母
            regex = "[^A-Z a-z]";
        }

        Pattern pattern = compile(regex);
        Matcher matcher = pattern.matcher(param);
        // 获取目标参数 - 即将非目标字符替换为空串
        String targetParam = matcher.replaceAll("").trim();

        return targetParam;
    }

    public static String extractBodyClazz(String typeName) {
        Pattern pattern = compile("<.*>");
        Matcher m = pattern.matcher(typeName);
        String type = "";
        if (m.find()) {
            type = m.group(0);
        }
        String clazz = type.substring(1);
        return clazz.substring(0, clazz.length() - 1);
    }

    public static void populateQuery(StringBuilder builder, String alias, String value) {
        String url = builder.toString();
        if (!url.contains("?")) {
            builder.append("?");
        } else {
            builder.append("&");
        }

        builder.append(alias).append("=").append(value);
    }

    // ============================================= LowerCase

    public String toLowerCase(String target, int index) {
        Assert.hasText(target, "the target Str content can't be blank");
        Assert.isTrue(index > 0, "the target index must be > 0");
        if (index > target.length()) {
            index = target.length();
        }

        return target.substring(0, index).toLowerCase() + target.substring(index);
    }

    // ============================================= List

    public static <T> List<T> toList(Iterator<T> iterator) {
        List<T> target = new ArrayList<>();
        while (iterator.hasNext()) {
            target.add(iterator.next());
        }

        return target;
    }

    // ============================================= Spring

    public static String generateBeanName(String className, String candidate) {
        if (isNotEmpty(candidate)) {
            return candidate;
        }
        return className.substring(0, 1).toLowerCase() + className.substring(1);
    }

    public static String convertClassNameToResourcePath(String className) {
        return className.replace(PACKAGE_SEPARATOR, PATH_SEPARATOR);
    }

    public static String resolveBasePackage(String basePackage) {
        return convertClassNameToResourcePath(basePackage);
    }

    public static final String DEFAULT_RESOURCE_PATTERN = "**/*.class";
    /**
     * The package separator character: {@code '.'}.
     */
    public static final char PACKAGE_SEPARATOR = '.';

    /**
     * The path separator character: {@code '/'}.
     */
    public static final char PATH_SEPARATOR = '/';

}
