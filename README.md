Encrypt 第二版（进行中）
====


注意
-----
该版已废弃，请移步组件化版
https://github.com/CMonoceros/Encrypt-Client-Componentize

软件目的
------

利用服务器运算速度，进行远程文件加密，下载传输过程安全且下载后可以用于安全传输。<br />

总体功能
------

用户能够注册，并且登录。登录成功后，可以上传文件，获取文件列表。选择具体文件后，可以获取所支持的加密方法及加密介绍，并进行服务器加密，下载到本地。下载成功后可以进行本地解密。<br />

概念模型
------

![image](https://github.com/CMonoceros/Encrypt-Client-MVP/raw/master/screenshot/usecase.jpg)

系统设计
------

将整体分为，用户模块，文件模块，加密方式模块，加密关系模块，每个模块所对应的基本功能如下：<br />
1.用户模块：登录，注册<br />
2.文件模块：上传，下载，获取文件列表<br />
3.加密方式模块：获取加密方式<br />
4.加密关系模块：加密，解密<br />

客户端为MVP模式。<br />
View中应包含Activity基类，基本用户交互方法接口定义，具体模块Activity实现类。<br />
Presenter中应包含具体模块注入的组件接口，网络操作相关方法接口定义，网络操作相关方法具体实现类<br />

系统实现
------

针对用户模块：<br />
View中包含BaseActivity基类。Contract.View，Contract接口中定义基本交互方法的接口。Activity，继承BaseActivity，实现Contract.View，负责用户交互的实现类。<br />
Presenter中包含Component，被Activity中injectDependences方法初始化，通过Component注解定义的组件接口。BaseRepository，网络操作相关方法的RxJava格式的方法定义接口。Repository，实现BaseRepository，通过ApiService初始化，网络操作相关方法具体实现类。BaseUseCase，基于该模块模型，网络操作相关的方法定义接口。UseCase，实现BaseUseCase，通过BaseRepository初始化，可以获取模型，设定模型的网络操作相关方法的具体实现。Contract.Presenter，Contract接口中定义基本逻辑操作方法的接口。Presenter，实现Contract.Presenter，通过RxJava进行主要异步耗时逻辑操作的主持人类。Module，通过BaseRepository初始化，通过Provides注解注入UseCase及Presenter，通过Module注解定义的模块类。<br />
Model中包含通过Expose注解过滤相关属性，对应所属模块的序列化对象。<br />
WebApi中包含ApiService，Retrofit2注解定义的网络接口。Interceptor，Cache，Cookie，HttpLog相关拦截器。<br />
Server为J2EE服务器。<br />

![image](https://github.com/CMonoceros/Encrypt-Client-MVP/raw/master/screenshot/client_mvp.jpg)


其它
------
其他内容，包括相关方法接口，见doc文件夹下开发报告



