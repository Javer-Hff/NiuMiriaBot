<div align=center></br></br></br>
<center> <img src="http://q2.qlogo.cn/headimg_dl?dst_uin=2506694095&spec=100" style="zoom:300%;" /></center>

#  <center>  Hff-NiuZibot</center>

###### <center>Introduce</center>

###### 							
![java](https://img.shields.io/badge/java-black?logo=openjdk) ![qqBot](https://img.shields.io/badge/qq-bot-blue?logo=tencentqq)  ![open sourse (shields.io)](https://img.shields.io/badge/open-sourse-darkgreen?logo=opensourceinitiative)   ![github (shields.io)](https://img.shields.io/badge/github-grey?logo=github) ![gitee (shields.io)](https://img.shields.io/badge/gitee-orange?logo=gitee) ![git (shields.io)](https://img.shields.io/badge/git-lightblue?logo=git) ![Mit: license (shields.io)](https://img.shields.io/badge/GNU%20AGPL-license-blue?logo=bookstack) ![img](https://komarev.com/ghpvc/?username=hff-niubot&&style=flat-square)  

<center>use java implement</center>

</div>



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