# wenda
## 1. bug解决记录：
http://note.youdao.com/noteshare?id=881ae12313ee7f12ab8dd6ea95e0e5bc

## 2. 项目知识笔记：
http://note.youdao.com/noteshare?id=a33810ccbd453a392a077d51d5fa7e5b
## 3. 项目部署/更新linux步骤：

### 3.1. 从git中下载代码
```
cd /temp/wenda2

git pull origin master
```
### 3.2. 用maven打包

```
mvn package -Dmaven.test.skip=true
// 跳过测试
```
### 3.3.把war把移动到tomcat的根目录下
```
cp /temp/wenda2/target/wenda-0.0.1-SNAPSHOT.war /usr/local/tomcat8/webapps/wenda.war
```

### 3.4. 重启tomcat
```
/usr/local/tomcat8/bin/shutdown.sh
/usr/local/tomcat8/bin/startup.sh
tail -300f /usr/local/tomcat8/logs/catalina.out
```

## 4. 项目启动流程
### 4.1. 启动mysql
```
systemctl start mysql.service
```

```
netstat -anp|grep 3306
```
### 4.2. 启动tomcat
```
/usr/local/tomcat8/bin/startup.sh

/usr/local/tomcat8/bin/shutdown.sh

tail -300f /usr/local/tomcat8/logs/catalina.out
```
### 4.3. 启动redis
```
/temp/redis-3.2.1/src/redis-server
```
## 4.4. 启动solr
```
/temp/solr-7.6.0/bin/solr start -force -p 8983
/temp/solr-7.6.0/bin/solr stop -p 8983
/temp/solr-7.6.0/bin/solr restart -force -p 8983
```

### 4.5. 启动nginx

```
nginx

nginx -s reload

nginx -s stop
```

### 4.6. 启动ftp(如有必要)
```

systemctl stop vsftpd.service
systemctl start vsftpd.service
systemctl status vsftpd.service
systemctl restart vsftpd.service

```
## 4.7. 压力测试
需要先安装pd