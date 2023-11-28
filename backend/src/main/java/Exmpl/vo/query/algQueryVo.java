package Exmpl.vo.query;

import Exmpl.Entity.Alg;
import lombok.Data;

@Data
public class algQueryVo extends Alg {
    private Long unonow;
    private Long pageNow = 1L; //当前页码
    private Long pageSize = 10L; //每页显示数量
}

