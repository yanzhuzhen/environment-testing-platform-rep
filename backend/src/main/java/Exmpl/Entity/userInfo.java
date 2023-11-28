package Exmpl.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class userInfo implements Serializable {
    private Long uno;
    private String username;
    private String realname;
    private Long phone;
    private String email;
    private String avatar;
    private String remark;
    private Object[] roles;


}
