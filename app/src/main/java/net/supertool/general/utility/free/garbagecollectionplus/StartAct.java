package net.supertool.general.utility.free.garbagecollectionplus;

import static net.supertool.general.utility.free.garbagecollectionplus.util.AppInfoUtil.getAppListAsync;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tencent.mmkv.MMKV;

import net.supertool.general.utility.free.garbagecollectionplus.entity.AppInfo;
import net.supertool.general.utility.free.garbagecollectionplus.util.AppInfoUtil;

import java.util.ArrayList;

public class StartAct extends AppCompatActivity implements View.OnClickListener{
    private boolean isReady = false;
    private boolean isReady2 = false;
    private Button bt_next;
    private ImageView select;
    private TextView privacy_policy;
    private boolean isSelect = false;
    private RelativeLayout layout_bt;
    private ProgressBar avi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_start);

        bt_next = findViewById(R.id.bt_next);
        select = findViewById(R.id.select);
        privacy_policy = findViewById(R.id.privacy_policy);
        layout_bt = findViewById(R.id.layout_bt);
        avi = findViewById(R.id.avi);
        MMKV.initialize(this);

        bt_next.setOnClickListener(this);
        select.setOnClickListener(this);
        privacy_policy.setOnClickListener(this);

        String policy = MMKV.defaultMMKV().decodeString("policy");
        if (policy!=null){
            switch (policy){
                case "no":
                    isSelect = false;
                    select.setImageResource(R.drawable.ic_select_no);
                    break;
                case "yes":
                    isSelect = true;
                    select.setImageResource(R.drawable.ic_select_yes);
                    break;
            }
        }

        getAppListAsync(this, new AppInfoUtil.AppListListener() {
            @Override
            public void onAppListFetched(ArrayList<AppInfo> appList) {

                String toJson = new Gson().toJson(appList);
                MMKV.defaultMMKV().encode("appList",toJson);

                runOnUiThread(()->{
                    isReady2 = true;
                    startMain();
                });



            }
        });


    }

    public void startMain(){
        layout_bt.setVisibility(View.VISIBLE);
        avi.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.bt_next){
            if (isSelect){
                startActivity(new Intent(StartAct.this,MainAct.class));
                finish();
            }
        }
        if (v.getId()==R.id.select){
            if (isSelect){
                isSelect = false;
                MMKV.defaultMMKV().encode("policy","no");
                select.setImageResource(R.drawable.ic_select_no);
            }else {
                isSelect = true;
                MMKV.defaultMMKV().encode("policy","yes");
                select.setImageResource(R.drawable.ic_select_yes);
            }
        }
        if (v.getId()==R.id.privacy_policy){
            startActivity(new Intent(StartAct.this,PolicyAct.class));
        }
    }
}