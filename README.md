# 项目名称
用户权限系统

# 项目简介

用户权限管理系统是一个用于管理用户账户和权限的应用程序。它提供了管理员对用户进行增删改查操作，并允许用户登录、注册、退出登录以及修改个人信息。该系统旨在帮助组织或网站管理员轻松地管理用户账户和权限，从而有效地管理用户操作。

# 功能特点

- ​	管理员功能
  - 增加用户：管理员可以创建新用户并为其分配相应的角色和权限。
  - 删除用户：管理员有权删除不再需要的用户账户。
  - 修改用户信息：管理员可以编辑用户账户的相关信息，如用户名、密码等。
  - 查询用户信息：管理员可以通过关键字搜索用户信息，以便快速找到所需信息。

- ​	用户功能
  - 用户注册：新用户可以注册账户，并填写相关个人信息。
  - 用户登录：已注册用户可以使用自己的账号和密码登录系统。
  - 退出登录：用户可以安全地退出当前登录的账号。
  - 修改个人信息：已登录用户可以编辑和更新个人信息，如用户名、密码等。

# 技术选型

- 前端
  - HTML+CSS+JavaScript 三件套
  - React 开发框架
  - 组件库 Ant Design
  - Umi 开发框架
  - Umi Request 开发框架
  - Ant Design Pro（现成的管理系统）
- 后端
  - java
  - spring（依赖注入框架，帮助你管理 Java 对象，集成一些其他的内容）
  - springmvc（web 框架，提供接口访问、restful接口等能力）
  - mybatis（Java 操作数据库的框架，持久层框架，对 jdbc 的封装）
  - mybatis-plus（对 mybatis 的增强，不用写 sql 也能实现增删改查）
  - springboot（快速启动 / 快速集成项目。不用自己管理 spring 配置，不用自己整合各种框架）
  - junit 单元测试库
  - mysql 数据库

# 项目展示

- **登录页**

![image-20230714174530787](https://img.enndfp.cn/image-20230714174530787.png)

- **注册页**

![image-20230714174612930](https://img.enndfp.cn/image-20230714174612930.png)

- **首页**

![image-20230714174735848](https://img.enndfp.cn/image-20230714174735848.png)

- **用户管理页**

![image-20230714174844930](https://img.enndfp.cn/image-20230714174844930.png)



![image-20230714174924758](https://img.enndfp.cn/image-20230714174924758.png)

- **新增用户页**

![image-20230714175304516](https://img.enndfp.cn/image-20230714175304516.png)

- **用户信息页**

![image-20230714175412983](https://img.enndfp.cn/image-20230714175412983.png)

- **修改页**

![image-20230714175541944](https://img.enndfp.cn/image-20230714175541944.png)



![image-20230714175611148](https://img.enndfp.cn/image-20230714175611148.png)



![image-20230714175630647](https://img.enndfp.cn/image-20230714175630647.png)

# 项目启动

## 前端

1.克隆前端项目到本地：

```bash
git clone https://github.com/Enndfp/user-rights-frontend.git
```

2.进入前端项目目录：

```bash
cd user-rights-frontend
```

3.安装依赖：

```bash
npm install
```

4.启动项目：

```bash
npm start
```

5.部署：

```bash
npm build
```

执行命令后会得到 dist 目录，可以放到自己的 web 服务器指定的路径；也可以使用 Docker 容器部署，将 dist、Dockerfile、docker 目录（文件）一起打包即可。

## 后端

1.克隆后端项目到本地：

```bash
git clone https://github.com/Enndfp/user-rights-backend.git
```

2.进入后端项目目录：

```bash
cd user-rights-backend
```

3.配置数据库：

- 根据sql目录中的**create_table.sq**l文件，建立本地sql表
- 修改application.yml中的数据库连接信息

# 知识补充

### 1.Node.js

- [Node.js官网](https://nodejs.org/zh-cn) 	  
- [Node.js安装](https://blog.csdn.net/weixin_44893902/article/details/121788104?ops_request_misc=%257B%2522request%255Fid%2522%253A%2522168830138516800222823395%2522%252C%2522scm%2522%253A%252220140713.130102334..%2522%257D&request_id=168830138516800222823395&biz_id=0&utm_medium=distribute.pc_search_result.none-task-blog-2~all~top_positive~default-1-121788104-null-null.142^v88^koosearch_v1,239^v2^insert_chatgpt&utm_term=nodejs%E5%AE%89%E8%A3%85%E5%8F%8A%E7%8E%AF%E5%A2%83%E9%85%8D%E7%BD%AE&spm=1018.2226.3001.4187)

在 Node.js 之前，JavaScript 只能运行在浏览器中，作为网页脚本使用，为网页添加一些特效，或者和服务器进行通信。有了 Node.js 以后，JavaScript 就可以脱离浏览器，像其它编程语言一样直接在计算机上使用，想干什么就干什么，再也不受浏览器的限制了。

Node.js 不是一门新的编程语言，也不是一个 JavaScript 框架，它是一套 JavaScript 运行环境，用来支持 JavaScript 代码的执行。用编程术语来讲，Node.js 是一个 JavaScript 运行时（Runtime）。

### 2.npm

npm 是 [Node.js](https://nodejs.org/zh-cn) 的包管理工具，用来安装各种 Node.js 的扩展。

npm 是 JavaScript 的包管理工具，也是世界上最大的软件注册表。有超过 60 万个 JavaScript 代码包可供下载，每周下载约 30 亿次。npm 让 JavaScript 开发人员可以轻松地使用其他开发人员共享的代码。

### 3.yarn

- [yarn官网](https://yarnpkg.com/)	    
- [yarn安装](https://blog.csdn.net/weixin_40808668/article/details/122606543?ops_request_misc=%257B%2522request%255Fid%2522%253A%2522168830157416800184147656%2522%252C%2522scm%2522%253A%252220140713.130102334..%2522%257D&request_id=168830157416800184147656&biz_id=0&utm_medium=distribute.pc_search_result.none-task-blog-2~all~top_positive~default-1-122606543-null-null.142^v88^koosearch_v1,239^v2^insert_chatgpt&utm_term=yarn%E5%AE%89%E8%A3%85&spm=1018.2226.3001.4187)

Yarn是facebook发布的一款取代npm的包管理工具。

### 3. Spring Boot

Spring Boot 是一个用于简化创建独立的、基于生产级别的 Spring 应用程序的框架。它通过提供默认的配置和约定大于配置的方式，简化了 Spring 应用程序的开发过程，减少了开发者的配置工作。

Spring Boot 使用了一种名为 "约定优于配置" 的方式，它提供了一些默认配置和常用的依赖项，可以快速启动和运行应用程序。Spring Boot 还提供了自动配置和快速集成的能力，可以方便地集成其他常用框架和技术。

### 4. MyBatis 和 MyBatis-Plus

- [MyBatis 官网](https://mybatis.org/mybatis-3/zh/index.html)
- [MyBatis-Plus 官网](https://mp.baomidou.com/)

MyBatis 是一个持久层框架，用于将 Java 对象与数据库之间进行映射。它提供了简单且强大的 SQL 映射功能，可以通过 XML 配置或注解来实现对象与数据库表的映射关系，从而实现对数据库的增删改查操作。

MyBatis-Plus 是对 MyBatis 的增强，它提供了更加便捷的操作方法和功能。MyBatis-Plus 可以自动生成常用的增删改查 SQL，大大简化了编码工作。它还提供了分页查询、条件构造器、乐观锁、性能分析等功能，提高了开发效率和代码质量。

### 5. MySQL 数据库

MySQL 是一种开源的关系型数据库管理系统（RDBMS），被广泛应用于各种 Web 应用程序和软件开发项目。它具有稳定、高效、易用等特点，并支持大规模数据存储和高并发访问。

MySQL 使用 SQL（Structured Query Language）作为其查询语言，可以进行复杂的数据库操作，包括数据的增删改查、事务管理、用户权限控制等。

# 致谢

感谢使用影视作品分析应用程序！如有任何问题或反馈，请联系作者。

如果你喜欢这个项目，请给一个星星⭐支持并在GitHub上关注该项目。

欢迎访问 [GitHub 项目地址](https://github.com/Enndfp/user-rights-backend) 获取最新代码和更多信息。

作者：Enndfp

邮箱：enndfp@163.com





