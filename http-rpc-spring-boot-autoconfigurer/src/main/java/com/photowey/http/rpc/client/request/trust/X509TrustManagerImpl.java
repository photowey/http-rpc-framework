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
package com.photowey.http.rpc.client.request.trust;

import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * X509TrustManagerImpl
 *
 * @author WcJun
 * @date 2020/08/08
 * @since 1.0.0
 */
public class X509TrustManagerImpl implements X509TrustManager {

    public X509TrustManagerImpl() {
    }

    @Override
    public void checkClientTrusted(X509Certificate[] var1, String var2) throws CertificateException {
    }

    @Override
    public void checkServerTrusted(X509Certificate[] var1, String var2) throws CertificateException {
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0];
    }
}
