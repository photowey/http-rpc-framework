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
package com.photowey.provider;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.InetAddress;
import java.net.UnknownHostException;


@Slf4j
@SpringBootApplication
public class ProviderApp {

    public static void main(String[] args) throws UnknownHostException {
        SpringApplication.run(ProviderApp.class, args);
        log.info("\n----------------------------------------------------------\n\t" +
                        "Bootstrap: '{}' is Success!\n\t" +
                        "Application: '{}' is running! Access URLs:\n\t" +
                        "Local: \t\t{}://localhost:{}\n\t" +
                        "Swagger: \t{}://localhost:{}{}\n\t" +
                        "External: \t{}://{}:{}\n\t" +
                        "Profile(s): \t{}\n----------------------------------------------------------\n",
                // Bootstrap
                "Provider" + " Context",
                // Application
                "Provider App",
                // Local
                "http", "8888",
                // Swagger
                "http", "8888", "/swagger-ui.html",
                // External
                "http", InetAddress.getLocalHost().getHostAddress(), "8888",
                // Profile(s)
                "dev"
        );
    }

}
