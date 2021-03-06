# `Http-RPC framework`

> `Http ` Client package similar to `FeignClient ` writing


# v1.0.0

## 1.`import`

```xml
<dependency>
    <groupId>com.photowey</groupId>
    <artifactId>http-rpc-spring-boot-starter</artifactId>
    <version>1.0.0</version>
</dependency>
```



## 2.`yml config`

```yml
hrpc:
  client:
    #@see com.photowey.http.rpc.core.enums.ExecutorEnum
    executor-type: "OK_HTTP"
    # connect-timeout: 6
    # read-timeout: 60
    # write-timeout: 60
```



## 3.`remote Interface`

```java
// http-rpc-test/com.photowey.consumer.client.HRpcProviderClient
/**
 * HRpcProviderClient
 *
 * @author WcJun
 * @date 2020/08/09
 * @since 1.0.0
 */
@HRpcClient(value = "hrpcProviderClient")
public interface HRpcProviderClient {
    // some methods...
}

// @HRpcClient: The interface is a remote call client, and a proxy object needs to be generated
// hrpcProviderClient: the alisa
```



## 4.`load Http-Rpc module`

```java
// load the HRpcClient module into IOC
@EnableHRpcClients(basePackages = {"com.photowey.consumer.client"})

// com.photowey.consumer.client: the package of HRpcClient
```



## 5.`test module`

```java
// ./http-rpc-test
// http-rpc-test/http-rpc-test-provider
// http-rpc-test/http-rpc-test-consumer

// consumer <--> HRpcClient <--> provider
```



## 6.`proxy mode`

```java
// @{link com.photowey.http.rpc.client.factory.CglibProxyFactory}
// Cglib

// @{link com.photowey.http.rpc.client.factory.JdkProxyFactory}
// Jdk
```



## 7.`http request executor`

```java
// @{com.photowey.http.rpc.client.request.okhttp.OkHttpRequestExecutor}
// OKHttp

// @{link com.photowey.http.rpc.client.request.httpclient.HttpClientRequestExecutor}
// Apache Http Client
```



## 8.`extension the OkHttp request executor`

```java
// ./http-rpc-test/com.photowey.consumer.config.OkHttpRequestExecutorExt
// At this time, the OkHttpRequestExecutor executor will not be added to the IOC
// E.G.

@Slf4j
@Component
public class OkHttpRequestExecutorExt extends OkHttpRequestExecutor {

    public HttpClientRequestExecutorExt(HRpcConfiguration hrpcConfiguration) {
        super(hrpcConfiguration);
    }

    @Override
    protected void preBuildClient(OkHttpClient.Builder builder) {
        super.preBuildClient(builder);
        log.info("execute the preBuildClient() in sub-class:[{}]", this.getClass().getSimpleName());
    }

    @Override
    protected void preExecuteRequest(OkHttpClient client, Request request) {
        super.preExecuteRequest(client, request);
        log.info("execute the preExecuteRequest() in sub-class:[{}]", this.getClass().getSimpleName());
    }
}
```



## 9.`extension the Http Client request executor`

```java
// ./http-rpc-test/com.photowey.consumer.config.HttpClientRequestExecutorExt
// At this time, the HttpClientRequestExecutor executor will not be added to the IOC
// E.G.
@Slf4j
@Component
public class HttpClientRequestExecutorExt extends HttpClientRequestExecutor {

    public HttpClientRequestExecutorExt(HRpcConfiguration hrpcConfiguration) {
        super(hrpcConfiguration);
    }

    @Override
    public void requestEnhance(HttpEntityEnclosingRequestBase requestBase) {
        super.requestEnhance(requestBase);
        log.info("execute the requestEnhance() in sub-class:[{}]", this.getClass().getSimpleName());
    }
}
```


# v1.1.0

```text
1.Support the cluster mode. there are fewer applicable scenarios, unless there is no support from a registry, such as Nacos.
2.The strategy impl in package: com.photowey.http.rpc.client.cluster
```
## 1.`import`

```xml
<dependency>
    <groupId>com.photowey</groupId>
    <artifactId>http-rpc-spring-boot-starter</artifactId>
    <version>1.1.0</version>
</dependency>
```

## 2.modify the consumer's client config
```java
// the value() = provider must be IN config -> hrpc.client.services.service
@HRpcClient(value = "provider")
public interface HRpcProviderClient {
    
}
```

## 2.the config need modify,if in cluster mode.
```yml
hrpc:
  client:
    # @see com.photowey.http.rpc.core.enums.ExecutorEnum
    executor-type: "APACHE_HTTP_CLIENT"
    # connect-timeout: 6
    # read-timeout: 60
    # write-timeout: 60
    # @see com.photowey.http.rpc.core.enums.ClusterStrategyEnum
    # default is: POLLING
    # @since 1.1.0
    cluster-strategy: "RANDOM"
    services:
      # @see com.photowey.consumer.client.HRpcProviderClient#HRpcClient.value()
      - service: "provider"
        routes:
          - {ip: "192.168.0.5", port: 8888}
```

## 3.modify the consumer's client method Annotation
```java
// HttpGet.hostType IN (STATIC, DYNAMIC)
// STATIC: use the annotation host() value  
// DYNAMIC: find the route info in config -> hrpc.client.services.service by HRpcClient.value()
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
```

