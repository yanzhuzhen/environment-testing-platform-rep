package Exmpl.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class routerVo {
    private String path; //路由地址
    private String name; //路由名字
    private  String component; //组件名
    private boolean alwaysShow; //是否显示
    private Meta mata;

    @Data
    @AllArgsConstructor
    public class Meta {
        private String title; //标题
        private String icon; //图标
        private Object[] roles; //角色
    }

    private List<routerVo> children = new ArrayList<routerVo>();

}
