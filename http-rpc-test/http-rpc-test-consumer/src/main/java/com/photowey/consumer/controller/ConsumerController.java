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
package com.photowey.consumer.controller;

import com.photowey.consumer.client.HRpcProviderClient;
import com.photowey.consumer.domain.HealthDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * The Consumer Controller
 *
 * @author WcJun
 * @date 2020/08/09
 * @since 1.0.0
 */
@RestController
@RequestMapping("/consumer")
@Api(value = "Consumer", tags = "Consumer-module", description = "Consumer-Entrance")
public class ConsumerController {

    @Value("${spring.application.name}")
    private String app;

    @Value("${server.port:9527}")
    private Integer port;

    @Autowired
    private HRpcProviderClient hrpcProviderClient;

    /**
     * GET :/test/get
     * test the http get request
     *
     * @return {@link HealthDTO}
     * @author WcJun
     * @date 2020/08/09
     * @see * http://localhost:9999/consumer/test/get
     */
    @GetMapping("/test/get")
    @ApiOperation(value = "test the http get request", notes = "test the http get request")
    public ResponseEntity<HealthDTO> testGet() {
        Map<String, Object> queries = new HashMap<>();
        queries.put("hello", "hello");
        queries.put("world", "world");
        HealthDTO healthDTO = this.hrpcProviderClient.testGet(939L, 9527L, app, port, queries);
        return new ResponseEntity<>(healthDTO, HttpStatus.OK);
    }

    /**
     * POST :/test/post
     * test the http post request
     *
     * @return {@link HealthDTO}
     * @author WcJun
     * @date 2020/08/09
     * @see * http://localhost:9999/consumer/test/post
     */
    @PostMapping("/test/post")
    @ApiOperation(value = "test the http post request", notes = "test the http post request")
    public ResponseEntity<HealthDTO> testPost() {
        HealthDTO requestBody = new HealthDTO();
        requestBody.setApp(app);
        requestBody.setPort(port);
        ResponseEntity<HealthDTO> testPost = this.hrpcProviderClient.testPost(939L, 9527L, requestBody);
        return testPost;
    }

    /**
     * POST :/test/void
     * test the response is void type
     *
     * @author WcJun
     * @date 2020/08/09
     * @see * http://localhost:9999/consumer/test/void
     */
    @PostMapping("/test/void")
    @ApiOperation(value = "test the response is void type", notes = "test the response is void type")
    public ResponseEntity<HealthDTO> testVoid() {
        HealthDTO health = new HealthDTO();
        health.setApp(app);
        health.setPort(port);
        this.hrpcProviderClient.testVoid(939L, health);
        return new ResponseEntity<>(health, HttpStatus.OK);
    }

    /**
     * PUT :/test/put
     * test the http put request
     *
     * @return {@link HealthDTO}
     * @author WcJun
     * @date 2020/08/09
     * @see * http://localhost:9999/consumer/test/put
     */
    @PutMapping("/test/put")
    @ApiOperation(value = "test the http put request", notes = "test the http put request")
    public ResponseEntity<HealthDTO> testPut() {
        HealthDTO requestBody = new HealthDTO();
        requestBody.setApp(app);
        requestBody.setPort(port);
        ResponseEntity<HealthDTO> testPost = this.hrpcProviderClient.testPut(939L, 9527L, requestBody);
        return testPost;
    }

    /**
     * PUT :/test/patch
     * test the http patch request
     *
     * @return {@link HealthDTO}
     * @author WcJun
     * @date 2020/08/09
     * @see * http://localhost:9999/consumer/test/patch
     */
    @PatchMapping("/test/patch")
    @ApiOperation(value = "test the http patch request", notes = "test the http patch request")
    public ResponseEntity<HealthDTO> testPatch() {
        HealthDTO requestBody = new HealthDTO();
        requestBody.setApp(app);
        requestBody.setPort(port);
        ResponseEntity<HealthDTO> testPost = this.hrpcProviderClient.testPatch(939L, 9527L, requestBody);
        return testPost;
    }

    /**
     * PUT :/test/delete
     * test the http delete request
     *
     * @return {@link HealthDTO}
     * @author WcJun
     * @date 2020/08/09
     * @see * http://localhost:9999/consumer/test/delete
     */
    @DeleteMapping("/test/delete")
    @ApiOperation(value = "test the http delete request", notes = "test the http delete request")
    public ResponseEntity<HealthDTO> testDelete() {
        HealthDTO requestBody = new HealthDTO();
        requestBody.setApp(app);
        requestBody.setPort(port);
        ResponseEntity<HealthDTO> testPost = this.hrpcProviderClient.testDelete(939L, 9527L, requestBody);
        return testPost;
    }
}
