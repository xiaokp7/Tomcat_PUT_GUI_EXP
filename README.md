# Tomcat_PUT_EXP_V1.2
Tomcat PUT方法任意文件写入（CVE-2017-12615）exp

# 漏洞介绍
  2017年9月19日，Apache Tomcat官方确认并修复了两个高危漏洞，漏洞CVE编号:CVE-2017-12615和CVE-2017-12616,其中 远程代码执行漏洞（CVE-2017-12615）    影响： Apache Tomcat 7.0.0 - 7.0.79（7.0.81修复不完全）

  当 Tomcat 运行在 Windows 主机上，且启用了 HTTP PUT 请求方法（例如，将 readonly 初始化参数由默认值设置为 false），攻击者将有可能可通过精心构造的攻击请求向服务器上传包含任意代码的 JSP 文件。之后，JSP 文件中的代码将能被服务器执行。
 # 
# 工具介绍
- 漏洞检测

  检测目标站点是否存在tomcat put方法任意文件写入CVE-2017-12615）漏洞
  
  <img width="600" alt="image" src="https://user-images.githubusercontent.com/105373673/224598722-b892d618-5153-466a-8fea-1716c3e5e1f6.png">
  
- 命令执行

  执行操作系统命令
  
  <img width="600" alt="image" src="https://user-images.githubusercontent.com/105373673/224598759-faefbf79-5b90-45f9-bcc2-99de5bd3ad57.png">
  
- 文件上传

  默认上传哥斯拉webshell，可自定义上传webshell内容及上传文件名
  
<img width="1227" alt="image" src="https://user-images.githubusercontent.com/105373673/224598784-8a78eb59-58fa-4896-b769-1b9836b6efef.png">

