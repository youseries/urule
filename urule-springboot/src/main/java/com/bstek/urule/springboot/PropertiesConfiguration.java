package com.bstek.urule.springboot;

import com.bstek.urule.URulePropertyPlaceholderConfigurer;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

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

    @Autowired
    private ResourceLoader resourceLoader;

    public void afterPropertiesSet() throws Exception {
        // 外部配置定位
        List<Resource> resources = new ArrayList<>();
        String springConfigLocation = System.getProperty(SPRING_CONFIG_LOCATION);
        if (springConfigLocation != null) {
            String[] configFiles = springConfigLocation.split(",|;");

            for (String configFile : configFiles) {
                resources.add(resourceLoader.getResource(configFile));
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
                logger.error("extra properties init failed", e);
            }
        }
        super.setPropertiesArray(propertiesList.toArray(new Properties[propertiesList.size()]));
    }
}
