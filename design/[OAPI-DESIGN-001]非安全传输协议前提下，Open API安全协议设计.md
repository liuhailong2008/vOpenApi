#非安全传输协议前提下，Open API安全协议设计

> 编号：OAPI-DESIGN-001
> 作者：刘海龙
> 微博：[http://weibo.com/liuhailong2008]
> 博客：[http://blog.csdn.net/stationxp]

本文考虑在不使用安全传输协议的前提下，Open API调用的安全问题。
##角色定义
- 发布者：Open API的发布者。
- 调用方：Open API的调用者。
##调用方消息发送流程
1. 生成一个UUID，称为~_seed~。
2. 对~_seed~用自己的私钥签名，得到~_sign~，提供给发布者认证身份。
3. 对~_seed~用发布者的公钥加密，得到~_key~。
4. 使用~_seed~对消息~_msg~对称加密，得到密文~_msgx~。
5. 将~_sign~~_key~~_msgx~拼接，进行Base64压缩（可选），得到~_body~。
6. 发送~_body~，执行API调用。
##发布者消息接收流程
1. 得到~_body~，拆分为~_sign~、~_key~、~_msgx~。
2. 通过调用着~uri~到注册库中找到调用者公钥~_invkerpk~。
3. 使用~_invkerpk~解密~_sign~，得到~_seed1~。
4. 使用自己对私钥揭秘~_key~，得到~_seed2~。
5. 比对~_seed1~和~_seed2~，如果一致，确认得到~_seed~；否则处理过程结束。
6. 使用~_seed~对~_msgx~解密，得到明文消息。
##调用结果返回流程
1. 返回结果为预先设定的消息代码。
2. 采用安全方式传输，则使用调用者的公钥加密。
##代码设计
###调用方
```Java
 interface Invoker{
 	void setEndPoint(InvokerEndPoint endPoint);
 	/**
 	* 通过调用EndPoint实现。
 	*/
 	Response get(String api,Object…params);
 	Response post(String api,Object…params);
 }
 interface InvokerEndPoint{
 	Response invoke(Request request);
 }
 /** 装饰模式 */
 interface SecurityInvokerEndPoint{
 	Response invoke(Request request);
 }
```
###发布者
```Java
 interface OpenApiFilter{
 	void setEndPoint(ExportEndPoint endPoint);
 	/**
 	* 通过调用EndPoint实现。
 	*/
 	HttpServletResponse process(HttpServletRequest request);
 }
 interface ExportEndPoint{
 	Response export(Request request);
 }
 /** 装饰模式 */
 interface SecurityExportEndPoint{
 	Response export(Request request);
 }
 interface ExportHandler{
 	Response handle(Object...args);
 }
```
