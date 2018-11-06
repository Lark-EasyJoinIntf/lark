# lark
云雀（百灵鸟）个人服务

集easy join、支付集成、界面化代码自动生成等开源项目


一、Easy join项目

· 项目背景

1、面对几十上百的参数的接口时，你是否感到眼花缭乱、发愁、头疼？
2、在公司现有成熟的业务上突然需要接入外部的数据接口，是否感觉参数、返回等的转换繁琐且费事？
3、当你接入同一个业务，却要面对多套外部接口，且返回五花八门？
4、当你的入参格式是JSON，当对方的入参格式缺失XML或其他，是不是感觉天都要塌下来了？
5、当外部接口参数改变时，是否觉得变更麻烦？
      ..........
本项目就是为了一站式解决这些问题应势而生，为你解决这些烦恼、让你更专注于业务本身！

· 主调接口

包名：com.easyjoin.util

IntfUtil.sendPost(JSONObject reqParams, String provider, String intfCode)

reqParams：请求参数
provider： 接口提供者编码
intfCode： 接口编码或接口路径
· 界面化操作

1.项目集成

(1)将项目下载到本地，在本地以war包的形式引入业务项目。项目基于Spring MVC框架搭建，业务工程需要满足Spring的要求。

(2)easy-join-config.xml文件可以使用业务项目本身的文件，将 easy-join-servlet.xml配置到web.xml或包含进业务系统的servlet文件即可

(3)easyjoin.properties下配置映射文件保存路径

2.新增提供者

(1)在界面左上角点新增，弹出提供者新增界面。配置提供者的编码、描述、请求地址（接口外部访问的域名或IP等）

(2)请求头配置：为该接口提供者所有接口的统一请求参数，加入Header请求头。如果存在请求头参数则需要配置，参数值可为固定的也可为变量（#变量名#）

(3)标准输出配置：为该接口提供者所有接口的统一输出，一般为请求接口返回的状态及描述信息。可映射多个节点，用"|"分隔


3.新增提供者所提供的接口

(1)双击提供者列表，右侧和下侧会刷新该提供者的信息，然后点击右侧新增接口，弹出接口新增界面，填写接口的基本信息

(2)接口入参映射配置：根据提供者提供的接口文档，按其结构将接口所需的参数名与本地业务统一的参数名映射。依据type属性，可配置为：不填值-必传 项；fit-固定值；auto-自动序列填充；list-列表；false-可选（根据入参是否存在决定是否生成在最终请求报文中）；

(3)结果数据节点映射配置：（该配置不包括状态信息）根据返回的请求结果，按业务方的数据结构，将本地业务统一的节点与返回结果映射。依据type属性，可配置为：list-列表；

4.接口测试

(1)点击接口列表的"接口测试"按钮，进入请求入参填写、请求结果展示界面

(2)依据参数对照，将必填参数赋值

(3)点击发送请求，等待请求结果


5.接口使用

(1)定义转换器：转换器实现com.easyjoin.util.http.DataParser接口。目前默认实现了Xml和json的转换器XmlDataParser和JsonDataParser

(1)调用主调接口IntfUtil.sendPost(JSONObject reqParams, String provider, String intfCode)

(2)示例：

  JSONObject reqParams = JSONObject.parseObject(reqParamsStr);

  JSONObject result = IntfUtil.sendPost(reqParams, provider, intfCode);

6.扩展-转换器

转换器接口：com.easyjoin.util.http.DataParser接口。包含参数转换方法和结果转换方法

默认实现了输入输出一致的xml与json格式的转换器，如果客户有自己的格式，可以自定义实现，如输入是json格式，输出是xml格式

转换器自定义

(1)定义自己的格式类型，如JSONXML，表示入参是JSON格式，返回是XML格式

(2)实现com.easyjoin.util.http.DataParser接口，实现的类名规则是JsonXmlDataParser，加上@Component("jsonxmlDataParser")注解

(3)配置接口信息，请求及应答格式选择您自定义的格式类型即可

· 团队风采
    听风者：10年以上IT工作从业经验(待完善)
    UZ：4年以上IT工作从业经验(待完善)

待续。。。
