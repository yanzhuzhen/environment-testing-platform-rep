package Exmpl.vo.query;

import Exmpl.Entity.Follows;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class followsQueryVo extends Follows {
    private Long unonow;
    private Long pageNow = 1L; //当前页码
    private Long pageSize = 10L; //每页显示数量
}
