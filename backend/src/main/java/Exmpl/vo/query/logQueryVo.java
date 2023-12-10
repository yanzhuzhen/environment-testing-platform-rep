package Exmpl.vo.query;

import Exmpl.Entity.Log;
import lombok.Data;

@Data
public class logQueryVo extends Log {
    private Long pageNow = 1L; //当前页码
    private Long pageSize = 10L; //每页显示数量
}
