package Exmpl.Entity;

import com.baomidou.mybatisplus.annotation.TableId;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.Column;

@Data
@TableName("roletable")
public class Role {
    @TableId
    private Long rno;
    private String rolename;
    private String remark;
    private String createuser;
    private String rolecode;
}
