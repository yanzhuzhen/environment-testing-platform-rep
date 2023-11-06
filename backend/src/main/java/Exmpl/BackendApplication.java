package Exmpl;

import org.jasypt.util.text.BasicTextEncryptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@MapperScan("Exmpl.Dao")
@SpringBootApplication
@EnableSwagger2
public class BackendApplication {

    public static void main(String[] args) {
        BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
        textEncryptor.setPassword("123asd!@#");
//        要加密的数据（数据库的用户名或密码）
        String username = textEncryptor.encrypt("Arno");
        String password = textEncryptor.encrypt("110894");
        System.out.println("username:"+username);
        String encryptedPassword =textEncryptor.decrypt (password);
        System.out.println("password:" + password + "   decrypted:" + encryptedPassword);

        SpringApplication.run(BackendApplication.class, args);
    }

}
