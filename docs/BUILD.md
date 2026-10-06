# 影壳构建说明

## 环境

| 项 | 要求 |
|----|------|
| JDK | 21 |
| Python | 3.10（Chaquopy） |
| Android SDK | compileSdk 见 `gradle/libs.versions.toml` |
| NDK | 工程指定版本（见 `app/build.gradle` ndkVersion） |

## 配置

`local.properties`（不提交）：

```properties
sdk.dir=/path/to/Android/Sdk
# 可选 Release 签名
# storeFile=/path/to/yingke.jks
# keyAlias=yingke
# storePassword=***
# keyPassword=***
```

## 命令

```bash
# 主推：手机 arm64 Debug
./gradlew :app:assembleMobileArm64_v8aDebug

# 手机 arm64 Release（有签名则正式签，否则 debug 签）
./gradlew :app:assembleMobileArm64_v8aRelease

# 电视版（一般不需要）
./gradlew :app:assembleLeanbackArm64_v8aDebug
```

产物目录：`app/build/outputs/apk/`

## CI

工作流：`.github/workflows/android-mobile.yml`  
触发：push / PR / 手动  
产物：Actions Artifact `yingke-mobile-arm64-debug`

## 配置源

App **不内置**点播站源。安装后在设置中填写 CatVod 兼容配置 URL，或导入本地 JSON。
