# `Http-RPC framework`

> `Http ` Client package similar to `FeignClient ` writing



## 1.`import`

```xml
<dependency>
    <groupId>com.photowey</groupId>
    <artifactId>http-rpc-spring-boot-starter</artifactId>
    <version>1.0.0-SNAPSHOT</version>
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
// However, it does not currently support
```

