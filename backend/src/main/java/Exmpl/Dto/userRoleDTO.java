package Exmpl.Dto;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
public class userRoleDTO {
    private Long uno;
    private List<Long> rnoList;
}
