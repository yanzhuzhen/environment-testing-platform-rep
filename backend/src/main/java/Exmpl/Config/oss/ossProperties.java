package Exmpl.Config.oss;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@ConfigurationProperties(prefix = "aliyun.oss.file")
@Component
public class ossProperties {
    private String endpoint;
    private String keyId;
    private String keySecret;
    private String bucketName;



}
