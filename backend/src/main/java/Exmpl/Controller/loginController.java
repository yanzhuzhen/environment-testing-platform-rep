package Exmpl.Controller;

import Exmpl.Utils.commonUtils;
import Exmpl.Utils.verificationCode;
import Exmpl.api.Login.LoginRequest;
import Exmpl.api.Login.LoginResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class loginController {

    @PatchMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest){
        Map<String, String> users = new HashMap<>();
        users.put("admin","admin123");
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();
        if(commonUtils.InputTest(username,password)) {
            if (users.containsKey(username) && users.get(username).equals(password)) {
                // 生成用户令牌
//                String token = generateToken(username);
//                return ResponseEntity.ok(new LoginResponse(token));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("用户名或密码错误");
            }
        }
        return null;
    }
    @GetMapping("/verifyCode")
    public void verifyCode(HttpServletRequest request, HttpServletResponse resp) throws IOException {
        verificationCode code = new verificationCode();
        BufferedImage image = code.getImage();
        String text = code.getText();
        HttpSession session = request.getSession(true);
        session.setAttribute("verify_code", text);
        verificationCode.output(image,resp.getOutputStream());
    }
}






