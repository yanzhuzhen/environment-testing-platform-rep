package com.example.backend;


import org.jasypt.util.text.BasicTextEncryptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.Mapper")
public class BackendApplication {

    public static void main(String[] args) {
        BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
        textEncryptor.setPassword("123asd!@#");
        //要加密的数据（数据库的用户名或密码）
//        String username = textEncryptor.encrypt("system");
//        String password = textEncryptor.encrypt("11203172");
//        System.out.println("username:"+username);
//        String encryptedPassword =textEncryptor.decrypt (password);
//        System.out.println("password:" + encryptedPassword);

        SpringApplication.run(BackendApplication.class, args);
    }

}
