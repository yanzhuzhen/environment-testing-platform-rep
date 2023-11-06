package Exmpl.example.backend;
import Exmpl.Entity.User;
import Exmpl.Service.userService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.annotation.Resource;

@SpringBootTest
public class test {
    @Resource
    BCryptPasswordEncoder passwordEncoder;
    @Test
    public  void  test1(){
        System.out.println(passwordEncoder.encode("123456"));

    }


}

