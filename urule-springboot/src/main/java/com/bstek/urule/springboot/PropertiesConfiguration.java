package com.bstek.urule.springboot;

import com.bstek.urule.URulePropertyPlaceholderConfigurer;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author Jacky.gao
 * @since 2016年10月12日
 */
@Configuration
public class PropertiesConfiguration extends URulePropertyPlaceholderConfigurer implements InitializingBean {

    public static final String SPRING_CONFIG_LOCATION = "spring.config.location";

    @Override
    public void afterPropertiesSet() throws Exception {

        // 外部配置定位
        List<Resource> resources = new ArrayList<>();
        String springConfigLocation = System.getProperty(SPRING_CONFIG_LOCATION);
        if (springConfigLocation != null) {
            String[] configFiles = springConfigLocation.split(",|;");

            for (String configFile : configFiles) {
                String trimConfigFile = StringUtils.trim(configFile);

                if (trimConfigFile.startsWith(ResourceUtils.FILE_URL_PREFIX)) {
                    resources.add(new FileSystemResource(trimConfigFile));
                } else if (trimConfigFile.startsWith(ResourceUtils.CLASSPATH_URL_PREFIX)) {
                    resources.add(new ClassPathResource(trimConfigFile));
                } else {
                    Resource resource = new FileSystemResource(trimConfigFile);
                    if (!resource.exists()) {
                        resource = new ClassPathResource(trimConfigFile);
                    }
                    resources.add(resource);
                }
            }
        } else {
            resources.add(new ClassPathResource("application.properties"));
        }

        // 配置加载并覆盖 urule-console 与 urule-core 中的配置项
        List<Properties> propertiesList = new ArrayList<>();
        for (Resource resource : resources) {
            try {
                if (resource.exists()) {
                    Properties properties = new Properties();
                    properties.load(resource.getInputStream());
                    propertiesList.add(properties);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.setPropertiesArray(propertiesList.toArray(new Properties[propertiesList.size()]));
    }
}
