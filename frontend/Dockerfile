# 使用一个基础镜像，例如Node.js
FROM node:16

# 设置工作目录
WORKDIR /app

# 复制前端应用程序代码到容器中
COPY . .

# 安装前端应用程序的依赖
RUN npm install

# 构建前端应用程序
RUN npm run build:stage

# 暴露前端应用程序的端口（根据应用程序配置）
EXPOSE 9527

# 启动前端应用程序
CMD ["npm", "run","dev"]