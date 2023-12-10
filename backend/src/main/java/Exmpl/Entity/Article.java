package Exmpl.Entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.Column;
import java.util.Date;

@Data
@TableName("article")
public class Article {
    private String title;
    private String author;
    private String content;
    private Long id;
    private int commentdisable; //关闭评论
    private int score; //评分
    private String status; //状态
    @Column(name = "displaytime", columnDefinition = "date")
    private Date displaytime; //发布时间
    @Column(name = "contentshort", columnDefinition = "varchar(255)")
    private String contentshort; //概要
}
