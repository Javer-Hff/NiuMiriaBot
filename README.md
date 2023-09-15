
项目使用JDK版本为OpenJDK17

签名服务：https://github.com/fuqiuluo/unidbg-fetch-qsign

配置文件
```yaml
#bot相关配置
bot:
  #qq号码
  qq:
  #qq密码  
  password:
  #协议，ANDROID_PHONE,ANDROID_PAD,ANDROID_WATCH,IPAD,MACOS;
  protocol:
  #监听群组，可配置多个
  listeningGroup:
    - 123456
    - 12345
  #v2ex的token
  v2token:
  #bot工作目录，主要用于缓存一些文件
  workdir:
  #代理地址
  proxy:
    host:
    port:
  #命令分隔符，用于分隔命令和参数
  split: ' '
```