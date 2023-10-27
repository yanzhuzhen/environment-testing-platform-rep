package Exmpl.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class tokenVo {
    private Long  expire;   //过期时间
    private String token;
}
