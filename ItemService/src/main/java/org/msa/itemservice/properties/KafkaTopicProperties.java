package org.msa.itemservice.properties;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "topic")
@Getter
public class KafkaTopicProperties {

    private String name;

}