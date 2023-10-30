package Exmpl.Entity;

import com.baomidou.mybatisplus.annotation.TableId;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("role")
public class Role {
    @TableId
    private Long rno;
    private String rolename;
    private boolean  isAdmin;
//    private boolean isUpdate = false;
//    private boolean isDelete = false;
//    public  Role() {
//        if (isAdmin) {
//            isUpdate = true;
//            isDelete = true;
//        }
//    }



}
