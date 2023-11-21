package Exmpl.Entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Column;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@TableName("usertable")
public class User implements Serializable, UserDetails {
    @TableId
    private Long uno;
    private String username;
    private String password;
    private String avatar;
    private String email;
    private Long phone;
    private String realname;
    private int isadmin;
    private boolean isAccountNonExpired = true;
    private boolean isAccountNonLocked = true;
    private boolean isCredentialsNonExpired = true;
    private boolean isEnabled = true;
    private int isdelete;


    @TableField(exist = false)
    private List<Role> roles;

    @TableField(exist = false)
    private List<Menu> menuList;
    @TableField(exist = false)
    Collection<? extends GrantedAuthority> authorities;


}
