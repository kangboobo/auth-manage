create table sys_app
(
    id           bigserial
        constraint sys_app_pk
            primary key,
    app_code     varchar(32),
    app_name     varchar(64)        not null,
    factory_code varchar(32),
    remark       varchar(256),
    delete_flag  smallint default 0 not null,
    create_user  varchar(64)        not null,
    create_time  bigint             not null,
    update_user  varchar(64),
    update_time  bigint
);

comment on table sys_app is '系统应用表';

comment on column sys_app.id is '系统应用表主键';

comment on column sys_app.app_code is '应用编码';

comment on column sys_app.app_name is '应用名称';

comment on column sys_app.factory_code is '工厂代码';

comment on column sys_app.remark is '备注信息';

comment on column sys_app.delete_flag is '逻辑删除 0:未删除 1:删除';

comment on column sys_app.create_user is '创建人';

comment on column sys_app.create_time is '创建事件';

comment on column sys_app.update_user is '修改人';

comment on column sys_app.update_time is '修改时间';

alter table sys_app
    owner to postgres;

create table sys_base
(
    id           bigserial
        constraint sys_base_pk
            primary key,
    base_code    varchar(32),
    base_name    varchar(64)        not null,
    factory_code varchar(32),
    area         varchar(32),
    description  varchar(512),
    remark       varchar(256),
    delete_flag  smallint default 0 not null,
    create_user  varchar(64)        not null,
    create_time  bigint             not null,
    update_user  varchar(64),
    update_time  bigint
);

comment on table sys_base is '系统应用表';

comment on column sys_base.id is '系统应用表主键';

comment on column sys_base.base_code is '基地编码';

comment on column sys_base.base_name is '基地名称';

comment on column sys_base.factory_code is '工厂代码';

comment on column sys_base.area is '基地面积';

comment on column sys_base.description is '基地介绍';

comment on column sys_base.remark is '备注信息';

comment on column sys_base.delete_flag is '逻辑删除 0:未删除 1:删除';

comment on column sys_base.create_user is '创建人';

comment on column sys_base.create_time is '创建时间';

comment on column sys_base.update_user is '修改人';

comment on column sys_base.update_time is '修改时间';

alter table sys_base
    owner to postgres;

create table sys_app_base
(
    id      bigserial
        constraint sys_app_base_pk
            primary key,
    app_id  bigint not null,
    base_id bigint not null
);

comment on table sys_app_base is '应用-基地关联表';

comment on column sys_app_base.id is '应用-基地关联表主键';

comment on column sys_app_base.app_id is '应用表id';

comment on column sys_app_base.base_id is '基地表id';

alter table sys_app_base
    owner to postgres;

create table sys_menu
(
    id          bigserial
        constraint sys_menu_pk
            primary key,
    app_id      bigint             not null,
    menu_name   varchar(64)        not null,
    parent_id   bigint,
    order_num   integer,
    path        varchar(32)        not null,
    component   varchar(32),
    query       varchar(32),
    is_frame    varchar(16),
    menu_type   varchar(16)        not null,
    visible     varchar(16),
    perms       varchar(256),
    remark      varchar(256),
    delete_flag smallint default 0 not null,
    create_user varchar(64)        not null,
    create_time bigint             not null,
    update_user varchar(64),
    update_time bigint,
    icon        varchar(16)
);

comment on table sys_menu is '系统菜单表';

comment on column sys_menu.id is '菜单表主键';

comment on column sys_menu.app_id is '应用id';

comment on column sys_menu.menu_name is '菜单名称';

comment on column sys_menu.parent_id is '父菜单id';

comment on column sys_menu.order_num is '菜单顺序';

comment on column sys_menu.path is '路由地址';

comment on column sys_menu.component is '组件路径';

comment on column sys_menu.query is '路由参数';

comment on column sys_menu.is_frame is '是否为外链';

comment on column sys_menu.menu_type is 'M:目录，C:菜单 F:按钮';

comment on column sys_menu.visible is '显示状态';

comment on column sys_menu.perms is '权限字符串';

comment on column sys_menu.remark is '备注信息';

comment on column sys_menu.delete_flag is '逻辑删除 0:未删除 1:已删除';

comment on column sys_menu.create_user is '创建人';

comment on column sys_menu.create_time is '创建时间';

comment on column sys_menu.update_user is '修改人';

comment on column sys_menu.update_time is '修改时间';

comment on column sys_menu.icon is '菜单图标';

alter table sys_menu
    owner to postgres;

create table sys_role
(
    id          bigserial
        constraint sys_role_pk
            primary key,
    app_id      bigint             not null,
    base_id_str varchar(256),
    role_name   varchar(64)        not null,
    role_key    varchar(32),
    role_sort   integer,
    data_scope  varchar(16),
    remark      varchar(256),
    delete_flag smallint default 0 not null,
    create_user varchar(64)        not null,
    create_time bigint             not null,
    update_user varchar(64),
    update_time bigint
);

comment on table sys_role is '系统角色表';

comment on column sys_role.id is '系统角色表主键id';

comment on column sys_role.app_id is '应用id';

comment on column sys_role.base_id_str is '基地ids';

comment on column sys_role.role_name is '角色名称';

comment on column sys_role.role_key is '角色权限';

comment on column sys_role.role_sort is '角色顺序';

comment on column sys_role.data_scope is '数据范围';

comment on column sys_role.remark is '备注信息';

comment on column sys_role.delete_flag is '逻辑删除 0:未删除 1:已删除';

comment on column sys_role.create_user is '创建人';

comment on column sys_role.create_time is '创建时间';

comment on column sys_role.update_user is '修改人';

comment on column sys_role.update_time is '修改时间';

alter table sys_role
    owner to postgres;

create table sys_role_menu
(
    id      bigserial
        constraint sys_role_menu_pk
            primary key,
    role_id bigint not null,
    menu_id bigint not null
);

comment on table sys_role_menu is '角色-菜单关联表';

comment on column sys_role_menu.id is '角色-菜单关联表主键';

comment on column sys_role_menu.role_id is '角色id';

comment on column sys_role_menu.menu_id is '菜单表id';

alter table sys_role_menu
    owner to postgres;

create table sys_user_group
(
    id              bigserial
        constraint sys_user_group_pk
            primary key,
    app_id          bigint             not null,
    base_id_str     varchar(256),
    user_group_name varchar(64)        not null,
    remark          varchar(256),
    create_user     varchar(64)        not null,
    create_time     bigint             not null,
    update_user     varchar(64),
    update_time     bigint,
    delete_flag     smallint default 0 not null
);

comment on table sys_user_group is '系统用户组表';

comment on column sys_user_group.id is '用户组主键id';

comment on column sys_user_group.app_id is '应用id';

comment on column sys_user_group.base_id_str is '基地ids';

comment on column sys_user_group.user_group_name is '用户组名称';

comment on column sys_user_group.remark is '备注信息';

comment on column sys_user_group.create_user is '创建人';

comment on column sys_user_group.create_time is '创建时间';

comment on column sys_user_group.update_user is '修改人';

comment on column sys_user_group.update_time is '修改时间';

comment on column sys_user_group.delete_flag is '逻辑删除 0:未删除 1:已删除';

alter table sys_user_group
    owner to postgres;

create table sys_user_group_role
(
    id            bigserial
        constraint sys_user_group_role_pk
            primary key,
    user_group_id bigint not null,
    role_id       bigint not null
);

comment on table sys_user_group_role is '用户组-角色关联表';

comment on column sys_user_group_role.id is '用户组-角色关联表主键';

comment on column sys_user_group_role.user_group_id is '用户组io';

comment on column sys_user_group_role.role_id is '角色id';

alter table sys_user_group_role
    owner to postgres;

create table sys_user_group_user
(
    id            bigserial
        constraint sys_user_group_user_pk
            primary key,
    user_group_id bigint      not null,
    user_id       varchar(64) not null
);

comment on table sys_user_group_user is '用户组-用户关联表';

comment on column sys_user_group_user.id is '用户组用户关联表主键';

comment on column sys_user_group_user.user_group_id is '用户组id';

comment on column sys_user_group_user.user_id is '用户id';

alter table sys_user_group_user
    owner to postgres;


