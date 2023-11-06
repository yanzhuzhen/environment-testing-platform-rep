package Exmpl.Dto;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
public class roleMenuDTO {
    private Long rno;
    private List<Long> list;
}
