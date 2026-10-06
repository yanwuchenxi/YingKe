package com.fongmi.android.tv.lab;

import android.content.Context;

/**
 * 影壳精简：实验室自动安装/执行默认关闭，避免后台拉起扩展环境。
 * 入口已在设置中隐藏；如需恢复，还原历史实现即可。
 */
public final class LabAutoStart {

    private LabAutoStart() {
    }

    public static void start(Context context) {
        // no-op
    }
}
