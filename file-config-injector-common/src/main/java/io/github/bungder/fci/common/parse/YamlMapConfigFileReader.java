package io.github.bungder.fci.common.parse;

import java.util.Map;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.io.ByteArrayResource;

/**
* @author bungder
 * @date 09/18/2021 11:56 PM
 */
public class YamlMapConfigFileReader implements MapConfigFileReader{

    @Override
    public Map<String, Object> readFromBinary(byte[] configContent) {
        YamlPropertiesFactoryBean yamlFactory = new YamlPropertiesFactoryBean();
        yamlFactory.setResources(new ByteArrayResource(configContent));
        return this.propertiesToMap(yamlFactory.getObject());
    }

    @Override
    public String[] getSupportedFileTypes() {
        return new String[]{"yml", "yaml"};
    }
}
