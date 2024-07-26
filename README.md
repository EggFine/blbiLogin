# 波比登录 [blbiLogin]

##### 波比登录 [blbiLogin] 是基于高版本 Minecraft Spigot 核心制作的玩家登录解决方案

#### 下载渠道 [Download Links]

[![MCDD](https://mcdd.cn/available_min.png)](https://mcdd.cn/resources/3)[![Modrinth](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/available/modrinth_vector.svg)](https://modrinth.com/plugin/blbilogin)[![Spigot](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/supported/spigot_vector.svg)](https://www.spigotmc.org/resources/117672)

#### 中文专区 [For Chinese]
<img alt="Dynamic JSON Badge" src="https://img.shields.io/badge/dynamic/json?url=https%3A%2F%2Fapi.mcdd.cn%2Fddget%2Fresource%2Finfo%2F2&query=resource.version&prefix=v%20&style=for-the-badge&label=MCDD%E5%B7%B2%E5%8F%91%E5%B8%83&color=hsl(180%2C%2055%25%2C%2049%25)&link=https%3A%2F%2Fmcdd.cn%2Fresources%2F3"> [![Wiki](https://img.shields.io/badge/%E4%B8%AD%E6%96%87%E6%96%87%E6%A1%A3-blue?style=for-the-badge&label=Wiki)](https://eggfine.gitbook.io/blbilogin-docs) [![QQ](https://img.shields.io/badge/%E5%8A%A0%E5%85%A5%E7%BE%A4%E8%81%8A-blue?style=for-the-badge&label=QQ&color=white)](https://qm.qq.com/q/TWjF1qyIg2)

#### 插件状态 [Plugin Info]
[![GitHub Actions Workflow Status](https://img.shields.io/github/actions/workflow/status/EggFine/blbiLogin/gradle.yml?style=for-the-badge)](https://github.com/EggFine/blbiLogin/actions) [![Latest Version](https://img.shields.io/spiget/version/117672?style=for-the-badge&label=Latest%20Version&color=brightgreen)](https://www.spigotmc.org/resources/117672) [![Spiget Downloads](https://img.shields.io/spiget/downloads/117672?style=for-the-badge)](https://www.spigotmc.org/resources/117672) [![Discord](https://img.shields.io/discord/1256544705991147601?style=for-the-badge&label=Discord&color=pink)](https://discord.gg/KjXXbSgJru)

#### 统计 [bStats]
![bStats Servers](https://img.shields.io/bstats/servers/22490?style=for-the-badge) ![bStats Players](https://img.shields.io/bstats/players/22490?style=for-the-badge)

#### 依赖 [Depends]
[![blbi Library](https://img.shields.io/spiget/version/118213?style=for-the-badge&label=blbi%20Library&color=yellow)](https://www.spigotmc.org/resources/118213)

| 项       | 值                            |
| -------- | ----------------------------- |
| 插件名   | 波比登录 [blbiLogin]          |
| 最低要求 | Minecraft ≥ 1.20 && Java ≥ 17 |

### 基本命令 [Commands]

| 命令                                                         | 缩写                     | 作用                   | 权限                       |
| ------------------------------------------------------------ | ------------------------ | ---------------------- | -------------------------- |
| /register <密码>                                             | /reg <密码>              | 玩家注册               |                            |
| /login <密码>                                                | /l <密码>                | 玩家登录               |                            |
| /blbilogin <参数>                                            | none                     | 插件主命令<br />reload | none<br />blbilogin.reload |
| 玩家：/resetpassword <密码> <新密码><br />控制台：/resetpassword <玩家> <新密码> | /resetpw<br />/resetpass | 重置密码命令           | none                       |

### 配置文件 [Configs]

```yaml
# blbiLogin 配置文件
# blbiLogin Configuration File

# 配置文件版本号，请勿修改
# Configuration file version, do not modify
version: 2.1.1

# 插件消息前缀
# Plugin message prefix
prefix: "§8[§fblbi§bLogin§8] §f"

# 插件语言设置
# Plugin language setting
# 可用语言：
# Available languages:
#  zh_CN, zh_TW, zh_HK, en_US, en_GB, ru_RU, ar_SA, ja_JP, es_AR, pt_BR, de_DE, fr_FR
language: zh_CN

# 玩家加入游戏时是否自动传送到预设位置
# Whether to automatically teleport players to a preset location upon joining
playerJoinAutoTeleportToSavedLocation: false

# 预设位置坐标（使用 /blbilogin savelocation 命令设置）
# Preset location coordinates (set using /blbilogin savelocation command)
locationPos:
  world: ""  # 世界名称 | World name
  x: 0.0     # X 坐标 | X coordinate
  y: 0.0     # Y 坐标 | Y coordinate
  z: 0.0     # Z 坐标 | Z coordinate
  yaw: 0.0   # 水平角度 | Yaw
  pitch: 0.0 # 垂直角度 | Pitch

# 玩家登录后是否自动返回之前的位置
# Whether to automatically return players to their previous location after logging in
playerJoinAutoTeleportToSavedLocation_AutoBack: false

# 是否使用 SQLite 数据库存储玩家信息
# Whether to use SQLite database to store player information
useSqlite: true

# 未登录玩家行为限制设置
# Behavior restrictions for unlogged players
# 是否限制未登录玩家移动
# Whether to restrict unlogged players from moving
noLoginPlayerCantMove: true
# 是否限制未登录玩家使用命令
# Whether to restrict unlogged players from using commands
noLoginPlayerCantUseCommand: true
# 未登录玩家允许使用的命令列表
# List of commands allowed for unlogged players
noLoginPlayerAllowUseCommand:
  - "/example"
# 是否使未登录玩家无敌（不受伤害）
# Whether to make unlogged players invulnerable (immune to damage)
noLoginPlayerCantHurt: true
# 是否为未登录玩家添加粒子效果
# Whether to add particle effects to unlogged players
noLoginPlayerParticle: true
# 是否使未登录玩家无法发送消息
# Whether to prevent unlogged players from sending messages
noLoginPlayerCantSendMessage: true

# 是否使玩家无法操作（如放置/挖掘方块，对实体进行伤害，获取物品）
# Whether to restrict unlogged players from interact, damage, get
# targetted and place/break blocks
noLoginPlayerCantInteract: true

# 未登录玩家提醒方式设置
# Reminder settings for unlogged players
# 注：消息、动作栏和标题提醒会伴随音效
# Note: Messages, action bar, and title notifications are accompanied by sound effects
# 是否发送聊天消息提醒
# Whether to send chat message reminders
noLoginPlayerSendMessage: true
# 是否发送标题提醒
# Whether to send title reminders
noLoginPlayerSendTitle: true
# 是否发送副标题提醒
# Whether to send subtitle reminders
noLoginPlayerSendSubTitle: true
# 是否发送动作栏提醒
# Whether to send action bar reminders
noLoginPlayerSendActionBar: true

# 未注册玩家提醒方式设置
# Reminder settings for unregistered players
# 注：消息、动作栏和标题提醒会伴随音效
# Note: Messages, action bar, and title notifications are accompanied by sound effects
# 是否发送聊天消息提醒
# Whether to send chat message reminders
noRegisterPlayerSendMessage: true
# 是否发送标题提醒
# Whether to send title reminders
noRegisterPlayerSendTitle: true
# 是否发送副标题提醒
# Whether to send subtitle reminders
noRegisterPlayerSendSubTitle: true
# 是否发送动作栏提醒
# Whether to send action bar reminders
noRegisterPlayerSendActionBar: true

# 登录成功提醒方式设置
# Successful login notification settings
# 是否发送标题提醒
# Whether to send title notifications
successLoginSendTitle: true
# 是否发送副标题提醒
# Whether to send subtitle notifications
successLoginSendSubTitle: true

# 解锁一些基岩版功能
# Unblock some bedrock features
bedrock:
  # 如果玩家通过基岩版使用 floodgate 或 UUID（基岩版用户）加入，是否允许玩家自动登录？
  # 请为基岩版用户配置正确的前缀，因为如果没有默认前缀，Java 版玩家名也可能被自动登录。
  # Allow player auto-login if they join from bedrock with floodgate or UUID (of Bedrock user)?
  # Please configure a correct prefix for bedrock users, as Java names could be auto-logged in without a default prefix.
  autologin:
    # 是否启用 Floodgate 的此功能？
    # Enable this feature with Floodgate?
    floodgate: false
    # 是否启用 UUID 的此功能？（使用基岩版 UUID）
    # Enable this feature with UUID? (Use Bedrock UUID)
    uuid: false
    # 是否启用前缀的此功能？（请为基岩版玩家配置前缀！）
    # Enable this feature with prefix? (Please configure a prefix for bedrock players!)
    prefix: false
    prefix_value: "*"
  # 是否允许基岩版玩家使用表单？
  # Allow forms for bedrock players?
  forms: true


```

