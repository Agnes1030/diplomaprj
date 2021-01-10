# febs内容管理

#### 概要介绍
基于开源框架springBoot2.3+thymeleaf+springSecurity+lucene8.5.2 
是在FEBS-Security
权限管理脚手架上增加了cms内容管理和商城管理以及会员中心三个主要模块并集成lucene对商品全文检索,升级了springboot到了最新2.3版本,同时采用 Light Year Admin模板做为管理后台前端.

主要是一个农产品商城加旅游的项目


#### 功能模块
```
├── 网站个人中心管理
│   ├── 地址管理
│   ├── 我的收藏
│   ├── 我的订单
│   ├── 我的评论
│   └── 文章投递
├── 农产品商城后台管理
│   ├── 内容管理
│   │   ├── 分类管理
│   │   ├── 文章管理
│   │   └── 评论管理
│   ├── 商城管理
│   │   ├── 分类管理
│   │   ├── 商品管理
│   │   └── 订单管理
│   ├── 系统监控
│   │   ├── 在线用户
│   │   └── 系统日志
│   └── 系统管理
│       ├── 用户管理
│       ├── 菜单管理
│       └── 角色管理
└── 农产品商城旅游网站
    ├── 关于本站
    ├── 旅游信息
    └── 农产品商城 
        
```
### 开发环境
- 语言：Java 8
- IDE：Spring Tool Suite 4 
- 依赖管理：Maven
- 数据库：MySQL8.0
- 版本管理：git
## 模块说明
系统分为以下五个模块：
<table>
<tr>
	<th>模块</th>
	<th>说明</th>
</tr>
<tr>
	<td>febs-common</td>
	<td>基础模块，主要包含一些工具类，基础配置</td>
</tr>	
<tr>
	<td>febs-service</td>
	<td>数据库服务和全文检索服务模块，增删改查服务</td>
</tr>
<tr>
	<td>febs-quartz</td>
	<td>任务调度模块，处理定时任务</td>
</tr>
<tr>
	<td>febs-security</td>
	<td>安全模块，和安全有关的都在这个模块里</td>
</tr>
<tr>
	<td>febs-web</td>
	<td>web模块，包含前端部分和控制层</td>
</tr>
</table>


测试帐号:admin  123456

前台访问地址: http://localhost:8081/

个人中心登录: http://localhost:8081/member/login

后台登录: http://localhost:8081/admin/login

## 项目截图

首页：

<img src="/Users/agnesma/Desktop/diplomaprj/picture/front.png" alt="front" style="zoom: 50%;" />

商品详情：

![detail](/Users/agnesma/Desktop/diplomaprj/picture/detail.png)

分类页：

![category](/Users/agnesma/Desktop/diplomaprj/picture/category.png)

产品：

![products](/Users/agnesma/Desktop/diplomaprj/picture/products.png)

旅游

![tour](/Users/agnesma/Desktop/diplomaprj/picture/tour.png)

后台登录（动态背景mp4）

![login](/Users/agnesma/Desktop/diplomaprj/picture/login.png)

后台内容

![bk1](/Users/agnesma/Desktop/diplomaprj/picture/bk1.png)

![bk2](/Users/agnesma/Desktop/diplomaprj/picture/bk2.png)

## 启动方式  

1.IDE导入febs工程并进行maven安装依赖  

2.启动redis  

3.创建数据库febs(Character set:utf8,collation:utf8-bin)并导入脚本febs.sql  

4.修改application.yml数据库连接配置和redis连接配置  

5.运行febs-web工程下FebsApplication.java方法启动工程  

6.启动完成登录后台管理，商城管理->商品->重新索引 进行商品信息索引，前台才可以检索到

## 致谢

本项目基于或参考以下项目：

1. [febs-security](https://github.com/febsteam/FEBS-Security)  

   项目参考：以此项目为权限脚手架进行扩展

2. [jpress](https://gitee.com/fuhai/jpress)  
  
   项目参考：操作界面和功能逻辑

3. [ Light Year Admin](https://gitee.com/yinqi/Light-Year-Admin-Template)  

   项目参考：以此做为后台管理系统模板

3. [ litemall](https://gitee.com/linlinjava/litemall)  

   项目参考：部分商品和订单操作流程参考

