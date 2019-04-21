# spring-boot-mybatis

该 demo 主要演示以下功能：

- 使用 MyBatis-Plus 完成基本的增删查改
- 分别使用 MyBatis-Plus 和 Pagehelper 完成分页查询
- 手写 Mapper 完成批量插入、删除和更新功能，同时新增 Java 8 函数式编程的方法封装分页批量操作（增删改），应付海量数据导致 SQL 语句过长的问题
- 使用 `@Transactional` 注解实现事务功能（针对分页批量增删改）
- 使用 Alibaba Druid 监控数据源
- 使用 @Validated 相关注解进行 Controller 入参校验
- 增加全局异常处理类



**在使用二级缓存前一定要认真考虑脏数据对系统的影响！！！**

MyBatis 的缓存有两级，一级缓存基于 SqlSession，二级缓存基于 SqlSessionFactory，如果开启了二级缓存，一级缓存结束后，就会缓存到二级。

读写缓存：不是同一实例，使用序列化来缓存。
只读缓存：同一实例，使用 Map 来缓存。
这个读写和只读跟能不能修改没有关系，只是区别是否是同一实例

Redis缓存的特点：

1. 支持数据结构多
2. 支持持久化
3. 支持分布式缓存

EhCache缓存的特点：

1. 缓存数据有内存和硬盘两级
2. 缓存数据在虚拟机重启的过程中自动写入到硬盘
3. 通过RMI支持分布式缓存，但是不方便