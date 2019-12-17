
- 密码授权模式(recource ovner password credentials)

post请求：http://127.0.0.1:8101/oauth/token?username=user&password=123456&grant_type=password&client_id=dev&client_secret=dev


- 客户端授权模式(client credentials)

post请求：http://127.0.0.1:8101/oauth/token?grant_type=client_credentials&client_id=dev&client_secret=dev


- 授权码授权模式(authorization code)

http://127.0.0.1:8101/oauth/authorize?response_type=code&client_id=dev&redirect_uri=http://www.baidu.com


- 简化模式(implicit)

http://127.0.0.1:8101/oauth/authorize?response_type=code&client_id=dev&redirect_uri=http://www.baidu.com


- 刷新token

post请求：http://127.0.0.1:8101/oauth/token?grant_type=refresh_token&refresh_token=afc84284-1735-430c-bbc0-42a518fb8547&client_id=dev&client_secret=dev
