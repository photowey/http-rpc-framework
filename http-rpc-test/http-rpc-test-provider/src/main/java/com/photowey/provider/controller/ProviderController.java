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
package com.photowey.provider.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.photowey.provider.domain.HealthDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * User 控制器
 *
 * @author WcJun
 * @date 2020/08/09
 * @since 1.0.0
 */
@Slf4j
@RestController
@RequestMapping("/provider")
@Api(value = "Provider", tags = "Provider", description = "Provider-Entrance")
public class ProviderController {

    /**
     * GET :/test/get
     * test the http get request
     *
     * @param orderId
     * @param userId
     * @param app
     * @param port
     * @param hello
     * @param world
     * @return {@link HealthDTO}
     * @author WcJun
     * @date 2020/08/09
     * @see * http://localhost:8888/consumer/test/get
     */
    @GetMapping("/test/get/{orderId}/{userId}")
    @ApiOperation(value = "test the http get request", notes = "test the http get request")
    public HealthDTO testGet(
            @PathVariable("orderId") Long orderId, @PathVariable("userId") Long userId,
            @RequestHeader("app") String app, @RequestHeader("port") Integer port,
            @RequestParam("hello") String hello, @RequestParam("world") String world) {

        log.info("Request test post,the PathVariable:[orderId:{}, userId:{}]", orderId, userId);
        log.info("Request test post,the RequestHeader:[app:{}, port:{}]", app, port);
        log.info("Request test post,the RequestParam:[hello:{} port:{}]", hello, world);

        HealthDTO dto = new HealthDTO(app, port);

        return dto;
    }

    /**
     * POST :/test/post
     * test the http post request
     *
     * @param orderId
     * @param userId
     * @param body
     * @return {@link HealthDTO}
     * @author WcJun
     * @date 2020/08/09
     * @see * http://localhost:8888/consumer/test/post
     */
    @PostMapping("/test/post/{orderId}/{userId}")
    @ApiOperation(value = "test the http post request", notes = "test the http post request")
    public ResponseEntity<HealthDTO> testPost(
            @PathVariable("orderId") Long orderId, @PathVariable("userId") Long userId,
            @RequestBody HealthDTO body) {
        log.info("Request test post,the PathVariable:[orderId:{},userId:{}],the RequestBody is:\n{}",
                orderId, userId, JSON.toJSONString(body, SerializerFeature.PrettyFormat));

        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    /**
     * POST :/test/void
     * test the response is void type
     *
     * @param orderId
     * @param body
     * @author WcJun
     * @date 2020/08/09
     * @see * http://localhost:8888/consumer/test/void
     */
    @PostMapping("/test/void/{orderId}")
    @ApiOperation(value = "test the response is void type", notes = "test the response is void type")
    void testVoid(
            @PathVariable("orderId") Long orderId,
            @RequestBody HealthDTO body) {
        log.info("test response type void, the orderId:[{}] request body is:\n{}", orderId, JSON.toJSONString(body, SerializerFeature.PrettyFormat));
    }

    /**
     * PUT :/test/put
     * test the http put request
     *
     * @param orderId
     * @param userId
     * @param body
     * @return {@link HealthDTO}
     * @author WcJun
     * @date 2020/08/09
     * @see * http://localhost:8888/consumer/test/put
     */
    @PutMapping("/test/put/{orderId}/{userId}")
    @ApiOperation(value = "test the http put request", notes = "test the http put request")
    public ResponseEntity<HealthDTO> testPut(
            @PathVariable("orderId") Long orderId, @PathVariable("userId") Long userId,
            @RequestBody HealthDTO body) {
        log.info("Request test put,the PathVariable:[orderId:{},userId:{}],the RequestBody is:\n{}",
                orderId, userId, JSON.toJSONString(body, SerializerFeature.PrettyFormat));

        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    /**
     * PATCH :/test/patch
     * test the http patch request
     *
     * @param orderId
     * @param userId
     * @param body
     * @return {@link HealthDTO}
     * @author WcJun
     * @date 2020/08/09
     * @see * http://localhost:8888/consumer/test/patch
     */
    @PatchMapping("/test/patch/{orderId}/{userId}")
    @ApiOperation(value = "test the http patch request", notes = "test the http patch request")
    public ResponseEntity<HealthDTO> testPatch(
            @PathVariable("orderId") Long orderId, @PathVariable("userId") Long userId,
            @RequestBody HealthDTO body) {
        log.info("Request test patch,the PathVariable:[orderId:{},userId:{}],the RequestBody is:\n{}",
                orderId, userId, JSON.toJSONString(body, SerializerFeature.PrettyFormat));

        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    /**
     * DELETE :/test/post
     * test the http post request
     *
     * @param orderId
     * @param userId
     * @param body
     * @return {@link HealthDTO}
     * @author WcJun
     * @date 2020/08/09
     * @see * http://localhost:8888/consumer/test/delete
     */
    @DeleteMapping("/test/delete/{orderId}/{userId}")
    @ApiOperation(value = "test the http delete request", notes = "test the http delete request")
    public ResponseEntity<HealthDTO> testDelete(
            @PathVariable("orderId") Long orderId, @PathVariable("userId") Long userId,
            @RequestBody HealthDTO body) {
        log.info("Request test delete,the PathVariable:[orderId:{},userId:{}],the RequestBody is:\n{}",
                orderId, userId, JSON.toJSONString(body, SerializerFeature.PrettyFormat));

        return new ResponseEntity<>(body, HttpStatus.OK);
    }
}