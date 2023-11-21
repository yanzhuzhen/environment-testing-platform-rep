package Exmpl.vo.query;

import Exmpl.Entity.Role;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class roleQueryVo extends Role {
    private Long pageNow = 1L; //当前页码
    private Long pageSize = 10L; //每页显示数量
    private Long unonow;

}
