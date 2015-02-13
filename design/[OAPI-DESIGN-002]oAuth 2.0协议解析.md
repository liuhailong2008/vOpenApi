#oAuth 2.0协议解析

> 编号：OAPI-DESIGN-002
> 作者：刘海龙
> 微博：[http://weibo.com/liuhailong2008]
> 博客：[http://blog.csdn.net/stationxp]


##协议概述

###4个主体

完整的oAuth 2.0协议流包括4个主体，6个步骤。
4个主体分别是：
 - 资源拥有者：可以是人，负责授权工作。对Open API来说，即发布方。发布方审批调用者的申请，通过后，即完成授权，体现为数据库中的记录。
 - 客户端：一般是第三方应用程序。对于Open API来说，即调用者。
 - 授权服务器：负责完成授权凭证的发放和验证工作。
 - 资源服务器：负责提供资源或服务。

###6个步骤

客户端要发起调用，面对对方3个角色，需要分别依次交流，每个角色一去一回，共6个步骤。

流程如下，首先是客户端和资源拥有者交流：

1. 客户端向资源拥有者发起申请，请求许可。
2. 资源拥有者批准申请，发送许可凭证grant_code给客户端。

客户端提交申请的时候提交了invoker uri，授权服务器可以发起如下请求，传回grant code。
```
http://invoker_uri?grant_code=xxxxxxxx
```
invoker uri在整个交互过程中要保持一致，对资源发起调用也必须来来自这个uri。

接下来就是客户端和授权服务器交互的过程了。

3. 客户端向授权服务器发起申请，提交grant_code、client_id、invoker_uri。
4. 授权服务器返回令牌token给客户端。

获得token之后，客户端暂时不需要直接和授权服务器打交道了。
客户端通过token，可以愉快地访问资源了，直到token过期、失效。

接下来，客户端和资源服务器交流，享受服务。

5. 客户端向资源服务器端发起请求，携带token。
6. 资源服务器验证token，提供服务。


支持sequence语法的MD阅读器可以看到时序图：
```sequence
客户端->资源拥有者:1.我想要访问XX接口
资源拥有者-->客户端: 2.准了，这是“授权“
客户端->授权服务器:3.我想要访问X的接口，这是“授权“
授权服务器-->客户端: 4.OK，这是“令牌“，拿好
客户端->资源服务器:5.1.我手续办齐了，我要访问你的XX1接口
资源服务器-->客户端: 6.1.好吧
客户端->资源服务器:5.2.我手续办齐了，我要访问你的XX2接口
资源服务器-->客户端: 6.2.好吧
```

上面的过程中，客户端直接和资源拥有者交互，oAuth 2.0推荐授权服务器为中介，客户端和资源拥有者不直接沟通。

以上即oAuth 2.0的big picture。

##授权服务器规划

> oAuth 2.0定义了4种授权模式，以下仅以授权码模式为例。以下假定读者熟悉授权码模式的流程。

###功能设计

协议的关键是授权服务器的实现。
在规划授权服务器功能时，不要考虑任何具体业务。
授权服务器完成以下几项工作：
- 发放Token
- 更新Token
- 验证token

###对外接口

#### 发放Token
Token的发放过程如下：
首先，客户端发起申请，如下：
```
http://auth_server/authorize?response_type=code&client_id=xxx&state=ddd&redirect_uri=httpxxxx
```
发送申请的地址需要和参数redirect_uri的值一致。
成功后，返回auth_code，发回状态码302。
```
location: httpxxxx?auth_code=ssss&state=ddd
```
传回的auth_code有效期通常是10分钟，只能使用一次。
接下来，客户端发起请求，申请正式token。

```
http://auth_server/authorize?grant_type=authorization_code&auth_code=ddd&&redirect_uri=httpxxxx
```
授权服务器返回：
access_token,token_type,expires_in,refresh_token,scope

#### 重发Token

```
http://auth_server/authorize?grant_type=refresh_token&refresh_token=xxxxx
```

#### 验证Token
```
http://auth_server/authenticate?access_token=ddd&invoker_uri=xxx&scope=ss
```

###程序设计
```
interface AuthorizationServer{
	/** 发token，更新token */
	Response authorize(Request request);
	/** 验证token */
	Response authenticate(Request request);
}
interface GrantHandler{
	/** 授权 */
	Response grant(Request request);
	/** 验证 */
	Response authenticate(Request request);
}
interface AuthorizationCodeGrantHandler extends GrantHandler{ 
	/** 返回auth code */
	Response authCode(Request request);
}
```
为了实现上述功能，授权服务器还应该实现用户注册、授权申请、授权审批、资源定义、资源组管理（即scope）、授权码管理（grant code）、Token管理等功能。



