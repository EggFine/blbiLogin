# 波比登录 [blbiLogin]

##### 波比登录 [blbiLogin] 是基于高版本 Minecraft Paper 核心制作的玩家登录解决方案

| 项       | 值                            |
| -------- | ----------------------------- |
| 插件名   | 波比登录 [blbiLogin]          |
| 最新版本 | Alpha 1.0                     |
| 最低要求 | Minecraft ≥ 1.21 && Java ≥ 21 |

### 基本命令

| 命令              | 缩写        | 作用                     | 权限                       |
| ----------------- | ----------- | ------------------------ | -------------------------- |
| /register <密码>  | /reg <密码> | 玩家注册                 |                            |
| /login <密码>     | /l <密码>   | 玩家登录                 |                            |
| /blbilogin <参数> | none        | 插件主命令<br /><reload> | none<br />blbilogin.reload |

### 配置文件

```yaml
#blbiLogin的配置文件

# 警告: 配置文件版本号, 无特殊情况不要修改!!! [Warning: Do not edit this!!!]
version: 1.0

prefix: "§8[§fblbi§bLogin§8] §f"

# 插件语言文件
# Use Language. ! important ! We default to having zh_CN and en_US language files. For other languages, you can create your own yaml files in the /languages directory
language: zh_CN

# 使用 Sqlite 方式存储玩家数据 (!重要! 目前版本仅支持这一种存储方式, 请勿修改!)
# use Sqlite (!important! this type is only can use for now!)
useSqlite: true

# 禁止未登录玩家移动
# No Login Player Can't Move
noLoginPlayerCantMove: true
```

