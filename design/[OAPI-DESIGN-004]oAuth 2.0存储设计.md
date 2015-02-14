#oAuth 2.0存储设计
 
 > 编号：OAPI-DESIGN-004
 > 作者：刘海龙
 > 微博：[http://weibo.com/liuhailong2008]
 > 博客：[http://blog.csdn.net/stationxp]


## 注册

### 用户注册

oauth_user(user_id,user_name,password,corp_name,contact,addr,tel,mobile,fax,website,email,status,timestamp).
oauth_resource_owner(ro_id,ro_name,contact,addr,tel,mobile,email,fax,status,timestamp).

### 客户端注册

客户端编号（client id）。
所属用户。
重定向 URI。
客户端类型。
应用名称、网址、描述、Logo 图片、接受法律条款等。

oauth_client(client_id,user_id,redirect_uri,client_secret,security_env,client_type,client_name,client_website,client_desc,logo_url,law_terms,status,timestamp).
security_env ：1-机密客户端,2-公开客户端；
client_type ：1-Web应用，2-浏览器应用，3-本地应用。

##授权

### 权限定义

oauth_scope(scope,resource,memo,version,timestamp).

### 授权申请

oauth_apply(apply_id,client_id,scope_list,apply_time,status,timestamp).

### 授权许可

oauth_grant(grant_code,apply_id,client_id,scope_list,approve_time).


### 授权码auth code

授权码用来换取access token，有效期一般不超过10分钟，只可以用一次。

不需要持久化保存。
保存在redis服务器上，key为auth_code，value为{client_id,redirect_uri}。

## 认证

oauth_token(access_token,refresh_token,scope_list,resource_list,client_id,redirect_uri,expires_in,create_time,status,timestamp).
status: 1-有效，2-过期

## 日志

oauth_log(log_id,from_uri,invoked_resource,access_token,response_code,response_msg,timestamp)



暂时先这样，对照协议再过一遍，有问题再改。


