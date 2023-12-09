package net.supertool.general.utility.free.garbagecollectionplus.adapter;


import static net.supertool.general.utility.free.garbagecollectionplus.util.AppInfoUtil.byteArrayToBitmap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import net.supertool.general.utility.free.garbagecollectionplus.R;
import net.supertool.general.utility.free.garbagecollectionplus.entity.AppInfo;
import java.util.ArrayList;
import java.util.Random;


public class AppInfoAdapter extends RecyclerView.Adapter<AppInfoAdapter.AppInfoViewHolder> {
    private int selects=0;
    private int selectedPosition;
    private onFinishListener listener;
    private boolean startFirst = true;
    private Context context;
    private ArrayList<AppInfo> appInfos;
    private ArrayList<String> select_list = new ArrayList<>();
    private boolean isFirst = true;
    public AppInfoAdapter(Context context, ArrayList<AppInfo> appInfos,onFinishListener listener) {
        this.context = context;
        this.appInfos = appInfos;
        this.listener = listener;
    }

    @Override
    public AppInfoViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate( R.layout.item_app, parent, false);
        return new AppInfoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AppInfoViewHolder holder, int position) {
        AppInfo appInfo = appInfos.get(position);
        String appName = appInfo.getAppName();
        Bitmap icon = byteArrayToBitmap(appInfo.getIcon());
        String packageName = appInfo.getPackageName();

        if (isFirst){
            isFirst = false;
            for (int i = 0; i < appInfos.size(); i++) {
                startAddProgress(appInfos.get(i));
                if (i==(appInfos.size()-1)){
                    System.out.println("最后一个");
                    end();
                }
            }

        }
        if (!appInfo.isStart()){
            appInfo.setStart(true);
            if (appInfo.getProgress()>=100){
                holder.ic_finish.setVisibility(View.VISIBLE);
                holder.clearingProgress.setVisibility(View.GONE);
                //  notifyItemChanged(position);
             //   notifyDataSetChanged();
            }else {

                //  startPr(holder.clearingProgress,appInfo);
                startTest(holder.clearingProgress,appInfo,holder.ic_finish,position);
            }

        }

        if (icon!=null){
            holder.app_icon.setImageBitmap(icon);
        }

        holder.appName.setText(appName);

       /* if (position==0){
            holder.appName.setText("appName");
        //    holder.clearingProgress.setProgress(50);

            startPr(holder.clearingProgress,appInfo);

        }else {
            holder.appName.setText(appName);
            holder.clearingProgress.setProgress(1);
        }*/
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }
    @Override
    public int getItemCount() {
        return appInfos.size();
    }

    public void end(){
        new CountDownTimer(10500, 100) {
            @Override
            public void onTick(long millisUntilFinished) {

            }
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onFinish() {

              notifyDataSetChanged();
                listener.isFinish();
            }
        }.start();
    }
    public void startTest(ProgressBar progressBar,AppInfo appInfo,ImageView ic_finish,int position){
        new CountDownTimer(10000, 10) {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onTick(long millisUntilFinished) {
                //appInfo.setProgress();
                int progress = appInfo.getProgress();
                progressBar.setProgress(progress);

                if (progress>=100&&appInfo.isRefreshStart()){
                    progressBar.setProgress(progress);
                    appInfo.setRefreshStart(false);
                    ic_finish.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                  //  notifyItemChanged(position);

                    notifyDataSetChanged();
                }
            }
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onFinish() {
                progressBar.setProgress(100);
                ic_finish.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
               // notifyItemChanged(position);
                notifyDataSetChanged();
            }
        }.start();
    }
    public void startPr(ProgressBar progressBar,AppInfo appInfo){

        int i = new Random().nextInt(6) + 4;
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

    public void startAddProgress(AppInfo appInfo){
        int i = new Random().nextInt(6) + 4;
        new CountDownTimer(12000, 10*i) {
            @Override
            public void onTick(long millisUntilFinished) {
                //appInfo.setProgress();
                appInfo.startProgress();
            }
            @Override
            public void onFinish() {
                System.out.println("*-*-*-:"+appInfo.getProgress());
            }
        }.start();
    }



    public class AppInfoViewHolder extends RecyclerView.ViewHolder {
        ImageView app_icon,ic_finish;
        TextView appName;
        ProgressBar clearingProgress;
        Button bt_startScan;

        public AppInfoViewHolder(View itemView) {
            super(itemView);
            app_icon = itemView.findViewById(R.id.app_icon);
            appName = itemView.findViewById(R.id.appName);
            clearingProgress = itemView.findViewById(R.id.clearingProgress);
            ic_finish = itemView.findViewById(R.id.ic_finish);


        }
    }
    public ArrayList<AppInfo> getSelectItems(){

        return appInfos;
    }
    public interface onFinishListener{
        void isFinish();
    }
}