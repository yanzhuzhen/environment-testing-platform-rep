# 使用官方MySQL镜像作为基础镜像
FROM mysql:latest

# 将自定义的初始化SQL脚本复制到容器中
COPY init.sql /docker-entrypoint-initdb.d/

# 设置MySQL环境变量
ENV MYSQL_ROOT_PASSWORD=11203172

# 暴露MySQL默认端口
EXPOSE 3306
