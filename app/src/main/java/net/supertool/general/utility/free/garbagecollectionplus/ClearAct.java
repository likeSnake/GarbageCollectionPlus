package net.supertool.general.utility.free.garbagecollectionplus;

import static net.supertool.general.utility.free.garbagecollectionplus.util.AppInfoUtil.getAppListAsync;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tencent.mmkv.MMKV;

import net.supertool.general.utility.free.garbagecollectionplus.adapter.AppInfoAdapter;
import net.supertool.general.utility.free.garbagecollectionplus.adapter.AppInfoListAdapter;
import net.supertool.general.utility.free.garbagecollectionplus.entity.AppInfo;
import net.supertool.general.utility.free.garbagecollectionplus.util.AppInfoUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import pl.droidsonroids.gif.AnimationListener;
import pl.droidsonroids.gif.GifDrawable;

public class ClearAct extends AppCompatActivity implements View.OnClickListener{
    private AppInfoAdapter adapter;
    private AppInfoListAdapter listAdapter;
    private RecyclerView recyclerView;
    private ListView my_list;
    private Button bt_startScan;
    private ArrayList<AppInfo> my_appList;
    private ImageView ic_back;
    private TextView rubbishSize;
    private String rubbish;
    private boolean isPause = false;
    private ImageView my_gif,rocket_gif_one;
    private RelativeLayout layout_gif;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clear);
        Window window = getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.my_title));
        initUI();
        initData();
        initListener();

    }
    public void initUI(){
        recyclerView = findViewById(R.id.recyclerView);
        bt_startScan = findViewById(R.id.bt_startScan);
        ic_back = findViewById(R.id.ic_back);
        rubbishSize = findViewById(R.id.rubbishSize);
        my_gif = findViewById(R.id.my_gif);
        layout_gif = findViewById(R.id.layout_gif);
        rocket_gif_one = findViewById(R.id.rocket_gif_one);

    }
    public void initData(){
        rubbish = getIntent().getStringExtra("rubbish");
        if (rubbish!=null){
            rubbishSize.setText(rubbish);
        }
        String appList = MMKV.defaultMMKV().decodeString("appList");
        if (appList!=null){
            my_appList =  new Gson().fromJson(appList,new TypeToken<ArrayList<AppInfo>>(){}.getType());
            startAdapter(my_appList);
        }

        Glide.with(ClearAct.this)
                .asGif()
                .load(R.drawable.rocket_gif_one)
                .apply(RequestOptions.centerInsideTransform())
                .into(rocket_gif_one);
    }
    public void initListener(){
        bt_startScan.setOnClickListener(this);
        ic_back.setOnClickListener(this);
        bt_startScan.setVisibility(View.GONE);
    }
    public static void sortAppListBySize(ArrayList<AppInfo> appList) {
        Collections.sort(appList, new Comparator<AppInfo>() {
            @Override
            public int compare(AppInfo appInfo1, AppInfo appInfo2) {
                return Long.compare(appInfo2.getSize(), appInfo1.getSize()); // 从大到小排序
            }
        });
    }



    public void startAdapter(ArrayList<AppInfo> appInfos){
        bt_startScan.setVisibility(View.GONE);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
   //     manager.setInitialPrefetchItemCount(appInfos.size());


        adapter = new AppInfoAdapter(this, appInfos, new AppInfoAdapter.onFinishListener() {
            @Override
            public void isFinish() {
             //   recyclerView.setItemViewCacheSize(2);
                bt_startScan.setVisibility(View.VISIBLE);
                if (!isPause) {
                    layout_gif.setVisibility(View.VISIBLE);
               /* Glide.with(ClearAct.this)
                        .asGif()
                        .load(R.drawable.rocket)
                        .apply(RequestOptions.centerInsideTransform())
                        .into(my_gif);*/

                    try {
                        GifDrawable gifDrawable = new GifDrawable(getResources(), R.drawable.rocket);

                        gifDrawable.setSpeed(0.6f); // 设置播放速度（1.0f为正常速度）
                        gifDrawable.setLoopCount(1); // 设置循环次数
                        gifDrawable.addAnimationListener(new AnimationListener() {
                            @Override
                            public void onAnimationCompleted(int i) {
                                layout_gif.setVisibility(View.GONE);
                                if (!isPause) {
                                    startDeleteDialog();
                                }

                            }
                        });
                        my_gif.setImageDrawable(gifDrawable);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

            }
        });


        recyclerView.setLayoutManager(manager);
        recyclerView.setItemViewCacheSize(appInfos.size());
        recyclerView.setAdapter(adapter);


    }

    public void startListAdapter(ArrayList<AppInfo> appInfos){

        listAdapter = new AppInfoListAdapter(this,appInfos);

        my_list.setAdapter(listAdapter);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.bt_startScan){
            if (my_appList!=null) {
                for (AppInfo appInfo : my_appList) {
                    appInfo.setProgress(0);
                    appInfo.setStart(false);
                    appInfo.setRefreshStart(true);
                }
                startAdapter(my_appList);
            }
        }
        if (v.getId() == R.id.ic_back){
            finish();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        isPause = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        isPause = false;
    }

    public void startDeleteDialog() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog);
        dialog.getWindow().setBackgroundDrawableResource(R.color.my_colors);
        dialog.setCancelable(false);
        ((TextView)dialog.findViewById(R.id.rubbishSize)).setText(rubbish);
        dialog.findViewById(R.id.dialog_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}