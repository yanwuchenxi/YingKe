# 影壳（YingKe）

手机影音客户端 · 包名 `com.yingke.app`

基于 [Silent1566/webhtv](https://github.com/Silent1566/webhtv)（FongMi 系完整播放栈：Media3 / mpv / Chaquopy Python），**不内置任何内容源**。

| 项 | 值 |
|----|-----|
| 包名 | `com.yingke.app` |
| 版本 | **1.0.0**（versionCode 10000） |
| 主构建 | `mobile` + `arm64-v8a` |
| Python | Chaquopy 3.10 |
| CI | [Android Mobile Build](https://github.com/yanwuchenxi/YingKe/actions) |

## 快速构建

```bash
git clone https://github.com/yanwuchenxi/YingKe.git
cd YingKe
echo "sdk.dir=$ANDROID_HOME" > local.properties
./gradlew :app:assembleMobileArm64_v8aDebug
```

详见 [docs/BUILD.md](docs/BUILD.md) · 路线图 [ROADMAP.md](ROADMAP.md)

## 使用

1. 安装 APK（Debug 可从 GitHub Actions Artifact 下载）
2. 打开 **设置**，填写自己的 **配置地址**（CatVod / TVBox 兼容 JSON）
3. 返回首页浏览与播放

## 协议与声明

- 上游及本仓库均不提供影视资源
- 请仅配置你有权使用的合法源
- 许可证见上游 `LICENSE.md` 及本仓库依赖声明

## 上游

二次开发自 webhtv / FongMi 生态，Spider 协议兼容 CatVod。
