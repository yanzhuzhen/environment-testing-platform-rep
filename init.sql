create database mydatabase;

use mydatabase;

create table algorithmtable
(
    ano        bigint auto_increment
        primary key,
    algname    varchar(255)      null,
    remark     varchar(255)      null,
    createuser varchar(255)      null,
    createuno  bigint            null,
    ispermit   int(10) default 0 null,
    algtype    int(10)           null,
    context    text              null,
    lang       varchar(255)      null
);

create table article
(
    id              int                      not null
        primary key,
    title           varchar(255)             null,
    author          varchar(255)             null,
    content         longtext charset utf8mb4 null,
    display_time    date                     null,
    content_short   varchar(255)             null,
    comment_disable int default 0            null,
    status          varchar(255)             null,
    score           int default 0            null
);

create table follows
(
    id       int auto_increment
        primary key,
    uno      int          not null comment '粉丝id',
    username varchar(255) not null,
    fno      int          not null comment '发布者id',
    fname    varchar(255) not null,
    date     datetime     not null
);

create table likes
(
    id   int      not null
        primary key,
    uno  int      not null,
    date datetime null
);

create table menutable
(
    mno      bigint(10) auto_increment comment '菜单id'
        primary key,
    label    varchar(50)      null comment '菜单标题',
    pid      bigint(10)       null comment '父菜单id',
    pname    varchar(50)      null comment '父菜单名',
    name     varchar(50)      null comment '菜单名字',
    url      varchar(255)     null comment '菜单地址',
    path     varchar(255)     null comment '菜单路径',
    icon     varchar(255)     null comment '菜单图标',
    code     varchar(50)      null comment '菜单字段',
    type     int(5)           not null comment '菜单类型（0为菜单1为子菜单2为按钮）',
    isdelete int(5) default 1 not null comment '是否删除（1为是0为否）'
);

create table roletable
(
    rno        bigint(10) auto_increment
        primary key,
    rolename   varchar(50)  null,
    rolecode   varchar(50)  null,
    remark     varchar(255) null,
    createuser bigint(10)   null
);

create table mr
(
    mno bigint(10) not null,
    rno bigint(10) not null,
    primary key (mno, rno),
    constraint mno
        foreign key (mno) references menutable (mno),
    constraint rno
        foreign key (rno) references roletable (rno)
);

create table ur
(
    uno bigint(10) not null,
    rno bigint(10) not null,
    primary key (uno, rno)
);

create table usertable
(
    uno                        bigint(10) auto_increment
        primary key,
    username                   varchar(50)                                                                         null,
    password                   varchar(255) default '$2a$10$CDGnjZfCttqfzfoK8i0sI.4LOgaw18SuM/K6z8XiRXebXwJOkkWX.' null,
    avatar                     varchar(255)                                                                        null,
    email                      varchar(255)                                                                        not null,
    phone                      bigint                                                                              null,
    realname                   varchar(50)                                                                         null,
    isadmin                    int(5)       default 0                                                              not null,
    is_account_non_expired     int(5)       default 1                                                              null,
    is_account_non_locked      int(5)       default 1                                                              null,
    is_credentials_non_expired int(5)       default 1                                                              null,
    is_enabled                 int(5)       default 0                                                              null,
    isdelete                   int(5)       default 1                                                              null
);







INSERT INTO `usertable`(`uno`, `username`, `password`, `avatar`, `email`, `phone`, `realname`, `isadmin`, `is_account_non_expired`, `is_account_non_locked`, `is_credentials_non_expired`, `is_enabled`, `isdelete`) VALUES (1, 'admin', '$2a$10$SZceRyX.3TXXArNwno5.yuWg6g/T8nUmgRuAHmgYaReFEtU2.SvKi', 'https://my-buket-xinandiqu.oss-cn-chengdu.aliyuncs.com/avatar/avatarDefualt.jpg', 'ekingdoral@gmail.com', 15328828191, '?????', 0, 1, 1, 1, 1, 0);
INSERT INTO `usertable`(`uno`, `username`, `password`, `avatar`, `email`, `phone`, `realname`, `isadmin`, `is_account_non_expired`, `is_account_non_locked`, `is_credentials_non_expired`, `is_enabled`, `isdelete`) VALUES (2, 'arno', '$2a$10$CDGnjZfCttqfzfoK8i0sI.4LOgaw18SuM/K6z8XiRXebXwJOkkWX.', NULL, 'null', NULL, '亚诺', 1, 1, 1, 1, 1, 1);
INSERT INTO `usertable`(`uno`, `username`, `password`, `avatar`, `email`, `phone`, `realname`, `isadmin`, `is_account_non_expired`, `is_account_non_locked`, `is_credentials_non_expired`, `is_enabled`, `isdelete`) VALUES (3, 'huke', '$2a$10$CDGnjZfCttqfzfoK8i0sI.4LOgaw18SuM/K6z8XiRXebXwJOkkWX.', NULL, 'null', 15328828191, '虎克', 0, 1, 1, 1, 1, 1);
INSERT INTO `usertable`(`uno`, `username`, `password`, `avatar`, `email`, `phone`, `realname`, `isadmin`, `is_account_non_expired`, `is_account_non_locked`, `is_credentials_non_expired`, `is_enabled`, `isdelete`) VALUES (9, 'test', '$2a$10$/PYFaC4.yyq6IxC.PjXulO0hYb9plG1DAfLKeBhGQnorsYDxayptO', 'http://my-buket-xinandiqu.oss-cn-chengdu.aliyuncs.com/avatar/2023/12/02/24d838e95428497398c32f01f4fdc869.jpg', '937858146@qq.com', 15328828191, '123', 0, 1, 1, 1, 1, 0);


INSERT INTO `menutable`(`mno`, `label`, `pid`, `pname`, `name`, `url`, `path`, `icon`, `code`, `type`, `isdelete`) VALUES (1, '系统管理', 0, '顶级菜单', 'system', '/system/system', '/system', 'el-icon-menu', 'system:admin', 0, 1);
INSERT INTO `menutable`(`mno`, `label`, `pid`, `pname`, `name`, `url`, `path`, `icon`, `code`, `type`, `isdelete`) VALUES (2, '角色管理', 1, '系统管理', 'roleList', '/system/role/roleList', '/role/roleList', 'el-icon-s-custom', 'system:role', 1, 1);
INSERT INTO `menutable`(`mno`, `label`, `pid`, `pname`, `name`, `url`, `path`, `icon`, `code`, `type`, `isdelete`) VALUES (3, '菜单管理', 1, '系统管理', 'menuList', '/system/menu/menuList', '/menu/menuList', 'el-icon-tickets', 'system:menu', 1, 1);
INSERT INTO `menutable`(`mno`, `label`, `pid`, `pname`, `name`, `url`, `path`, `icon`, `code`, `type`, `isdelete`) VALUES (4, '用户管理', 1, '系统管理', 'userList', '/system/user/userList', '/user/userList', 'el-icon-user', 'system:user', 1, 1);
INSERT INTO `menutable`(`mno`, `label`, `pid`, `pname`, `name`, `url`, `path`, `icon`, `code`, `type`, `isdelete`) VALUES (5, '新增角色', 2, '角色管理', NULL, NULL, NULL, 'el-icon-plus', 'system:role:add', 2, 0);
INSERT INTO `menutable`(`mno`, `label`, `pid`, `pname`, `name`, `url`, `path`, `icon`, `code`, `type`, `isdelete`) VALUES (6, '删除角色', 2, '角色管理', NULL, '', NULL, 'el-icon-delete', 'system:role:delete', 2, 1);
INSERT INTO `menutable`(`mno`, `label`, `pid`, `pname`, `name`, `url`, `path`, `icon`, `code`, `type`, `isdelete`) VALUES (7, '修改角色', 2, '角色管理', NULL, NULL, NULL, 'el-icon-edit', 'system:role:update', 2, 1);
INSERT INTO `menutable`(`mno`, `label`, `pid`, `pname`, `name`, `url`, `path`, `icon`, `code`, `type`, `isdelete`) VALUES (8, '增加菜单', 3, '菜单管理', NULL, NULL, NULL, 'el-icon-plus', 'system:menu:add', 2, 1);
INSERT INTO `menutable`(`mno`, `label`, `pid`, `pname`, `name`, `url`, `path`, `icon`, `code`, `type`, `isdelete`) VALUES (9, '删除菜单', 3, '菜单管理', NULL, NULL, NULL, 'el-icon-delete', 'system:menu:delete', 2, 1);
INSERT INTO `menutable`(`mno`, `label`, `pid`, `pname`, `name`, `url`, `path`, `icon`, `code`, `type`, `isdelete`) VALUES (10, '修改菜单', 3, '菜单管理', NULL, NULL, NULL, 'el-icon-edit', 'system:menu:update', 2, 1);
INSERT INTO `menutable`(`mno`, `label`, `pid`, `pname`, `name`, `url`, `path`, `icon`, `code`, `type`, `isdelete`) VALUES (11, '增加用户', 4, '用户管理', NULL, NULL, NULL, 'el-icon-plus', 'system:user:add', 2, 1);
INSERT INTO `menutable`(`mno`, `label`, `pid`, `pname`, `name`, `url`, `path`, `icon`, `code`, `type`, `isdelete`) VALUES (12, '删除用户', 4, '用户管理', NULL, NULL, NULL, 'el-icon-delete', 'system:user:delete', 2, 1);
INSERT INTO `menutable`(`mno`, `label`, `pid`, `pname`, `name`, `url`, `path`, `icon`, `code`, `type`, `isdelete`) VALUES (13, '修改用户', 4, '用户管理', NULL, NULL, NULL, 'el-icon-edit', 'system:user:update', 2, 1);
INSERT INTO `menutable`(`mno`, `label`, `pid`, `pname`, `name`, `url`, `path`, `icon`, `code`, `type`, `isdelete`) VALUES (14, '学习平台', 0, '顶级菜单', 'platform', '/platform/platform', '/platform', 'el-icon-monitor', 'platform', 0, 1);
INSERT INTO `menutable`(`mno`, `label`, `pid`, `pname`, `name`, `url`, `path`, `icon`, `code`, `type`, `isdelete`) VALUES (15, '首页', 14, '学习平台', 'home', '/platform/home/homePage', '/home/home', 'el-icon-copy-document', 'platform:home', 1, 1);
INSERT INTO `menutable`(`mno`, `label`, `pid`, `pname`, `name`, `url`, `path`, `icon`, `code`, `type`, `isdelete`) VALUES (16, '智能技术', 14, '学习平台', 'intelligence', '/platform/intelligence/intelligencePage', '/intelligence/intelligence', 'el-icon-news', 'platform:intelligence', 1, 1);
INSERT INTO `menutable`(`mno`, `label`, `pid`, `pname`, `name`, `url`, `path`, `icon`, `code`, `type`, `isdelete`) VALUES (17, '算法模型', 14, '学习平台', 'algorithm', '/platform/algorithm/algorithmPage', '/algorithm/algorithm', 'el-icon-cpu', 'plateform:algorithm', 1, 1);
INSERT INTO `menutable`(`mno`, `label`, `pid`, `pname`, `name`, `url`, `path`, `icon`, `code`, `type`, `isdelete`) VALUES (18, '人才培养', 14, '学习平台', 'introduction', '/platform/introduction/introductionPage', '/introduction/introduction', 'el-icon-medal', 'platform:introduction', 1, 1);
INSERT INTO `menutable`(`mno`, `label`, `pid`, `pname`, `name`, `url`, `path`, `icon`, `code`, `type`, `isdelete`) VALUES (19, '个人主页', 14, '学习平台', 'personal', '/platform/personal/personalPage', '/personal/personal', 'el-icon-edit-outline', 'platform:personal', 1, 1);
INSERT INTO `menutable`(`mno`, `label`, `pid`, `pname`, `name`, `url`, `path`, `icon`, `code`, `type`, `isdelete`) VALUES (20, '算法管理', 1, '系统管理', 'alg', '/system/alg/algList', '/alg/alg/List', 'el-icon-reading', 'system:alg', 1, 1);
INSERT INTO `menutable`(`mno`, `label`, `pid`, `pname`, `name`, `url`, `path`, `icon`, `code`, `type`, `isdelete`) VALUES (21, '文章管理', 1, '系统管理', 'article', '/system/article/articleList', '/article/articleList', 'el-icon-document', 'system:article', 1, 1);


INSERT INTO `roletable`(`rno`, `rolename`, `rolecode`, `remark`, `createuser`) VALUES (1, '系统管理员', 'ADMIN_SYSTEM', NULL, 1);
INSERT INTO `roletable`(`rno`, `rolename`, `rolecode`, `remark`, `createuser`) VALUES (2, '角色管理员', 'ADMIN_ROLE', NULL, 1);
INSERT INTO `roletable`(`rno`, `rolename`, `rolecode`, `remark`, `createuser`) VALUES (3, '普通用户', 'NORMAL', NULL, 1);
INSERT INTO `roletable`(`rno`, `rolename`, `rolecode`, `remark`, `createuser`) VALUES (4, '菜单管理员', 'ADMIN_MENU', NULL, 1);
INSERT INTO `roletable`(`rno`, `rolename`, `rolecode`, `remark`, `createuser`) VALUES (5, '用户管理员', 'ADMIN_USER', NULL, 1);


INSERT INTO `ur`(`uno`, `rno`) VALUES (1, 1);
INSERT INTO `ur`(`uno`, `rno`) VALUES (1, 3);
INSERT INTO `ur`(`uno`, `rno`) VALUES (2, 4);
INSERT INTO `ur`(`uno`, `rno`) VALUES (3, 3);
INSERT INTO `ur`(`uno`, `rno`) VALUES (3, 4);
INSERT INTO `ur`(`uno`, `rno`) VALUES (9, 3);


INSERT INTO `mr`(`mno`, `rno`) VALUES (1, 1);
INSERT INTO `mr`(`mno`, `rno`) VALUES (2, 1);
INSERT INTO `mr`(`mno`, `rno`) VALUES (3, 1);
INSERT INTO `mr`(`mno`, `rno`) VALUES (4, 1);
INSERT INTO `mr`(`mno`, `rno`) VALUES (5, 1);
INSERT INTO `mr`(`mno`, `rno`) VALUES (6, 1);
INSERT INTO `mr`(`mno`, `rno`) VALUES (7, 1);
INSERT INTO `mr`(`mno`, `rno`) VALUES (8, 1);
INSERT INTO `mr`(`mno`, `rno`) VALUES (9, 1);
INSERT INTO `mr`(`mno`, `rno`) VALUES (10, 1);
INSERT INTO `mr`(`mno`, `rno`) VALUES (11, 1);
INSERT INTO `mr`(`mno`, `rno`) VALUES (12, 1);
INSERT INTO `mr`(`mno`, `rno`) VALUES (13, 1);
INSERT INTO `mr`(`mno`, `rno`) VALUES (20, 1);
INSERT INTO `mr`(`mno`, `rno`) VALUES (21, 1);
INSERT INTO `mr`(`mno`, `rno`) VALUES (2, 2);
INSERT INTO `mr`(`mno`, `rno`) VALUES (14, 3);
INSERT INTO `mr`(`mno`, `rno`) VALUES (15, 3);
INSERT INTO `mr`(`mno`, `rno`) VALUES (16, 3);
INSERT INTO `mr`(`mno`, `rno`) VALUES (17, 3);
INSERT INTO `mr`(`mno`, `rno`) VALUES (18, 3);
INSERT INTO `mr`(`mno`, `rno`) VALUES (19, 3);
INSERT INTO `mr`(`mno`, `rno`) VALUES (3, 4);
INSERT INTO `mr`(`mno`, `rno`) VALUES (4, 5);


INSERT INTO `likes`(`id`, `uno`, `date`) VALUES (2, 1, NULL);


INSERT INTO `algorithmtable`(`ano`, `algname`, `remark`, `createuser`, `createuno`, `ispermit`, `algtype`, `context`, `lang`) VALUES (1, 'KNN', 'k??????', 'admin', 1, 1, 1, '    @DeleteMapping(\"/delete/{id}\")\n    public Result delete(@PathVariable Long id){\n        if(algService.deleteAlgById(id)){\n            return Result.ok().message(\"??????\");\n        }else {\n            return Result.e;\n        }\n    }\n\n    @GetMapping(\"/context/{id}\")\n    public Result context(@PathVariable Long id)\n        String context = algService.fintContextByAno(id);\n        return Result.ok(context);\n    }', 'java');
INSERT INTO `algorithmtable`(`ano`, `algname`, `remark`, `createuser`, `createuno`, `ispermit`, `algtype`, `context`, `lang`) VALUES (2, 'test', 'test', 'arno', 2, 1, 1, '123', NULL);
INSERT INTO `algorithmtable`(`ano`, `algname`, `remark`, `createuser`, `createuno`, `ispermit`, `algtype`, `context`, `lang`) VALUES (3, 'test_1', '1', 'admin', 1, 0, 1, '456', NULL);


INSERT INTO `article`(`id`, `title`, `author`, `content`, `display_time`, `content_short`, `comment_disable`, `status`, `score`) VALUES (1, 'test', 'admin', '<p>test</p>\n<p><img class=\"wscnph\" src=\"https://my-buket-xinandiqu.oss-cn-chengdu.aliyuncs.com/pic/2023/12/02/6169bd218ea6475ca8c20afb274f596b.jpg\" width=\"400\" height=\"\" /></p>\n<pre class=\"language-java\"><code>@Data\n@TableName(\"article\")\npublic class Article {\n    private String title;\n    private String author;\n    private String content;\n    private Date display_time; //????\n    private String content_short; //??\n    private Long id;\n    private int comment_disable; //????\n    private Long score; //??\n    private String status; //??\n\n}</code></pre>', '2023-12-02', 'test1', 0, 'draft', 21);
INSERT INTO `article`(`id`, `title`, `author`, `content`, `display_time`, `content_short`, `comment_disable`, `status`, `score`) VALUES (2, 'test2', 'admin', '<p>test2</p>', '2023-12-02', 'test222', 0, 'enable', 25);


