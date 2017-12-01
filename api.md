---
title: Syx API 文档
author: Equim
---
<style>body{width:55em;}</style>
# 契约
* 本文档中出现的所有的“必须”、“必须不”、“应当”、“应当不”、“可以”分别对应 [RFC 2119](https://tools.ietf.org/html/rfc2119) 所定义的"MUST"、"MUST NOT"、"SHOULD"、"SHOULD NOT"、"MAY"。
* REST API 后端发生错误时，响应的状态必须不是`200 OK`且响应 Body 中必须包含`msg`字段。
* 若响应 Body 包含`msg`字段，则它必须是能展现给用户的。`msg`应当不是内部的异常、调用栈等信息。
* 若发送方违反契约，则接收方有权对发送方采取不信任措施，例如强行断开连接等。

# API 文档 (REST)
## 用户注册
用户注册接口。服务器应当在注册成功后自动登录，并如同下文的用户登录 API 一样返回 Set-Cookie 头。

### 请求头
```plain
POST /api/signup HTTP/1.1
Accept: application/json
Content-Type: application/json; charset=utf-8
```

### 请求参数
| 参数名      | 必选   | 类型     | 说明   |
| :------- | :--- | :----- | ---- |
| username | 是    | String | 用户名  |
| password | 是    | String | 密码   |
| email    | 是    | String | 邮箱   |

### 响应参数
| 参数名  | 必选   | 类型     | 说明                  |
| :--- | :--- | :----- | ------------------- |
| msg  | 否    | String | 错误的原因               |
| uid  | 否    | String | 若未发生错误，则该字段为该用户的 ID |

### 示例
__请求__
```plain
POST /api/signup HTTP/1.1
Accept: application/json
Content-Type: application/json; charset=utf-8
```
```js
{
  "username": "Equim",
  "password": "さやか-大好きです！",
  "email":"123456789@gmail.com"
}
```
__响应__
```plain
HTTP/1.1 200 OK
Set-Cookie: auth=bkgmaGqwk8GrW2
Content-Type: application/json; charset=utf-8
```
```js
{
  "uid": "7914be3a"
}
```

## 用户登录
用户登录接口。服务器应当设置 session，返回相应的 Set-Cookie 头。

### 请求头
```plain
POST /api/login HTTP/1.1
Accept: application/json
Content-Type: application/json; charset=utf-8
```

### 请求参数
| 参数名      | 必选   | 类型     | 说明   |
| :------- | :--- | :----- | ---- |
| username | 是    | String | 用户名  |
| password | 是    | String | 密码   |

### 响应参数
| 参数名     | 必选   | 类型      | 说明                     |
| :------ | :--- | :------ | ---------------------- |
| msg     | 否    | String  | 错误的原因                  |
| uid     | 否    | String  | 若未发生错误，则该字段为该用户的 ID    |
| isAdmin | 否    | Boolean | 若未发生错误，则该字段表示该用户是否为管理员 |

### 示例
__请求__
```plain
POST /api/login HTTP/1.1
Accept: application/json
Content-Type: application/json; charset=utf-8
```
```js
{
  "username": "Equim",
  "password": "さやか-大好きです！",
}
```
__响应__
```plain
HTTP/1.1 200 OK
Set-Cookie: auth=bkgmaGqwk8GrW2
Content-Type: application/json; charset=utf-8
```
```js
{
  "uid": "7914be3a",
  "isAdmin": true
}
```

## 鉴权窗口
该 API 提供一个根据客户端请求头的`Cookie`字段来鉴权的方法，返回该用户身份的有效性及相关权限等。(rememberme)

### 请求头
```plain
GET /api/auth HTTP/1.1
Accept: application/json
```

### 请求参数
空。

### 响应参数
| 参数名      | 必选   | 类型      | 说明                              |
| :------- | :--- | :------ | ------------------------------- |
| isLogged | 是    | Boolean | 是否为已登录用户                        |
| uid      | 否    | String  | 若 logged 为 true，则该字段为该用户的 ID    |
| isAdmin  | 否    | Boolean | 若 logged 为 true，则该字段表示该用户是否为管理员 |
| username | 否    | String  | 若 logged 为 true，则该字段为该用户的用户名    |

### 示例
__请求__
```plain
GET /api/auth HTTP/1.1
Accept: application/json
Cookie: auth=ebWIZiSqyf8
```
Body 为空。

__响应__
```plain
HTTP/1.1 200 OK
Content-Type: application/json; charset=utf-8
```
```js
{
  "isLogged": true,
  "uid": "7914be3a",
  "isAdmin": false,
  "username": "Aqua"
}
```

## 消息记录



# API 文档 (WebSocket)

所有的 WebSocket 过程必须遵循 [RFC 6455](https://tools.ietf.org/html/rfc6455) 规范。

应用层面，所有的 WebSocket 帧必须为 JSON 字符串，若一方发送的帧不符合 JSON 格式，接收方有权直接断开连接。

以下为详细的流程。

## 握手
路由`/ws`处理 WebSocket 连接。

### 示例
__请求__
```plain
GET /ws HTTP/1.1
Upgrade: websocket
Connection: Upgrade
Sec-WebSocket-Key: zmPKAThg4EynZNhDNX6wKA==
Sec-WebSocket-Version: 13
```

__响应__
```plain
HTTP/1.1 101 Switching Protocols
Upgrade: websocket
Connection: Upgrade
Sec-WebSocket-Accept: o7EnkkjcWINA5h0po5ycwGPTGBs=
```

## 鉴权
WebSocket 的握手本身是一个 HTTP 请求，在握手完毕后，服务端必须像 RESTful API 中的“鉴权窗口”一样，根据客户端请求头中的`Cookie`判断其身份是否有效。如果身份无效，或没有指明身份，则以匿名处理。

服务端做完上述鉴权后，必须向客户端返回一个鉴权信息，作为本次连接的第一条讯息。客户端必须等待该讯息到达，在此之前，所有其他讯息均无效。

### 响应参数
| 参数名      | 必选   | 类型      | 说明                              |
| :------- | :--- | :------ | ------------------------------- |
| isLogged | 是    | Boolean | 是否为已登录用户                        |
| uid      | 否    | String  | 若 logged 为 true，则该字段为该用户的 ID    |
| isAdmin  | 否    | Boolean | 若 logged 为 true，则该字段表示该用户是否为管理员 |
| username | 否    | String  | 若 logged 为 true，则该字段为该用户的用户名    |

__例：__
```js
{
  "isLogged": false // 匿名用户
}
```

## 一般通信模式
鉴权完成后即进入一般通信模式。

### 客户端主动请求
#### 请求参数
| 参数名      | 必选   | 类型     | 说明                                       |
| :------- | :--- | :----- | ---------------------------------------- |
| c_seqid  | 是    | Number | 该帧的序列号，客户端必须保证不会重复                       |
| cmd      | 是    | String | 指示该操作的类型。目前，只能是`"chat"`                  |
| chat.to  | 否    | String | 聊天消息发送的对象的 ID，当且仅当发送者为管理员时有效，其他情况，发送给管理员 |
| chat.msg | 否    | String | 聊天消息的内容                                  |

#### 响应参数
| 参数名        | 必选   | 类型      | 说明                        |
| :--------- | :--- | :------ | ------------------------- |
| s_seqid    | 是    | Number  | 该帧的序列号，服务端必须保证不会重复        |
| c_seqid    | 是    | Number  | 该帧对应的请求帧的序列号，服务端必须保证该字段有效 |
| isAccepted | 是    | Boolean | 指示客户端的请求是否已被受理            |
| msg        | 否    | String  | 若未被受理，则此字段为原因             |

### 服务端主动请求
#### 请求参数
| 参数名       | 必选   | 类型     | 说明                                       |
| :-------- | :--- | :----- | ---------------------------------------- |
| s_seqid   | 是    | Number | 该帧的序列号，服务端必须保证不会重复                       |
| cmd       | 是    | String | 指示该操作的类型。目前，只能是`"chat"`                  |
| chat.from | 否    | String | 聊天消息来自的对象的 ID，当且仅当接收者为管理员时有效，其他情况，发送者均为管理员 |
| chat.msg  | 否    | String | 聊天消息的内容                                  |

#### 响应参数
客户端只在必要时响应。

| 参数名     | 必选   | 类型     | 说明                        |
| :------ | :--- | :----- | ------------------------- |
| c_seqid | 是    | Number | 该帧的序列号，客户端必须保证不会重复        |
| s_seqid | 是    | Number | 该帧对应的请求帧的序列号，客户端必须保证该字段有效 |

_(待续)_

问题，匿名用户，登录用户，admin用户三个用户的websocket握手统一

后台写死admin，uid必须提前确定，写死在数据库或者写死在代码？（思考一下？？？

写在代码里session很麻烦啊。。。

admin必须第一个登陆，然后确定它的uid才能方便之后的各个信息查询



- [x] 然后每次鉴权都是从数据库之中读取信息然后鉴权
- [ ] 代码里面添加查询admin是否在线？？？
- [ ] 数据库里面提前添加admin这个数据，以后实验直接使用更新数据库，不删除




匿名用户向服务器请求uid，返回新的uid，登陆用户从数据库里面获取uid，admin写死uid

匿名用户获取uid，登陆用户获取uid，admin获取自己uid都是通过登陆（登陆用户可以通过注册

通过获取列表获得别人的uid和name

后台维护的三个列表的作用

```js
userSessionMap：uid和websocketsession的对应关系
NameToUid：name和uid的对应关系
alives：仅维护已经登陆的人的cookie
```

userSessionMap：每次建立连接会被增加到这里面，断开连接就从里面删除

NameToUid：与userSessionMap同时进行维护，建立连接的时候添加到这里面

alives：每次signup或者login时候添加，logout删除

```sql
insert into users values("4c4b716b-dbc3-47fe-b750-95dfee776647",
                        "rouzipking@gamil.com",
                        "admin",
                        "123",
                        "admin");
```



