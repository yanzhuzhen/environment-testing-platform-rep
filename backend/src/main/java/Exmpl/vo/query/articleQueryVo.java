package Exmpl.vo.query;

import Exmpl.Entity.Article;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
public class articleQueryVo extends Article {
    private Long unonow;
    private Long pageNow = 1L; //当前页码
    private Long pageSize = 10L; //每页显示数量
}
