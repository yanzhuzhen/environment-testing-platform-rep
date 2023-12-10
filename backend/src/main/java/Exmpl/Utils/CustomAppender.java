package Exmpl.Utils;
import Exmpl.Dao.incMapper;
import Exmpl.Entity.Log;
import ch.qos.logback.core.AppenderBase;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.spi.FilterReply;

import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CustomAppender extends AppenderBase<ILoggingEvent> {

    private Connection connection;

    // 设置数据库连接信息
    private final String driverClass = "com.mysql.cj.jdbc.Driver";
    private final String url = "jdbc:mysql://localhost:3306/mydatabase?useUnicode=true&characterEncoding=utf-8&serverTimezone=GMT%2B8";
    private final String username = "root";
    private final String password = "11203172";

    @Override
    public void start() {
        try {
            // 初始化数据库连接
            Class.forName(driverClass);
            connection = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException | SQLException e) {
            addError("连接数据库失败", e);
        }
        super.start();
    }

    @Override
    protected void append(ILoggingEvent event) {

        String loglevel = event.getLevel().toString();
        String logcontent = event.getFormattedMessage();
        String logtime = dateUtils.getLocalTimeNow();
        String logclass = event.getLoggerName();
        String logthread = event.getThreadName();
        String trackid = event.getMDCPropertyMap().get("TRACE_ID");
        String logname = event.getLoggerName();
        // 将日志信息插入数据库表
        if(logname.startsWith("Exmpl")){
            try {
                PreparedStatement statement = connection.prepareStatement("insert into log(loglevel,logcontent,logtime,logclass,logthread,trackid,logname) values(?,?,?,?,?,?,?)");
                statement.setString(1, loglevel);
                statement.setString(2, logcontent);
                statement.setString(3, logtime);
                statement.setString(4, logclass);
                statement.setString(5, logthread);
                statement.setString(6, trackid);
                statement.setString(7, logname);
                statement.executeUpdate();
            } catch (SQLException e) {
                addError("插入失败", e);
            }
        }else{
            //非项目本身的日志不会入库
            System.out.println("非项目本身的日志不会入库");

        }
    }

    @Override
    public void stop() {
        try {
            // 关闭数据库连接
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            addError("关闭数据库失败", e);
        }
        super.stop();
    }
}
