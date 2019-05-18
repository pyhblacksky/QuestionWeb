# QuestionWeb
简易问答网站后端

# 项目构建

使用Spring Boot + MyBatis 构建 JavaWeb项目

##### 项目实现了以下功能：

1. 用户登录，注册
2. 问题发布，敏感词过滤
3. 评论中心，站内信功能
4. Redis实现赞踩功能
5. 异步设计站内邮件通知系统
6. 关注和粉丝列表页实现
7. 推拉模式拉取问题



#### 后端技术选型

|    选型     |     说明     |
| :---------: | :----------: |
| Spring Boot | 容器+MVC框架 |
|   MyBatis   |   ORM框架    |
|    Redis    |     缓存     |
|    MySql    |   数据存储   |

#### 项目结构

```
com.nowcoder
├── aspect -- 切面
├── async -- 异步处理
	└── handler -- 功能异步处理器存放包
├── configuration -- 设置
├── controller -- 控制层
├── DAO -- 数据层
├── interceptor -- 拦截器
├── model -- 模型层
├── service -- 服务层
└── util -- 工具类
```



#### 数据库相关表及字段

| 表名         | 字段                                                         | 说明     |
| ------------ | ------------------------------------------------------------ | -------- |
| user         | id,name,password,salt,head_url                               | 用户表   |
| question     | id,title,content,user_id,created_date,comment_count          | 问题表   |
| message      | id,from_id,to_id,content,created_date,has_read,conversation_id | 站内消息 |
| login_ticket | id,user_id,ticket,expired,status                             | 登录凭证 |
| feed         | id,created_date,user_id,data,type                            | feed流   |
| comment      | id,content,user_id,entity_id,entity_type,created_date,status | 评论表   |

#### 尚未实现的部分

1. 管理员后台管理
2. 用户注册使用邮件激活
3. 推荐问题置顶
4. timeline推拉结合
5. 个性化首页等业务

多商品、多库存、多活动模型尚未实现



#### 存在的问题

如何发现容量问题

如何使系统水平扩展

查询效率低下

库存行锁问题

浪涌流量问题如何解决

搜索结果的排序打分
