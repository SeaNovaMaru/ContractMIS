## 简单介绍

* 前端采用Vue、Element UI。
* 后端采用Spring Boot、Spring Security、Redis & Jwt。



## 后端运行

1、导入到`Idea`，菜单 `File` -> `Import`，然后选择 `Maven` -> `Existing Maven Projects`，点击 `Next`> 按钮，选择工作目录，然后点击 `Finish` 按钮，即可成功导入。
`Eclipse`会自动加载`Maven`依赖包，初次加载会比较慢（根据自身网络情况而定）

2、创建数据库`ry-vue`并导入数据脚本`ry_2021xxxx.sql`，`quartz.sql`，`contract_231116.sql`, 按顺序执行！

3、打开项目运行`com.ruoyi.RuoYiApplication.java`，出现如下图表示启动成功。

4、后端启动时需要修改application.yml中的ruoyi.profile、application-druid中的数据库地址和密码及redis地址和密码，Mac/Linux用户需要修改logback.xml中配置的日志路径

``````
(♥◠‿◠)ﾉﾞ  若依启动成功   ლ(´ڡ`ლ)ﾞ  
 .-------.       ____     __        
 |  _ _   \      \   \   /  /    
 | ( ' )  |       \  _. /  '       
 |(_ o _) /        _( )_ .'         
 | (_,_).' __  ___(_ o _)'          
 |  |\ \  |  ||   |(_,_)'         
 |  | \ `'   /|   `-'  /           
 |  |  \    /  \      /           
 ''-'   `'-'    `-..-'    
``````



## 前端运行

```bash
# 进入项目目录
cd ruoyi-ui

# 安装依赖
npm install

# 强烈建议不要用直接使用 cnpm 安装，会有各种诡异的 bug，可以通过重新指定 registry 来解决 npm 安装速度慢的问题。
npm install --registry=https://registry.npmmirror.com

# 本地开发 启动项目
npm run dev
```

注意，启动项目时可能会出现 "95% emitting CompressionPlugin ERROR Error: error:0308010C:digital envelope routines::unsupported" 的错误通常与Node.js的版本和Webpack之间的兼容性问题有关。这个问题在Node.js 17及以上版本中较为常见，因为Node.js 17开始默认启用了OpenSSL 3，而某些旧版本的Webpack或其他依赖可能不支持这个版本的OpenSSL。

以下是解决这个问题的几种方法：

##### 1. 降级Node.js版本
降级到Node.js的一个较低版本，例如14.x或16.x，可以避免这个问题。这些版本的Node.js不使用OpenSSL 3，因此不会触发该错误。你可以使用[nvm](https://github.com/nvm-sh/nvm) (Node Version Manager) 在不同的Node.js版本之间轻松切换。

##### 2. 设置环境变量
作为临时解决方案，你可以通过设置环境变量`NODE_OPTIONS`来禁用OpenSSL 3的特定特性。在你的终端中运行以下命令，或将其添加到你的项目脚本中：

```sh
export NODE_OPTIONS=--openssl-legacy-provider
```

或者，对于Windows系统，在命令行中运行：

```cmd
set NODE_OPTIONS=--openssl-legacy-provider
```

然后再次尝试启动你的项目。
