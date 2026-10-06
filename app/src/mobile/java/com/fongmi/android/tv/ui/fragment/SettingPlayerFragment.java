package com.fongmi.android.tv.ui.fragment;

import android.content.Intent;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;

import com.fongmi.android.tv.R;
import com.fongmi.android.tv.databinding.FragmentSettingPlayerBinding;
import com.fongmi.android.tv.impl.BufferListener;
import com.fongmi.android.tv.impl.SpeedListener;
import com.fongmi.android.tv.impl.UaListener;
import com.fongmi.android.tv.setting.PlayerSetting;
import com.fongmi.android.tv.setting.Setting;
import com.fongmi.android.tv.ui.base.BaseFragment;
import com.fongmi.android.tv.ui.dialog.BufferDialog;
import com.fongmi.android.tv.ui.dialog.ChoiceDialog;
import com.fongmi.android.tv.ui.dialog.PlayerKernelDialog;
import com.fongmi.android.tv.ui.dialog.SpeedDialog;
import com.fongmi.android.tv.ui.dialog.UaDialog;
import com.fongmi.android.tv.ui.dialog.VideoAspectModeDialog;
import com.fongmi.android.tv.utils.ResUtil;

import java.text.DecimalFormat;

import is.xyz.mpv.MPVLib;

/**
 * 播放设置（对齐 FongMi 常用项）。
 */
public class SettingPlayerFragment extends BaseFragment implements UaListener, BufferListener, SpeedListener {

    private FragmentSettingPlayerBinding mBinding;
    private DecimalFormat format;
    private String[] background;
    private String[] caption;
    private String[] failureFallback;
    private String[] kernel;
    private String[] mpvRender;
    private String[] render;
    private String[] scale;

    public static SettingPlayerFragment newInstance() {
        return new SettingPlayerFragment();
    }

    private String getSwitch(boolean value) {
        return getString(value ? R.string.setting_on : R.string.setting_off);
    }

    @Override
    protected ViewBinding getBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return mBinding = FragmentSettingPlayerBinding.inflate(inflater, container, false);
    }

    @Override
    protected void initView() {
        format = new DecimalFormat("0.#");
        mBinding.uaText.setText(Setting.getUa());
        mBinding.aacText.setText(getSwitch(PlayerSetting.isPreferAAC()));
        mBinding.tunnelText.setText(getSwitch(PlayerSetting.isTunnel()));
        mBinding.speedText.setText(format.format(PlayerSetting.getSpeed()));
        mBinding.bufferText.setText(String.valueOf(PlayerSetting.getBuffer()));
        mBinding.autoPlayText.setText(getSwitch(PlayerSetting.isAutoPlay()));
        mBinding.autoChangeText.setText(getSwitch(PlayerSetting.isAutoChange()));
        mBinding.failureFallbackText.setText((failureFallback = ResUtil.getStringArray(R.array.select_player_failure_fallback))[PlayerSetting.getFailureFallback()]);
        mBinding.caption.setVisibility(PlayerSetting.hasCaption() ? View.VISIBLE : View.GONE);
        mBinding.kernelText.setText((kernel = ResUtil.getStringArray(R.array.select_player_kernel))[PlayerSetting.getPlayer()]);
        mBinding.scaleText.setText((scale = ResUtil.getStringArray(R.array.select_scale))[PlayerSetting.getScale()]);
        mBinding.renderText.setText((render = ResUtil.getStringArray(R.array.select_render))[PlayerSetting.getRender()]);
        mBinding.backgroundText.setText((background = ResUtil.getStringArray(R.array.select_background))[PlayerSetting.getBackground()]);
        mBinding.captionText.setText((caption = ResUtil.getStringArray(R.array.select_caption))[PlayerSetting.isCaption() ? 1 : 0]);
        setMpvRows();
    }

    @Override
    protected void initEvent() {
        mBinding.ua.setOnClickListener(this::onUa);
        mBinding.aac.setOnClickListener(this::setAAC);
        mBinding.kernel.setOnClickListener(this::onKernel);
        mBinding.scale.setOnClickListener(this::onScale);
        mBinding.mpvRender.setOnClickListener(this::onMpvRender);
        mBinding.speed.setOnClickListener(this::onSpeed);
        mBinding.buffer.setOnClickListener(this::onBuffer);
        mBinding.autoPlay.setOnClickListener(this::setAutoPlay);
        mBinding.autoChange.setOnClickListener(this::setAutoChange);
        mBinding.failureFallback.setOnClickListener(this::setFailureFallback);
        mBinding.render.setOnClickListener(this::setRender);
        mBinding.tunnel.setOnClickListener(this::setTunnel);
        mBinding.caption.setOnClickListener(this::setCaption);
        mBinding.caption.setOnLongClickListener(this::onCaption);
        mBinding.background.setOnClickListener(this::onBackground);
    }

    private void onUa(View view) {
        UaDialog.show(this);
    }

    @Override
    public void setUa(String ua) {
        Setting.putUa(ua);
        mBinding.uaText.setText(ua);
    }

    private void setAAC(View view) {
        PlayerSetting.putPreferAAC(!PlayerSetting.isPreferAAC());
        mBinding.aacText.setText(getSwitch(PlayerSetting.isPreferAAC()));
    }

    private void onKernel(View view) {
        PlayerKernelDialog.show(this, PlayerSetting.getPlayer(), player -> {
            PlayerSetting.putPlayer(player);
            mBinding.kernelText.setText(kernel[player]);
            setMpvRows();
        });
    }

    private void onScale(View view) {
        VideoAspectModeDialog.show(this, PlayerSetting.getScale(), mode -> {
            PlayerSetting.putScale(mode);
            if (mode >= 0 && mode < scale.length) mBinding.scaleText.setText(scale[mode]);
        });
    }

    private void onMpvRender(View view) {
        if (mpvRender == null) mpvRender = ResUtil.getStringArray(R.array.select_mpv_render);
        ChoiceDialog.showSingle(this, R.string.player_mpv_render, mpvRender, PlayerSetting.getMpvRender(), which -> {
            PlayerSetting.putMpvRender(which);
            mBinding.mpvRenderText.setText(getMpvRenderText());
        });
    }

    private String getMpvRenderText() {
        if (mpvRender == null) mpvRender = ResUtil.getStringArray(R.array.select_mpv_render);
        int index = PlayerSetting.getMpvRender();
        if (index < 0 || index >= mpvRender.length) index = 0;
        return mpvRender[index];
    }

    private void setMpvRows() {
        boolean visible = PlayerSetting.getPlayer() == PlayerSetting.MPV;
        mBinding.mpvRender.setVisibility(visible ? View.VISIBLE : View.GONE);
        mBinding.mpvRenderText.setText(getMpvRenderText());
    }

    private void onSpeed(View view) {
        SpeedDialog.show(this);
    }

    @Override
    public void setSpeed(float speed) {
        PlayerSetting.putSpeed(speed);
        mBinding.speedText.setText(format.format(speed));
    }

    private void onBuffer(View view) {
        BufferDialog.show(this);
    }

    @Override
    public void setBuffer(int times) {
        PlayerSetting.putBuffer(times);
        mBinding.bufferText.setText(String.valueOf(times));
    }

    private void setAutoPlay(View view) {
        PlayerSetting.putAutoPlay(!PlayerSetting.isAutoPlay());
        mBinding.autoPlayText.setText(getSwitch(PlayerSetting.isAutoPlay()));
    }

    private void setAutoChange(View view) {
        PlayerSetting.putAutoChange(!PlayerSetting.isAutoChange());
        mBinding.autoChangeText.setText(getSwitch(PlayerSetting.isAutoChange()));
    }

    private void setFailureFallback(View view) {
        ChoiceDialog.showSingle(this, R.string.player_failure_fallback, failureFallback, PlayerSetting.getFailureFallback(), which -> {
            PlayerSetting.putFailureFallback(which);
            mBinding.failureFallbackText.setText(failureFallback[which]);
        });
    }

    private void setRender(View view) {
        int index = PlayerSetting.getRender() == 0 ? 1 : 0;
        PlayerSetting.putRender(index);
        mBinding.renderText.setText(render[index]);
    }

    private void setTunnel(View view) {
        PlayerSetting.putTunnel(!PlayerSetting.isTunnel());
        mBinding.tunnelText.setText(getSwitch(PlayerSetting.isTunnel()));
    }

    private void setCaption(View view) {
        PlayerSetting.putCaption(!PlayerSetting.isCaption());
        mBinding.captionText.setText(caption[PlayerSetting.isCaption() ? 1 : 0]);
    }

    private boolean onCaption(View view) {
        if (!PlayerSetting.hasCaption()) return false;
        Intent intent = new Intent(Settings.ACTION_CAPTIONING_SETTINGS);
        startActivity(intent);
        return true;
    }

    private void onBackground(View view) {
        ChoiceDialog.showSingle(this, R.string.player_background, background, PlayerSetting.getBackground(), which -> {
            PlayerSetting.putBackground(which);
            mBinding.backgroundText.setText(background[which]);
        });
    }
}
