package Exmpl.Dto;

import Exmpl.Entity.User;
import lombok.Data;

@Data
public class userDTO extends User {
    private String confirmPassword;
    private String newemail;

}
