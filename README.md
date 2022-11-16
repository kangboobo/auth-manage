# authority-manage
# 权限管理平台

## 项目简介
对用户权限进行统一管理，支持将用户划分到用户组，然后针对用户组设置角色，
再给角色赋予从应用->基地->菜单->按钮->数据等维度的权限
> 此项目集成第三方的swagger，美化文档样式。
> 
> 集成：[swagger-spring-boot-starter](https://github.com/battcn/swagger-spring-boot) 
>
> 启动项目，访问地址swagger：http://localhost:30001/swagger-ui.html#/

## 开发环境
- **JDK 1.8 +**
- **Maven 3.5 +**
- **IntelliJ IDEA ULTIMATE 2020.3 +** (*注意：务必使用 IDEA 开发，同时保证安装 `lombok` 插件*)

## 各 Module 介绍
| Module 名称                                                  | Module 介绍                                                  |
| ------------------------------------------------------------ | ------------------------------------------------------------ |
| [auth-manage-common](./auth-manage-common) | 公共模块，保护公共组件、常量、工具类等                              |
| [auth-manage-dao](./auth-manage-dao) | 数据访问模块                        |
| [auth-manage-facade](./auth-manage-facade)     | rpc接口模块，提供给外部系统依赖 |
| [auth-manage-service](./auth-manage-service) | 业务实现模块 |
