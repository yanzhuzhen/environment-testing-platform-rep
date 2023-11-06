package Exmpl.Entity;

import com.baomidou.mybatisplus.annotation.TableId;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("roletable")
public class Role {
    @TableId
    private Long rno;
    private String rolename;
    private String remark;

}
