package Exmpl.Utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class loginResult {
    private int uno;
    private  int code;
    private String token;
    private Long expires;
}
