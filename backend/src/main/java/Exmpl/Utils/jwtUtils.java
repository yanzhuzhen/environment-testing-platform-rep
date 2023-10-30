package Exmpl.Utils;

import Exmpl.Entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Data
@Component
@ConfigurationProperties(prefix = "myjwt")
public class jwtUtils {
    private Key secret; //密钥
    private long expires; //过期时间

    //从数据声明生产令牌
    private String generateToken(Map<String,Object> claims) {
        Date expirationDate = new Date(System.currentTimeMillis() + expires);
        return Jwts.builder().setClaims(claims).setExpiration(expirationDate)
                .signWith(secret, SignatureAlgorithm.HS512).compact();

    }

    //从令牌中获取数据声明
    public Claims getClaims(String token){
        Claims claims;
        try{
            claims = Jwts.parserBuilder().setSigningKey(secret).build().parseClaimsJws(token).getBody();
        } catch (Exception e){
            claims = null;
        }
        return claims;
    }

    //生成令牌
    public String generateToken(UserDetails userDetails){
        Map<String, Object> myclaims = new HashMap<>(2);
        myclaims.put(Claims.SUBJECT, userDetails.getUsername());
        myclaims.put(Claims.ISSUED_AT, new Date());
        return generateToken(myclaims);
    }

    //从令牌中获取用户名
    public String getUsernameFromToken(String token){
        String username;
        try {
            Claims claims = getClaims(token);
            username = claims.getSubject();
        } catch (Exception e){
            username = null;
        }
        return username;
    }

    //判断令牌是否过期
    public boolean isTokenExpired(String token) {
        return getClaims(token).getExpiration().before(new Date());
    }

    //刷新令牌
    public String reToken(String token) {
        String reToken;
        try {
            Claims claims = getClaims(token);
            claims.put(Claims.ISSUED_AT, new Date());
            reToken = generateToken(claims);
        } catch (Exception e) {
            reToken = null;
        }
        return reToken;
    }

    //验证令牌
    public Boolean checkToken(String token, UserDetails userDetails) {
        User user = (User) userDetails;
        String username = getUsernameFromToken(token);
        return (username.equals(user.getUsername())&&isTokenExpired(token));
    }
}
