# 影壳（YingKe）

基于 [Silent1566/webhtv](https://github.com/Silent1566/webhtv)（FongMi 系完整播放栈）二次开发的手机影音客户端。

| 项 | 值 |
|----|----|
| 包名 | `com.yingke.app` |
| 应用名 | 影壳 |
| 构建 | **mobile** + **arm64-v8a**（含 Chaquopy Python） |
| 上游 | webhtv / FongMi 协议兼容 |

```bash
./gradlew :app:assembleMobileArm64_v8aDebug
# 或 Release
./gradlew :app:assembleMobileArm64_v8aRelease
```

---

# WebHomeTV

WebHomeTV 是基于 [FongMi](https://github.com/FongMi/TV) / CatVod 生态二次开发的 Android 影音应用,保留原有点播、直播、Spider、解析、投屏、本地 HTTP 服务等能力,并重点增强了 **WebHome 自定义首页**、**App Native SDK**、**管理页面**、**远程托管**、**WebHome 扩展**、**登录态学习/同步**、**网盘链接检测**、**站点健康排序**、**观影记录同步** 和 **Nostr/TMDB 推荐首页**。

项目的核心目标不是替换 CSP/Spider 体系,而是让 CSP 站点首页变成一个真正可开发的网页应用:开发者用 HTML/CSS/JavaScript 定制首页,再通过 App 暴露的 Native 能力完成搜索、播放、跨域请求、资源代理、最近观看、网盘检测和状态同步。

### 增强功能

- **网盘检测**:内置网盘分享链接有效性检测,WebHome 和本地 HTTP API 均可调用。
- **站点健康排序**:自动学习站点搜索、详情和播放成功率,搜索与换源优先使用更可用的站点;站点弹窗默认保留用户配置顺序,可在弹窗内单独开启健康排序。
- **管理页面**:在 App 内启动局域网浏览器管理页 `/m`,可管理本机或远端设备文件、登录态、同步目录、站点注入、接口、壳代理、搜索和推送,运行期间通过前台服务保活。
- **远程托管**:通过自建 Cloudflare/Deno/Vercel/Go/Rust 中转服务绑定多台 WebHTV 设备,支持设备状态、远程搜索/推送、接口配置、主页设置、一键同步和最近日志;Go/Rust 版支持 WebSocket 实时通道,不支持时自动回退 HTTP 轮询。部署说明和二进制见 [远程托管中转服务器文档及二进制](serverless)。
- **一键同步**:在同一局域网设备间同步配置、站源数据(Jar/脚本保存数据)、登录态、WebHome 数据、搜索记录、观看历史、收藏和应用设置,每项可单独勾选。
- **站点注入**:添加自定义 WebHome 或通用 CSP 站点,主列表显示核心摘要和快捷操作,新增/修改在独立表单中维护启用状态、插入位置、首页、搜索和换源行为;顶部“识别”可粘贴单个或多个松散站点 JSON 片段并自动归类追加;WebHome 站点级扩展可直接填写扩展 URL / JSON,也可选择本地 JS/CSS/JSON 自动生成配置。
- **WebHome 扩展**:给真实网页注入用户脚本,主列表显示扩展摘要和状态,新增/修改在独立表单中配置本地文件、远程链接/manifest、直接代码、表单生成或 JSON;匹配范围默认从当前点播配置的 WebHome 站点弹窗多选,也可切换到 CSP key 正则;提供调试工作台用于 Web 预览、Console/Network/Elements 和代码保存预览。
- **观影记录同步**:增强功能中提供独立总览页,包含总开关、本机 API 修改开关、远端同步源和 Webhook 上报。爬虫可通过 `/api/playback/current` 读取当前播放记录,也可在用户开启修改后调用 `/api/playback/progress`、`/api/playback/progress/batch` 或 `/api/playback/progress/delete` 写入/清理本地进度;App 也可从用户配置的远端 API 拉取批量记录合并到本地历史,并通过删除墓碑同步清理记录。仓库内置的 Cloudflare、Deno、Vercel、Go、Rust 五种服务端都可用同一 URL 同时承接 Webhook 和增量拉取，分别使用 Durable Object SQLite、Deno KV、Redis REST 或本地原子文件持久化。完整协议见 `webhome-devkit/docs/应用完整开发文档.md` 的“观影记录同步”章节。
- **登录态学习**:用户手动开启后学习 Cookie、Token、接口 Jar 网盘登录文件等登录态路径,待确认项可在管理页查看/编辑,并可参与一键同步。
- **APP 代理**:配置代理地址和域名匹配规则,可按当前站点自动建议代理域名,用于改善特定站点、接口或播放链路的网络访问。
- **调试日志**:本机和局域网日志查看入口,便于排查播放、代理、站源和 WebHome 相关问题。

以上能力集中在设置页的"增强功能"入口,手机端和电视端均为独立设置页。

## 效果演示

https://github.com/user-attachments/assets/984c274f-8a9b-4857-b641-d251e061f5cc

演示视频对应的站点配置(Nostr/TMDB 推荐首页):

```json
{
  "key": "Nostr",
  "name": "Nostr推荐",
  "type": 3,
  "api": "csp_Nostr",
  "homePage": "https://www.252035.xyz/xs/tvbox/nostr.html"
}
```

## 文档

按功能模块拆分的接入文档见 [**开发者接入文档**](docs/integration/README.md)，覆盖总配置、点播源、Spider、Result/Vod、直播/EPG/回看、解析器、字幕、弹幕、播放结果、本地代理和扩展包。字段说明以当前仓库实际代码为准。

完整开发说明见 [**应用完整开发文档.md**](webhome-devkit/docs/应用完整开发文档.md),包含:

- App 配置字段(点播、解析、直播、样式)
- Spider 开发,JS/Python Spider 运行时
- 本地 HTTP 服务端点总览
- WebHome SDK 全部方法的参数和返回值
- 透明背景、电视端遥控器 UX、性能最佳实践
- 网盘检测 API 和站点健康排序
- 观影记录同步、Webhook 上报和爬虫 HTTP API
- 管理页面和局域网 HTTP 能力
- 远程托管部署、绑定流程和能力边界
- WebHome 扩展脚本开发
- 登录态学习与同步
- PanSou 集成、Nostr 首页实现要点
- 隐藏功能和使用技巧
- Android Intent、DLNA、MediaSession
- CORS、Cookie 和网络策略

WebHome 主页、扩展、模板、示例和 AI skills 统一放在 [webhome-devkit/](webhome-devkit/) （附 [独立 CNB 仓库](https://cnb.cool/fish2035/ext)）：

- 扩展脚本开发指南见 [webhome-devkit/README.md](webhome-devkit/README.md)。
- 扩展示例见 [webhome-devkit/examples/extension