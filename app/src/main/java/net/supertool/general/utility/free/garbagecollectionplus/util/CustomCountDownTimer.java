package net.supertool.general.utility.free.garbagecollectionplus.util;

import android.os.CountDownTimer;
import android.widget.ProgressBar;

import net.supertool.general.utility.free.garbagecollectionplus.entity.AppInfo;

public class CustomCountDownTimer extends CountDownTimer {
    private ProgressBar progressBar;
    private AppInfo appInfo;

    public CustomCountDownTimer(long millisInFuture, long countDownInterval, ProgressBar progressBar,AppInfo info) {
        super(millisInFuture, countDownInterval);
        this.progressBar = progressBar;
        this.appInfo = info;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        appInfo.startProgress();
        int progress = appInfo.getProgress();
        System.out.println("进度："+progress);
        progressBar.setProgress(progress);
    }

    @Override
    public void onFinish() {
       // progressBar.setProgress(100);
    }
}