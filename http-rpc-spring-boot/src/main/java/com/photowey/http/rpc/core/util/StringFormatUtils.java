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

/**
 * The String Format Utils
 * Use String.format
 *
 * @author WcJun
 * @date 2020/08/08
 * @since 1.0.0
 */
public final class StringFormatUtils {

    private StringFormatUtils() {
        throw new AssertionError("No " + this.getClass().getName() + " instances for you!");
    }

    // ===================================================================

    /**
     * String text = StringFormatUtils.format("Usage this method is the same as [{}]", "org.slf4j.Logger");
     * ->
     * Usage this method is the same as [org.slf4j.Logger]
     *
     * @param text   the format message
     * @param params the format params
     * @return the format result
     */
    public static String format(String text, Object... params) {
        return String.format(convertToLoggerStyle(text), params);
    }

    /**
     * Convert the logger style {} to String.format's style %s
     *
     * @param text the origin text
     * @return the converted text
     */
    private static String convertToLoggerStyle(String text) {
        // Usage this method is the same as org.slf4j.Logger
        return text.replaceAll("\\{", "%s").replaceAll("}", "");
    }
}
