# 影壳 务实路线图

> 基座：Silent1566/webhtv · 包名 `com.yingke.app` · 仅主推 **mobile + arm64-v8a + Python**

## 已完成（Phase 0）

- [x] 选定可完整编译的基座（webhtv，含 Media3/mpv）
- [x] 包名 / 应用名 / 图标品牌化
- [x] GitHub Actions：`assembleMobileArm64_v8aDebug` 绿通
- [x] 不内置任何影视内容源（用户自行配置 CatVod 接口）

## Phase 1 — 可分发（当前）

- [x] 版本号独立为影壳 `1.0.0`（versionCode 10000）
- [x] 构建与发布说明（README / docs）
- [ ] 本地 Release 签名配置（`local.properties` + jks，不入库）
- [ ] 从 Actions 下载 Debug APK 真机冒烟：安装 → 导入配置 → 播放一条源

## Phase 2 — 产品打磨

- [ ] 默认空态文案优化（引导用户「设置 → 配置地址」）
- [ ] 按需接入自建/合法演示配置（仅自有或授权源）
- [ ] 精简体积：评估去掉不需要的 leanback 产物（源码可留，CI 只编 mobile）
- [ ] 站点适配：用 `tvbox-site-adapter` 技能产出四壳兼容源并自测

## Phase 3 — 运维

- [ ] 正式签名 Release + 简单更新通道（可选）
- [ ] 崩溃与基础日志（已有上游能力则只做开关文档）
- [ ] 版本节奏：跟进 webhtv/FongMi 安全与播放器修复，按需 rebase

## 明确不做

- 不做影视仓商业壳代码级还原
- 不内置第三方付费/灰色片源
- 不以 kitkat 等低版本分支替换主线

## 日常构建

```bash
./gradlew :app:assembleMobileArm64_v8aDebug
./gradlew :app:assembleMobileArm64_v8aRelease   # 需配置签名
```

## 精简进度（已执行）

- [x] 根目录调试/分析 md 归档至 `docs/archive/`
- [x] 开屏页品牌化：手机全屏「影壳」启动图 + 统一 startup_logo
- [x] 发布路径仅 mobile + arm64（CI / 文档约定；v7a/leanback 源码保留便于上游同步）
- [ ] Lab / WebHome / GitCloud / TMDB：默认关闭（后续开关）
- [ ] LUT / PDF / sherpa 等：保留代码引用，避免编译破坏；体积优化下一阶段再动

- [x] 开屏仅居中「影」字环饰（去掉影壳/YingKe 字样）
- [x] 设置中隐藏：实验室 / GitCloud / WebHome 相关入口
- [x] LabAutoStart 默认 no-op（不后台装环境）
- [x] 移除仓库内 webhome-devkit、serverless（非运行时）
- [x] 清空内置 lut_presets（约 11MB assets；用户仍可自选 LUT）

