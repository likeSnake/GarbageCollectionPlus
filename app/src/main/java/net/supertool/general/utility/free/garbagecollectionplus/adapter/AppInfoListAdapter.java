package net.supertool.general.utility.free.garbagecollectionplus.adapter;

import static net.supertool.general.utility.free.garbagecollectionplus.util.AppInfoUtil.byteArrayToBitmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import net.supertool.general.utility.free.garbagecollectionplus.R;
import net.supertool.general.utility.free.garbagecollectionplus.entity.AppInfo;
import net.supertool.general.utility.free.garbagecollectionplus.util.CustomCountDownTimer;

import java.util.ArrayList;
import java.util.Random;

public class AppInfoListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<AppInfo> appInfos;

    public AppInfoListAdapter(Context context, ArrayList<AppInfo> appInfos) {
        this.context = context;
        this.appInfos = appInfos;
    }

    @Override
    public int getCount() {
        return appInfos.size();
    }

    @Override
    public AppInfo getItem(int position) {
        return appInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.item_app, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.app_icon = convertView.findViewById(R.id.app_icon);
            viewHolder.appName = convertView.findViewById(R.id.appName);
            viewHolder.clearingProgress = convertView.findViewById(R.id.clearingProgress);
            viewHolder.ic_finish = convertView.findViewById(R.id.ic_finish);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        AppInfo appInfo = appInfos.get(position);
        String appName = appInfo.getAppName();
        Bitmap icon = byteArrayToBitmap(appInfo.getIcon());

        if (!appInfo.isStart()) {
            appInfo.setStart(true);

            startPr(viewHolder.clearingProgress,appInfo);
        }

        if (icon != null) {
            viewHolder.app_icon.setImageBitmap(icon);
        }

        viewHolder.appName.setText(appName);

        return convertView;
    }

    public void startPr(ProgressBar progressBar,AppInfo appInfo){

      /*  new Thread(new Runnable() {
            @Override
            public void run() {

            }
        }).start();*/

        int i = new Random().nextInt(6) + 5;
/*        CustomCountDownTimer customCountDownTimer = new CustomCountDownTimer(10000, 10 * i, progressBar,appInfo);
        customCountDownTimer.start();*/
        new CountDownTimer(10000, 10*i) {
            @Override
            public void onTick(long millisUntilFinished) {
                //appInfo.setProgress();
                appInfo.startProgress();
                int progress = appInfo.getProgress();
                int m = progress;

                progressBar.setProgress(m);


            }
            @Override
            public void onFinish() {
                progressBar.setProgress(100);
            }
        }.start();
    }

    private static class ViewHolder {
        ImageView app_icon, ic_finish;
        TextView appName;
        ProgressBar clearingProgress;
    }
}