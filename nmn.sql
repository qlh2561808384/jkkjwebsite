create table nmn_user
(
    id int auto_increment comment '用户表主键id',
    email varchar(50) not null comment '用户用于登陆的用户名',
    password varchar(100) not null comment '密码',
    nickname varchar(100) null comment '昵称',
    status tinyint(1) default 0 null comment '用户状态：0表示有效用户，1表示无效用户',
    constraint nmn_user_pk
        primary key (id)
)
    comment '用户表';

create table nmn_role
(
    id int auto_increment comment '角色id',
    rolename varchar(20) not null comment '角色名称',
    status tinyint(1) default 0 null comment '角色状态：0有效1失效',
    description varchar(50) null comment '角色描述',
    constraint nmn_role_pk
        primary key (id)
)
    comment '角色表';
create table nmn_user_role
(
    id int auto_increment comment '用户角色关系表主键id',
    userid int null comment '用户表主键id',
    roleid int null comment '角色表主键id',
    constraint nmn_user_role_pk
        primary key (id)
)
    comment '用户角色关系表';

create table nmn_nmn
(
    id int auto_increment comment '商品主键id',
    title varchar(524) null comment '商品标题',
    summary varchar(1026) null comment '商品描述',
    view_num int(10) default 0 null comment '商品购买数量',
    price int null comment '商品价格，（单位/分）',
    create_time datetime null comment '创建时间',
    online tinyint(1) default 0 null comment '商品：0表示未上线，1表示上线',
    score double(11,2) default 8.70 null comment '商品评分：默认8.7，最高10分',
    constraint nmn_nmn_pk
        primary key (id)
)
    comment '商品表';

create table user_details
(
    id int auto_increment comment '主键id',
    user_id int null comment '用户id',
    phone varchar(50) null comment '用户手机电话',
    email varchar(50) null comment '用户邮箱',
    idcard varchar(50) null comment '用户身份证',
    constraint user_details_pk
        primary key (id)
)
    comment '用户详细信息表';

create table nmn_nmn_order
(
    id int auto_increment comment '主键id',
    out_trade_no varchar(64) null comment '订单唯一标识',
    state tinyint(1) null comment '订单状态，0表示未支付，1表示已支付',
    create_time datetime null comment '订单生成时间',
    notify_time datetime null comment '支付回调时间',
    total_fee int null comment '支付总金额，单位分',
    nmn_id int null comment 'nmn商品主键id',
    nmn_title varchar(128) null comment 'nmn商品标题',
    nmn_img varchar(256) null comment 'nmn商品图片',
    user_id int null comment '用户id',
    ip varchar(64) null comment '用户ip地址',
    del tinyint(1) null comment '0表示未删除，1表示已经删除',
    constraint nmn_nmn_order_pk
        primary key (id)
)
    comment '商品订单表';


