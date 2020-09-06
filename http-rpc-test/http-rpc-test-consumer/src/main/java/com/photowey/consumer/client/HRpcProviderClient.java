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
package com.photowey.consumer.client;

import com.photowey.consumer.domain.HealthDTO;
import com.photowey.http.rpc.client.annotation.HRpcClient;
import com.photowey.http.rpc.core.annotation.*;
import com.photowey.http.rpc.core.enums.HostTypeEnum;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * HRpcProviderClient
 *
 * @author WcJun
 * @date 2020/08/09
 * @since 1.0.0
 */
@HRpcClient(value = "provider")
public interface HRpcProviderClient {

    @HttpGet(
            protocol = "http",
            host = "localhost:8888",
            uri = "/provider/test/get/{orderId}/{userId}",
            hostType = HostTypeEnum.DYNAMIC
    )
    HealthDTO testGet(
            @PathVariable("orderId") Long orderId, @PathVariable("userId") Long userId,
            @RequestHeader("app") String app, @RequestHeader("port") Integer port,
            @RequestParam Map<String, Object> additional
    );

    @HttpPost(protocol = "http", host = "localhost:8888", uri = "/provider/test/post/{orderId}/{userId}")
    ResponseEntity<HealthDTO> testPost(
            @PathVariable("orderId") Long orderId, @PathVariable("userId") Long userId,
            @RequestBody HealthDTO body
    );

    @HttpPost(protocol = "http", host = "localhost:8888", uri = "/provider/test/void/{orderId}")
    void testVoid(
            @PathVariable("orderId") Long orderId,
            @RequestBody HealthDTO body
    );

    @HttpPut(protocol = "http", host = "localhost:8888", uri = "/provider/test/put/{orderId}/{userId}")
    ResponseEntity<HealthDTO> testPut(
            @PathVariable("orderId") Long orderId, @PathVariable("userId") Long userId,
            @RequestBody HealthDTO body
    );

    @HttpPatch(protocol = "http", host = "localhost:8888", uri = "/provider/test/patch/{orderId}/{userId}")
    ResponseEntity<HealthDTO> testPatch(
            @PathVariable("orderId") Long orderId, @PathVariable("userId") Long userId,
            @RequestBody HealthDTO body
    );

    @HttpDelete(protocol = "http", host = "localhost:8888", uri = "/provider/test/delete/{orderId}/{userId}")
    ResponseEntity<HealthDTO> testDelete(
            @PathVariable("orderId") Long orderId, @PathVariable("userId") Long userId,
            @RequestBody HealthDTO body
    );
}
