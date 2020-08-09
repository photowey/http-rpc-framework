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
package com.photowey.http.rpc.client.register;

import com.photowey.http.rpc.client.annotation.EnableHRpcClients;
import com.photowey.http.rpc.client.annotation.HRpcClient;
import com.photowey.http.rpc.core.exception.HRpcException;
import com.photowey.http.rpc.core.util.StringFormatUtils;
import com.photowey.http.rpc.core.util.HRpcUtils;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.AbstractClassTestingTypeFilter;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.*;

/**
 * the RpcClients Registrar
 *
 * @author WcJun
 * @date 2020/08/08
 * @since 1.0.0
 */
public class HRpcClientsRegistrar implements ImportBeanDefinitionRegistrar, ResourceLoaderAware, EnvironmentAware {

    private ResourceLoader resourceLoader;

    private Environment environment;

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        this.registerRpcClients(importingClassMetadata, registry);
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    // ==========================================================

    public void registerRpcClients(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
        ClassPathScanningCandidateComponentProvider scanner = this.getScanner();
        scanner.setResourceLoader(this.resourceLoader);
        Set<String> basePackages = new HashSet<>();

        Map<String, Object> clientsAttrs = metadata.getAnnotationAttributes(EnableHRpcClients.class.getName());
        AnnotationTypeFilter annotationTypeFilter = new AnnotationTypeFilter(HRpcClient.class);
        final Class<?>[] clients = clientsAttrs == null ? null : (Class<?>[]) clientsAttrs.get("clients");
        if (HRpcUtils.isEmpty(clients)) {
            scanner.addIncludeFilter(annotationTypeFilter);
            basePackages = this.getBasePackages(metadata);
        } else {
            final Set<String> clientClasses = new HashSet<>();
            basePackages = new HashSet<>();
            for (Class<?> clazz : clients) {
                basePackages.add(ClassUtils.getPackageName(clazz));
                clientClasses.add(clazz.getCanonicalName());
            }
            AbstractClassTestingTypeFilter filter = new AbstractClassTestingTypeFilter() {
                @Override
                protected boolean match(ClassMetadata metadata) {
                    String cleaned = metadata.getClassName().replaceAll("\\$", ".");
                    return clientClasses.contains(cleaned);
                }
            };
            scanner.addIncludeFilter(new AllTypeFilter(Arrays.asList(filter, annotationTypeFilter)));
        }

        for (String basePackage : basePackages) {
            Set<BeanDefinition> candidateComponents = scanner.findCandidateComponents(basePackage);
            for (BeanDefinition candidateComponent : candidateComponents) {
                if (candidateComponent instanceof AnnotatedBeanDefinition) {
                    // verify annotated class is an interface
                    AnnotatedBeanDefinition beanDefinition = (AnnotatedBeanDefinition) candidateComponent;
                    AnnotationMetadata annotationMetadata = beanDefinition.getMetadata();
                    this.checkAnnotationMetadata(annotationMetadata);
                    Map<String, Object> attributes = annotationMetadata.getAnnotationAttributes(HRpcClient.class.getCanonicalName());
                    this.registerRpcClient(registry, annotationMetadata, attributes);
                }
            }
        }
    }

    private void checkAnnotationMetadata(AnnotationMetadata annotationMetadata) {
        if (!annotationMetadata.isInterface()) {
            throw new HRpcException("@HRpcClient can only be specified on an interface");
        }
    }

    private void registerRpcClient(BeanDefinitionRegistry registry,
                                   AnnotationMetadata annotationMetadata, Map<String, Object> attributes) {
        String className = annotationMetadata.getClassName();
        BeanDefinitionBuilder definition = BeanDefinitionBuilder.genericBeanDefinition(HRpcClientFactoryBean.class);
        this.validate(attributes);
        definition.addPropertyValue("service", this.getService(attributes));
        definition.addPropertyValue("version", this.getVersion(attributes));
        String contextId = getContextId(attributes);
        definition.addPropertyValue("contextId", contextId);
        definition.addPropertyValue("type", className);
        definition.addPropertyValue("targetProxy", this.getTargetProxy(attributes));
        definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);

        String alias = contextId + "HRpcClient";
        AbstractBeanDefinition beanDefinition = definition.getBeanDefinition();

        // has a default, won't be
        boolean primary = (Boolean) attributes.get("primary");

        beanDefinition.setPrimary(primary);

        BeanDefinitionHolder holder = new BeanDefinitionHolder(beanDefinition, className, new String[]{alias});
        BeanDefinitionReaderUtils.registerBeanDefinition(holder, registry);
    }

    // ==========================================================

    protected ClassPathScanningCandidateComponentProvider getScanner() {
        return new ClassPathScanningCandidateComponentProvider(false, this.environment) {
            @Override
            protected boolean isCandidateComponent(
                    AnnotatedBeanDefinition beanDefinition) {
                boolean isCandidate = false;
                if (beanDefinition.getMetadata().isIndependent()) {
                    if (!beanDefinition.getMetadata().isAnnotation()) {
                        isCandidate = true;
                    }
                }
                return isCandidate;
            }
        };
    }

    // ==========================================================

    protected Set<String> getBasePackages(AnnotationMetadata importingClassMetadata) {
        Map<String, Object> attributes = importingClassMetadata.getAnnotationAttributes(EnableHRpcClients.class.getCanonicalName());

        Set<String> basePackages = new HashSet<>();
        for (String pkg : (String[]) attributes.get("value")) {
            if (StringUtils.hasText(pkg)) {
                basePackages.add(pkg);
            }
        }
        for (String pkg : (String[]) attributes.get("basePackages")) {
            if (StringUtils.hasText(pkg)) {
                basePackages.add(pkg);
            }
        }
        for (Class<?> clazz : (Class[]) attributes.get("basePackageClasses")) {
            basePackages.add(ClassUtils.getPackageName(clazz));
        }

        if (basePackages.isEmpty()) {
            basePackages.add(ClassUtils.getPackageName(importingClassMetadata.getClassName()));
        }

        return basePackages;
    }


    // ==========================================================

    private void validate(Map<String, Object> attributes) {
        // TODO
    }
    // ==========================================================

    private String getContextId(Map<String, Object> attributes) {
        String contextId = (String) attributes.get("contextId");
        if (!StringUtils.hasText(contextId)) {
            return getName(attributes);
        }

        contextId = resolve(contextId);
        return getName(contextId);
    }

    private String resolve(String value) {
        if (StringUtils.hasText(value)) {
            return this.environment.resolvePlaceholders(value);
        }
        return value;
    }

    String getName(Map<String, Object> attributes) {
        String name = (String) attributes.get("serviceId");
        if (!StringUtils.hasText(name)) {
            name = (String) attributes.get("name");
        }
        if (!StringUtils.hasText(name)) {
            name = (String) attributes.get("value");
        }
        name = resolve(name);
        return getName(name);
    }

    // ==========================================================

    private static String getName(String name) {
        if (!StringUtils.hasText(name)) {
            return "";
        }
        return name;
    }

    // ==========================================================

    private String getClientName(Map<String, Object> client) {
        if (client == null) {
            return null;
        }
        String value = (String) client.get("contextId");
        if (!StringUtils.hasText(value)) {
            value = (String) client.get("value");
        }
        if (!StringUtils.hasText(value)) {
            value = (String) client.get("name");
        }
        if (!StringUtils.hasText(value)) {
            value = (String) client.get("serviceId");
        }
        if (StringUtils.hasText(value)) {
            return value;
        }

        throw new IllegalStateException(StringFormatUtils.format("Either 'name' or 'value' must be provided in @{}", HRpcClient.class.getSimpleName()));
    }

    // ==========================================================

    private String getService(Map<String, Object> client) {
        if (client == null) {
            return null;
        }
        String value = (String) client.get("serviceId");
        if (!StringUtils.hasText(value)) {
            value = (String) client.get("name");
        }
        if (!StringUtils.hasText(value)) {
            value = (String) client.get("value");
        }
        if (StringUtils.hasText(value)) {
            return value;
        }

        throw new IllegalStateException(StringFormatUtils.format("Either 'serviceId' or 'name' or 'value' must be provided in @{}", HRpcClient.class.getSimpleName()));
    }

    private String getVersion(Map<String, Object> client) {
        String version = (String) client.get("version");

        return StringUtils.hasText(version) ? version : "1.0.0";
    }

    private String getTargetProxy(Map<String, Object> client) {
        String proxyTarget = (String) client.get("targetProxy");
        // use cglib by default
        return StringUtils.hasText(proxyTarget) ? proxyTarget : "cglib";
    }
    // ==========================================================

    /**
     * Helper class to create a {@link TypeFilter} that matches if all the delegates
     * match.
     *
     * @author Oliver Gierke
     */
    private static class AllTypeFilter implements TypeFilter {

        private final List<TypeFilter> delegates;

        /**
         * Creates a new {@link AllTypeFilter} to match if all the given delegates match.
         *
         * @param delegates must not be {@literal null}.
         */
        AllTypeFilter(List<TypeFilter> delegates) {
            Assert.notNull(delegates, "This argument is required, it must not be null");
            this.delegates = delegates;
        }

        @Override
        public boolean match(MetadataReader metadataReader,
                             MetadataReaderFactory metadataReaderFactory) throws IOException {

            for (TypeFilter filter : this.delegates) {
                if (!filter.match(metadataReader, metadataReaderFactory)) {
                    return false;
                }
            }

            return true;
        }
    }
}
