package Exmpl.Entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("log")
public class Log {
    private Long id;
    private String loglevel;
    private String logcontent;
    private String logtime;
    private String logclass;
    private String logthread;
    private String trackid;
    private String logname;
}

