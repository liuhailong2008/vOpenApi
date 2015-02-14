#oAuth 2.0测试


## 授权EntPoint

测试依据：rfc 6749 3.1

1. 请求：http://localhost:8080/soapi/api/oauth2/authorize
返回状态码：400
返回消息：没有传递redirect uri。

2. 请求：http://localhost:8080/soapi/api/oauth2/authorize?redirect_uri=client.com
返回状态码：302
返回消息：重定向到
```
http://localhost:8080/soapi/api/oauth2/client.com?error=invalid_request&error_description=Missing+response_type+parameter+value
```

3. 请求：http://localhost:8080/soapi/api/oauth2/authorize?redirect_uri=client.com&response_type=codex
返回状态码：302
返回消息：重定向到
```
http://localhost:8080/soapi/api/oauth2/client.com?error=invalid_request&error_description=Invalid+response_type+parameter+value
```

4. 请求：http://localhost:8080/soapi/api/oauth2/authorize?redirect_uri=client.com&response_type=code
返回状态码：302
返回消息：重定向到
```
http://localhost:8080/soapi/api/oauth2/client.com?error=invalid_request&error_description=Missing+parameters%3A+client_id
```

5. 请求：http://localhost:8080/soapi/api/oauth2/authorize?redirect_uri=client.com&client_id=s&response_type=code
返回状态码：302
返回消息：重定向到
```
http://localhost:8080/soapi/api/oauth2/client.com?code=958f936825e4a03a3379eec42e2c40d8
```
得到授权码了。
没有做验证工作，直接给了。

6. 请求：http://localhost:8080/soapi/api/oauth2/authorize?redirect_uri=client.com&client_id=s&response_type=token
返回状态码：302
返回消息：重定向到
```
http://localhost:8080/soapi/api/oauth2/client.com#expires_in=3600&access_token=8f30d4b403e3dca1ba797a4d9e4040a8
```
同样得到授权码了。
注意：返回的格式是access token，但这里拿到的只是授权码，而不是access token。

通过url传递token，这是不对的。

## Token EntPoint

1. http://localhost:8080/soapi/api/oauth2/access_token
返回状态码：400
{"error":"invalid_request","error_description":"Missing grant_type parameter value"}

2. http://localhost:8080/soapi/api/oauth2/access_token?grant_type=authorization_code
status:400
{"error":"invalid_request","error_description":"Method not set to POST."}

3.POST http://localhost:8080/soapi/api/oauth2/access_token 
grant_type=authorization_code
响应：
status:400
error_description:"Bad request content type. Expecting: application/x-www-form-urlencoded"

4. 
Host: localhost:8080
User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10.10; rv:35.0) Gecko/20100101 Firefox/35.0
Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8
Accept-Language: zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3
Accept-Encoding: gzip, deflate
Connection: keep-alive
Content-Type:application/x-www-form-urlencoded
---
grant_type=authorization_code
响应：
status:400
Missing parameters: redirect_uri code
Missing parameters: code
Missing parameters: client_secret client_id

status:401
error:unauthorized_client
error_description:Client authentication failed (e.g., unknown client, no client authentication included, or unsupported authentication method).



验证的逻辑还没做。暂时到此为止。


##小结：
- 架子搭起来了，跑起来了。
- 基本流程有了，细节基本都不对。

##下一步工作
以下按优先级排列
1. 陪老婆和小孩玩。
2. 进一步熟悉协议，熟悉每一步的消息传递方式、验证方式，这是目前主要问题，重点在RFC 6750的符合型上。
3. 完善客户端，实现自动化测试。
4. 实现非TLS安全。
5. 重构EntPoint代码，将逻辑抽象到StanOpenApi-Server中。最终StanOpenApi-Server-Impl中只需要做微量的工作即可获得oAuth 2.0支持。
6. 完善注册、验证等支持模块的设计。
7. 完成注册、验证等支持模块的开发。
8. 整体测试。
9. 完工。
0. 考虑Open API部分：接口设计如何体现，最好只写interface，服务器End Point代码、接口文档、调用说明自动生成；接口功能如何实现，基于dobbo改造，实现内部系统RPC简易化、高效化。










